package com.classnote.backend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
/**
 * ClassName：Whisperutil
 * Package:com.classnote.backend.util
 * Description:
 *
 * @Auther：zh
 * @Create: 2025/5/15 19:32
 * @Version: 1.0
 */
public class WhisperUtil {

    public static String runWhisper(String audioPath, String model, String outputPath) {
        try {
            // 确保ffmpeg在系统路径中
            String ffmpegPath = "D:\\Ai\\whisper-local\\venv\\Scripts";
            String currentPath = System.getenv("PATH");
            ProcessBuilder pb = new ProcessBuilder(
                "cmd",
                "/c",
                "set",
                "PATH=" + ffmpegPath + ";" + currentPath,
                "&&",
                "D:\\Ai\\whisper-local\\venv\\Scripts\\whisper.exe",
                audioPath,
                "--model", model,
                "--language", "Chinese",
                "--output_dir", "D:\\Ai\\whisper-local\\output"
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 读取输出日志
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "调用 Whisper 失败，退出码：" + exitCode + "\n日志：" + output;
            }

            // 构造正确的输出文件路径
            String fileName = audioPath.substring(audioPath.lastIndexOf("\\") + 1);
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            String txtOutputPath = "D:\\Ai\\whisper-local\\output\\" + fileName + ".txt";
            
            if (!new File(txtOutputPath).exists()) {
                return "转写失败：找不到输出文件 " + txtOutputPath + "\n日志：" + output;
            }
            return java.nio.file.Files.readString(java.nio.file.Paths.get(txtOutputPath));
        } catch (Exception e) {
            return "调用错误：" + e.getMessage();
        }
    }
}