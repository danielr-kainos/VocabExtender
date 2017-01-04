package com.example.eti.vocabextender.model;

import java.io.Serializable;

public class Word implements Serializable {
    private int id;

    private double memorizedValue;

    private int correctAnswers = 0;

    private String word;

    private String definition;

    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMemorizedValue() {
        return memorizedValue;
    }

    public void setMemorizedValue(double memorizedValue) {
        this.memorizedValue = memorizedValue;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
