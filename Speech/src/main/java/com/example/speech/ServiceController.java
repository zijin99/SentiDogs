package com.example.speech;

import com.google.api.client.util.Lists;
import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class ServiceController {
    @Autowired
    private SpeechService speechService;

    @Autowired
    private EntitySentimentService entitySentimentService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "download")
    @ResponseBody
    public void download() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "file/{fileName}")
    @ResponseBody
    public List<SpeechEntity> getFileName(@PathVariable("fileName") String  file) {

        String filePath = speechService.generatePath(file);
//        System.out.println(filePath);
        String trans = speechService.asyncRecognizeGcs(filePath);
        List<SpeechEntity> list = entitySentimentService.analyseSpeech(trans);
        List<SpeechEntity> sorted = entitySentimentService.generateList(list);
        return sorted;
    }
}
