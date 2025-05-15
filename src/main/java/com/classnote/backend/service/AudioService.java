package com.classnote.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName：AudioService
 * Package:com.classnote.backend.service
 * Description:
 *
 * @Auther：zh
 * @Create 2025/5/8 19:06
 * @Version 1.0
 */
@Service
public class AudioService {

    @Value("${audio.save.dir:/tmp/classnote/audio/}")
    private String audioSaveDir;

    public String saveAudio(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "-" + originalName;
        File dir = new File(audioSaveDir);
        if (!dir.exists())
            dir.mkdirs();

        File saveFile = new File(audioSaveDir + fileName);
        file.transferTo(saveFile);

        return saveFile.getAbsolutePath();
    }
}
