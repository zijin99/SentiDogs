import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpeechService {
    public String speechRecognize(String localFilePath) {
        try (SpeechClient speechClient = SpeechClient.create()) {
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
    public String generatePath(String fileName) {
        return "/Users/yanghaolin/Desktop/MP4/restaurant.wav";
    }
}
