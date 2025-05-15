package com.classnote.backend.controller;

import com.classnote.backend.util.WhisperUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.classnote.backend.dto.Result;
import com.classnote.backend.service.AudioService;

import java.io.File;
import java.util.UUID;

/**
 * ClassName：AudioController
 * Package:com.classnote.backend.controller
 * Description:
 *
 * @Auther：zh
 * @Create 2025/5/8 19:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping(value = "/upload", produces = "application/json")
    public ResponseEntity<Result<String>> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            // Validate the file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Result.error("文件不能为空"));
            }

            String path = audioService.saveAudio(file);
            Result<String> result = Result.success(path);
//            System.out.println("上传成功，文件路径：" + path);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.error("上传失败：" + e.getMessage()));
        }
    }

    @PostMapping("/transcribe")
    public ResponseEntity<Result<String>> transcribeAudio(@RequestParam("file") MultipartFile file) {
        try {
            // Validate the file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Result.error("文件不能为空"));
            }

            // 保存上传的音频文件
            String audioPath = audioService.saveAudio(file);

            // 设置输出文件的绝对路径（使用音频文件名作为输出文件名）
            String fileName = audioPath.substring(audioPath.lastIndexOf("\\") + 1);
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            String outputPath = "D:/Ai/whisper-local/output/" + fileName + ".txt";
            
            String resultText = WhisperUtil.runWhisper(audioPath, "small", outputPath);
            if (resultText.startsWith("调用错误") || resultText.startsWith("调用 Whisper 失败") || resultText.startsWith("转写失败")) {
                return ResponseEntity.status(500).body(Result.error(resultText));
            }

            return ResponseEntity.ok(Result.success(resultText));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.error("处理失败: " + e.getMessage()));
        }
    }
}
