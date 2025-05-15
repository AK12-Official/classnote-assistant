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
            // 保存上传的音频文件
            String audioPath = "audio/" + file.getOriginalFilename();
            file.transferTo(new File(audioPath));

            // 调用 Python 脚本进行转写
            String outputPath = "output/" + UUID.randomUUID() + ".txt";
            String resultText = WhisperUtil.runWhisper(audioPath, "small", outputPath);

            return ResponseEntity.ok(Result.success(resultText));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.error("处理失败: " + e.getMessage()));
        }
    }
}
