package com.example.speech;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class SpeechService {


    private CredentialsProvider credentialsProvider;
    @Autowired
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }
    private SpeechSettings settings = null;
    @PostConstruct
    public void init() throws IOException {
        settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
    }

    public String speechRecognize(String localFilePath) {
        try (SpeechClient speechClient = SpeechClient.create(settings)) {
            String languageCode = "en-US";
            // Sample rate in Hertz of the audio data sent
            // Encoding of audio data sent. This sample sets this explicitly.
            // This field is optional for FLAC and WAV audio formats.
            RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.ENCODING_UNSPECIFIED;
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setLanguageCode(languageCode)
                            .setEncoding(encoding)
                            .setEnableWordTimeOffsets(true)
                            .setEnableAutomaticPunctuation(true)
                            .setAudioChannelCount(1)
                            .build();
            Path path = Paths.get(localFilePath);
            byte[] data = Files.readAllBytes(path);
            ByteString content = ByteString.copyFrom(data);
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(content).build();
            RecognizeRequest request =
                    RecognizeRequest.newBuilder().setConfig(config).setAudio(audio).build();
            RecognizeResponse response = speechClient.recognize(request);
            for (SpeechRecognitionResult result : response.getResultsList()) {
                // First alternative is the most probable result
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcript: %s\n", alternative.getTranscript());
                return alternative.getTranscript();
            }
            return "";
        } catch (Exception exception) {
            System.err.println("Failed to create the client due to: " + exception);
        }
        return "";
    }

    public void convertToWav (String fileName) {
        try {
            File source2 = new File("/Users/yanghaolin/Desktop/Restaurant.mp4");
            File target2 = new File("/Users/yanghaolin/Desktop/Restaurant.wav");
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

    public String asyncRecognizeGcs(String gcsUri)  {
        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        try (SpeechClient speech = SpeechClient.create(settings)) {
            String languageCode = "en-US";
            RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.ENCODING_UNSPECIFIED;

            // Configure remote file request for FLAC
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setLanguageCode(languageCode)
                            .setEncoding(encoding)
                            .setEnableWordTimeOffsets(true)
                            .setEnableAutomaticPunctuation(true)
                            .setAudioChannelCount(1)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speech.longRunningRecognizeAsync(config, audio);
            while (!response.isDone()) {
                System.out.println("Waiting for response...");
                Thread.sleep(10000);
            }

            List<SpeechRecognitionResult> results = response.get().getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s\n", alternative.getTranscript());
                return alternative.getTranscript();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String generatePath(String fileName) {
        String str = "gs://test-senti-frontend.appspot.com/" + fileName;
        return str;
    }
}
