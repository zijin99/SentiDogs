import java.io.Serializable;

public class SpeechEntity implements Serializable {
    private String name;
    private double sentiment;
    private String type;
    private String content;

    public SpeechEntity(String name, double sentiment) {
        this.name = name;
        this.sentiment = sentiment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
