package com.classnote.backend.util;

import java.io.BufferedReader;
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
            // 构建命令
            ProcessBuilder pb = new ProcessBuilder("python", "transcribe_audio.py", audioPath, model, outputPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 读取输出日志（可选）
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "调用 Whisper 脚本失败，退出码：" + exitCode + "\n日志：" + output;
            }

            // 读取转写结果
            return java.nio.file.Files.readString(java.nio.file.Paths.get(outputPath));
        } catch (Exception e) {
            return "调用错误：" + e.getMessage();
        }
    }
}