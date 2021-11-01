//import com.google.api.gax.longrunning.OperationFuture;
//import com.google.api.gax.paging.Page;
//import com.google.auth.oauth2.ComputeEngineCredentials;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.language.v1.*;
//import com.google.cloud.speech.v1.*;
//
//import com.google.common.collect.Lists;
//import com.google.protobuf.ByteString;
//
//import org.slf4j.Logger;
//import ws.schild.jave.Encoder;
//import ws.schild.jave.MultimediaObject;
//import ws.schild.jave.encode.AudioAttributes;
//import ws.schild.jave.encode.EncodingAttributes;
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//public class Speech {
//    public static void main(String args[]){
//        try {
////            File source = new File("/Users/yanghaolin/Desktop/Video.avi");
////            File target = new File("/Users/yanghaolin/Desktop/target.mp3");
////            AudioAttributes audio = new AudioAttributes();
////            audio.setCodec("libmp3lame");
////            audio.setBitRate(128000);
////            audio.setChannels(2);
////            audio.setSamplingRate(44100);
////            EncodingAttributes attrs = new EncodingAttributes();
////            attrs.setOutputFormat("mp3");
////            attrs.setAudioAttributes(audio);
////            Encoder encoder = new Encoder();
////            encoder.encode(new MultimediaObject(source), target, attrs);
//
////            File source2 = new File("/Users/yanghaolin/Desktop/Restaurant.mp4");
////            File target2 = new File("/Users/yanghaolin/Desktop/Restaurant.wav");
////            AudioAttributes audio2 = new AudioAttributes();
////            audio2.setCodec("pcm_s16le");
////            EncodingAttributes attrs2 = new EncodingAttributes();
////            attrs2.setOutputFormat("wav");
////            attrs2.setAudioAttributes(audio2);
////            Encoder encoder2 = new Encoder();
////            encoder2.encode(new MultimediaObject(source2), target2, attrs2);
//
//            //transcribeModelSelection("gs://cmu-speech/input/target.wav");
//            //generateWav();
//           String transcript = sampleRecognize("/Users/yanghaolin/Desktop/MP4/restaurant.wav");
//           analyseSentiment(transcript);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void generateWav() {
//        try {
//            File source2 = new File("/Users/yanghaolin/Desktop/MP4/Restaurant.mp4");
//            File target2 = new File("/Users/yanghaolin/Desktop/MP4/Restaurant.wav");
//            AudioAttributes audio2 = new AudioAttributes();
//            audio2.setCodec("pcm_s16le");
//            EncodingAttributes attrs2 = new EncodingAttributes();
//            attrs2.setOutputFormat("wav");
//            attrs2.setAudioAttributes(audio2);
//            Encoder encoder2 = new Encoder();
//            encoder2.encode(new MultimediaObject(source2), target2, attrs2);
//        }   catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String sampleRecognize(String localFilePath) {
//        try (SpeechClient speechClient = SpeechClient.create()) {
//            String languageCode = "en-US";
//            // Sample rate in Hertz of the audio data sent
//            // Encoding of audio data sent. This sample sets this explicitly.
//            // This field is optional for FLAC and WAV audio formats.
//            RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.ENCODING_UNSPECIFIED;
//            RecognitionConfig config =
//                    RecognitionConfig.newBuilder()
//                            .setLanguageCode(languageCode)
//                            .setEncoding(encoding)
//                            .setEnableWordTimeOffsets(true)
//                            .setEnableAutomaticPunctuation(true)
//                            .setAudioChannelCount(1)
//                            .build();
//            Path path = Paths.get(localFilePath);
//            byte[] data = Files.readAllBytes(path);
//            ByteString content = ByteString.copyFrom(data);
//            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(content).build();
//            RecognizeRequest request =
//                    RecognizeRequest.newBuilder().setConfig(config).setAudio(audio).build();
//            RecognizeResponse response = speechClient.recognize(request);
//            for (SpeechRecognitionResult result : response.getResultsList()) {
//                // First alternative is the most probable result
//                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                System.out.printf("Transcript: %s\n", alternative.getTranscript());
//                return alternative.getTranscript();
//            }
//            return "";
//        } catch (Exception exception) {
//            System.err.println("Failed to create the client due to: " + exception);
//        }
//        return "";
//    }
//
//    public static void analyseSentiment(String input) throws IOException {
//        try (LanguageServiceClient language = LanguageServiceClient.create()) {
//            Document doc = Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
//            AnalyzeEntitySentimentRequest request =
//                    AnalyzeEntitySentimentRequest.newBuilder()
//                            .setDocument(doc)
//                            .setEncodingType(EncodingType.UTF16)
//                            .build();
//
//            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
//            Map<String, Double> map = new HashMap<>();
//            // Print the response
//            for (Entity entity : response.getEntitiesList()) {
//                System.out.printf("Entity: %s\n", entity.getName());
//                System.out.printf("Salience: %.3f\n", entity.getSalience());
//                System.out.println("Metadata: ");
//                System.out.println("Type" + entity.getType());
//                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
//                    System.out.printf("%s : %s", entry.getKey(), entry.getValue());
//                }
//                for (EntityMention mention : entity.getMentionsList()) {
//                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
//                    System.out.printf("Content: %s\n", mention.getText().getContent());
//                    System.out.println("Magnitude : " + mention.getSentiment().getMagnitude());
//                    System.out.println("Sentiment Score: " + mention.getSentiment().getScore());
//
//                    System.out.printf("Type: %s\n\n", mention.getType());
//                }
//                map.put(entity.getName(), (double) entity.getSentiment().getScore());
//            }
//            List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map.entrySet());
//            Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
//                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
//                    if (o2.getValue() - o1.getValue() > 0) {
//                        return 1;
//                    }else if (o2.getValue().equals(o1.getValue())){
//                        return 0;
//                    }else {
//                        return -1;
//                    }
//                }
//            });
//            for (Map.Entry<String, Double> l : list) {
//                System.out.println(l.getKey() + ":" + l.getValue());
//            }
//        }
//    }
//
//    public static void transcribeModelSelection(String gcsUri) throws Exception {
//        // Instantiates a client
//        try (SpeechClient speech = SpeechClient.create()) {
//
//            // Configure remote file request for FLAC
//            RecognitionConfig config =
//                    RecognitionConfig.newBuilder()
//                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//                            .setLanguageCode("en-US")
////                            .setSampleRateHertz(16000)
//                            .setAudioChannelCount(2)
//                            .build();
//            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
//
//            // Use non-blocking call for getting file transcription
//            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
//                    speech.longRunningRecognizeAsync(config, audio);
//            while (!response.isDone()) {
//                System.out.println("Waiting for response...");
//                Thread.sleep(10000);
//            }
//
//            List<SpeechRecognitionResult> results = response.get().getResultsList();
//
//            for (SpeechRecognitionResult result : results) {
//                // There can be several alternative transcripts for a given chunk of speech. Just use the
//                // first (most likely) one here.
//                for (SpeechRecognitionAlternative sp: result.getAlternativesList()) {
//                    System.out.printf("Transcription: %s\n", sp.getTranscript());
//                }
////                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
////                System.out.printf("Transcription: %s\n", alternative.getTranscript());
//            }
//        }
//    }
//}
