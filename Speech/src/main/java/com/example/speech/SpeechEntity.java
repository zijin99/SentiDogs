package com.example.speech;

import java.util.Map;

public class SpeechEntity{
    private String name;
    private double sentiment;
    private String type;
    private Map<String, String> metadataMap;


    public SpeechEntity(String name, double sentiment, String type, Map<String, String> metadataMap) {
        this.name = name;
        this.sentiment = sentiment;
        this.type = type;
        this.metadataMap = metadataMap;
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

    public Map<String, String> getMetadataMap() { return metadataMap; }

    public void setMetadataMap(Map<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }



}
