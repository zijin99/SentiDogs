package com.example.speech;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.storage.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class StorageService {

    private CredentialsProvider credentialsProvider;
    @Autowired
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }


    public void downloadObject(
            String projectId, String bucketName, String objectName, String destFilePath, String fileFullName) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The path to which the file should be downloaded
        // String destFilePath = "/local/path/to/file.txt";
        try {
            Storage storage = StorageOptions.newBuilder().setCredentials(credentialsProvider.getCredentials()).setProjectId(projectId).build().getService();

            Blob blob = storage.get(BlobId.of(bucketName, objectName));
            blob.downloadTo(Paths.get(destFilePath));

            System.out.println(
                    "Downloaded object "
                            + objectName
                            + " from bucket name "
                            + bucketName
                            + " to "
                            + destFilePath);
            convertToWav(fileFullName);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadObject(
            String projectId, String bucketName, String objectName, String filePath) throws IOException {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The path to your file to upload
        // String filePath = "path/to/your/file"

        Storage storage = StorageOptions.newBuilder().setCredentials(credentialsProvider.getCredentials()).setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

//        ClassPathResource classPathResource = new ClassPathResource(filePath);
//        InputStream inputStream = classPathResource.getInputStream();

        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public void convertToWav (String fileFullName) {
        try {
            String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/download/";
            String mp4FilePath = basePath + fileFullName;
//            ClassPathResource baseClassPathResource = new ClassPathResource("static/download/");
//
//            ClassPathResource mp4ClassPathResource = new ClassPathResource("static/download/" + fileFullName);
//            InputStream mp4InputStream = mp4ClassPathResource.getInputStream();
//
//            ClassPathResource wavClassPathResource = new ClassPathResource("static/download/" + fileFullName.split("\\.")[0] + ".wav");
//
//            String baseMP4Path = mp4ClassPathResource.getPath();

            String wavFileName = fileFullName.split("\\.")[0] + ".wav";
            String wavFilePath = basePath + wavFileName;

            System.out.println(wavFilePath + ">>>>>>>");
//            convertInputStreamToFile(baseMP4Path, mp4InputStream);

            File source2 = new File(mp4FilePath);
            File target2 = new File(wavFilePath);

            AudioAttributes audio2 = new AudioAttributes();
            audio2.setCodec("pcm_s16le");
            EncodingAttributes attrs2 = new EncodingAttributes();
            attrs2.setOutputFormat("wav");
            attrs2.setAudioAttributes(audio2);
            Encoder encoder2 = new Encoder();
            encoder2.encode(new MultimediaObject(source2), target2, attrs2);

            uploadObject("test-senti-frontend",
                    "test-senti-frontend.appspot.com", wavFileName,
                    wavFilePath);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean downloadAndConvertAndUpload(String fileFullName) {
        try {
            /**
             * this file name should include mp4 or wav
             */
            ClassPathResource classPathResource = new ClassPathResource("static/download/");
//            String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/download/";
//            String filePath = classPathResource.getPath() + fileFullName;
//            InputStream inputStream = classPathResource.getInputStream();

            String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/download/";
            File f = new File(basePath);
            if (!f.exists()) {
                f.mkdirs();
            }

            downloadObject("test-senti-frontend",
                    "test-senti-frontend.appspot.com", fileFullName,
                    basePath + fileFullName, fileFullName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void convertInputStreamToFile(String filePath, InputStream inputStream) {
        File file = new File(filePath);
        try(OutputStream outputStream = new FileOutputStream(file)){
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            // handle exception here
        } catch (IOException e) {
            // handle exception here
        }
    }
}
