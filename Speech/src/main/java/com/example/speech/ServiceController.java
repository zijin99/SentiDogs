package com.example.speech;

import com.google.api.client.util.Lists;
import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechSettings;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class ServiceController {
    @Autowired
    private SpeechService speechService;

    @Autowired
    private EntitySentimentService entitySentimentService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "file/{fileName}")
    @ResponseBody
    public List<SpeechEntity> getFileName(@PathVariable("fileName") String  file) {

        /**
         * file should include string mp4 or wav
         */
        String filePath;
        ClassPathResource classPathResource = new ClassPathResource("static/download/");

        File f = new File(classPathResource.getPath());
        if (!f.exists()) {
            f.mkdirs();
        }

        if (file.contains(".mp4")) {
            storageService.downloadAndConvertAndUpload(file);
            filePath = speechService.generatePath(file.split("\\.")[0] + ".wav");
        } else {
            filePath = speechService.generatePath(file);
        }

        String trans = speechService.asyncRecognizeGcs(filePath);
        List<SpeechEntity> list = entitySentimentService.analyseSpeech(trans);
        List<SpeechEntity> sorted = entitySentimentService.generateList(list);
        return sorted;
    }
}
