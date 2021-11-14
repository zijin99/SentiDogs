package com.example.speech;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class StorageService {

    private CredentialsProvider credentialsProvider;
    @Autowired
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }


    public void downloadObject(
            String projectId, String bucketName, String objectName, String destFilePath) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The path to which the file should be downloaded
        // String destFilePath = "/local/path/to/file.txt";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        blob.downloadTo(Paths.get(destFilePath));

        System.out.println(
                "Downloaded object "
                        + objectName
                        + " from bucket name "
                        + bucketName
                        + " to "
                        + destFilePath);
        convertToWav("");
    }

    public void convertToWav (String name) {
        try {
            String resourcesPath = ResourceUtils.getURL("classpath:").getPath();
            String filePath = resourcesPath + "files/";

            File source2 = new File(filePath + "Restaurant.mp4");
            File target2 = new File(filePath + "Restaurant.wav");
            AudioAttributes audio2 = new AudioAttributes();
            audio2.setCodec("pcm_s16le");
            EncodingAttributes attrs2 = new EncodingAttributes();
            attrs2.setOutputFormat("wav");
            attrs2.setAudioAttributes(audio2);
            Encoder encoder2 = new Encoder();
            encoder2.encode(new MultimediaObject(source2), target2, attrs2);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
