package com.example.speech;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.language.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class EntitySentimentService {
    private static HttpURLConnection con;
    private CredentialsProvider credentialsProvider;
    @Autowired
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }
    private LanguageServiceSettings settings = null;
    @PostConstruct
    public void init() throws IOException {
        settings = LanguageServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
    }

    public List<SpeechEntity> analyseSpeech(String input) {
        try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
            Document doc = Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeEntitySentimentRequest request =
                    AnalyzeEntitySentimentRequest.newBuilder()
                            .setDocument(doc)
                            .setEncodingType(EncodingType.UTF16)
                            .build();

            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            List<SpeechEntity> entityList = new ArrayList<>();
            List<SpeechEntity> result = new ArrayList<>();
            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.println("Metadata: ");
                System.out.println("Type: " + entity.getType());
                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                    System.out.printf("%s : %s", entry.getKey(), entry.getValue());
                }
                for (EntityMention mention : entity.getMentionsList()) {
                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
                    System.out.printf("Content: %s\n", mention.getText().getContent());
                    System.out.println("Magnitude : " + mention.getSentiment().getMagnitude());
                    System.out.println("Sentiment Score: " + mention.getSentiment().getScore());

                    System.out.printf("Type: %s\n\n", mention.getType());
                }
                if (entity.getType().name().equalsIgnoreCase("organization") || entity.getType().name().equalsIgnoreCase("location")) {
                    entityList.add(new SpeechEntity(entity.getName(), entity.getSentiment().getScore(), entity.getType().name(), entity.getMetadataMap()));
                }

            }

            //call the restaurant detector api
            var url = "https://som-restaurant-detector.azurewebsites.net/detect";
            for (var entity: entityList) {
                try {

                    var myurl = new URL(url);
                    con = (HttpURLConnection) myurl.openConnection();

                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", "Java client");
                    con.setRequestProperty("Content-Type", "application/json");

                    var data = "{\"text\":\"" + entity.getName() + "\"}";
                    byte[] postData = data.getBytes(StandardCharsets.UTF_8);
                    try (var wr = new DataOutputStream(con.getOutputStream())) {
                        wr.write(postData);
                    }

                    StringBuilder content;

                    try (var br = new BufferedReader(
                            new InputStreamReader(con.getInputStream()))) {

                        String line;
                        content = new StringBuilder();

                        while ((line = br.readLine()) != null) {
                            content.append(line);
                            content.append(System.lineSeparator());
                        }
                    }

                    System.out.println(content.toString());
                    if (content.toString().trim().equalsIgnoreCase("yes")) {
                        result.add(entity);
                    }

                } finally {

                    con.disconnect();
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SpeechEntity> generateList(List<SpeechEntity> list) {
        Collections.sort(list, new Comparator<SpeechEntity>() {
            public int compare(SpeechEntity o1, SpeechEntity o2) {
                if (o2.getSentiment() - o1.getSentiment() > 0) {
                    return 1;
                }else if (o2.getSentiment() == o1.getSentiment()){
                    return 0;
                }else {
                    return -1;
                }
            }
        });
        return list;
    }


}
