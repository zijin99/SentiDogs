package com.example.speech;

public class SpeechEntity{
    private String name;
    private double sentiment;
    private String type;


    public SpeechEntity(String name, double sentiment, String type) {
        this.name = name;
        this.sentiment = sentiment;
        this.type = type;
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


}
