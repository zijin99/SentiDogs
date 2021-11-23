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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ServiceController {
    @Autowired
    private SpeechService speechService;

    @Autowired
    private EntitySentimentService entitySentimentService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "file/{fileName}")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Map<String, String> getFileName(@PathVariable("fileName") String file) {

        /**
         * file should include string mp4 or wav
         */
        String filePath;
        String fileType;
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

        if (file.startsWith("restaurant_"))
            fileType = "restaurant";
        else
            fileType = "travel";

        String trans = speechService.asyncRecognizeGcs(filePath);
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("fileType", fileType);
        returnMap.put("transcription", trans);

        return returnMap;
    }

    @PostMapping(value = "/analyze")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<SpeechEntity> postText(@RequestBody Map<String, String> requestMap) {
        System.out.println("Entered post request");
        System.out.println(requestMap.get("transcription") + " and " + requestMap.get("fileType"));

        List<SpeechEntity> list = entitySentimentService.analyseSpeech(requestMap.get("transcription"), requestMap.get("fileType"));
        List<SpeechEntity> sorted = entitySentimentService.generateList(list);
        return sorted;
    }
}