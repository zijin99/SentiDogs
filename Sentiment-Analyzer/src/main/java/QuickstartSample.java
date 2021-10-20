// Imports the Google Cloud client library
import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;

import java.util.Map;

public class QuickstartSample {
    public static void main(String... args) throws Exception {
        // Instantiates a client
        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            // The text to analyze
            String text = "I don't like eating at Wendys. The food is terribly deep fried and still not tasty. It honestly feels like someone spat in my food. However, I do really enjoy eating at Taco Bell. They really do serve the best Tacos I have ever eaten. You should definitely go try it out! And while the burgers at BurgerKing are pretty huge and good for the price, the taste is quite average in my opinion. So I'll let you be the judge of that.";
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

            AnalyzeEntitySentimentRequest request =
                    AnalyzeEntitySentimentRequest.newBuilder()
                            .setDocument(doc)
                            .setEncodingType(EncodingType.UTF16)
                            .build();

            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);

            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.printf("Sentiment : %s\n", entity.getSentiment());
                System.out.printf("Type : %s\n", entity.getType());
                System.out.println("Metadata: ");
                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                    System.out.printf("%s : %s \n", entry.getKey(), entry.getValue());

                }
                for (EntityMention mention : entity.getMentionsList()) {
                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
                    System.out.printf("Content: %s\n", mention.getText().getContent());
                    System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
                    System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
                    System.out.printf("Type : %s\n\n", mention.getType());
                }
            }
        }
    }
}