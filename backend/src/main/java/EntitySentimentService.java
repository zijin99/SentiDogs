import com.google.cloud.language.v1.*;

import java.io.IOException;
import java.util.*;

public class EntitySentimentService {
    public List<SpeechEntity> analyseSpeech(String input) {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeEntitySentimentRequest request =
                    AnalyzeEntitySentimentRequest.newBuilder()
                            .setDocument(doc)
                            .setEncodingType(EncodingType.UTF16)
                            .build();

            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            Map<String, Double> map = new HashMap<>();
            List<SpeechEntity> result = new ArrayList<>();
            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.println("Metadata: ");
                System.out.println("Type" + entity.getType());
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
                result.add(new SpeechEntity(entity.getName(), entity.getSentiment().getScore()));
                map.put(entity.getName(), (double) entity.getSentiment().getScore());
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
