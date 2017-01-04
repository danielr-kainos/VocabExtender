package com.example.eti.vocabextender.model;

import java.util.LinkedList;
import java.util.List;

public class Question {
    private String question;

    private List<String> answers = new LinkedList<>();

    private int correctAnswer;

    private Word currentWord;

    public Question(Word correctAnswer, List<Word> otherAnswers) {
        int randomQuestionType = (int) (Math.random() * 2);

        switch (randomQuestionType) {
            case 0:
                selectWordGivenDefinition(correctAnswer, otherAnswers);
                break;
            case 1:
                selectDefinitionGivenWord(correctAnswer, otherAnswers);
                break;
        }
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAnswer(int index) {
        return answers.get(index);
    }

    public String getQuestion() {
        return question;
    }

    public Word getCurrentWord() {
        return currentWord;
    }

    private void selectWordGivenDefinition(Word correctAnswer, List<Word> otherAnswers) {
        this.currentWord = correctAnswer;
        this.question = correctAnswer.getDefinition();

        while (!otherAnswers.isEmpty()) {
            int index = (int) (Math.random() * otherAnswers.size());
            answers.add(otherAnswers.get(index).getWord());
            otherAnswers.remove(index);
        }

        this.correctAnswer = (int) (Math.random() * (answers.size() + 1));
        answers.add(this.correctAnswer, correctAnswer.getWord());
    }

    private void selectDefinitionGivenWord(Word correctAnswer, List<Word> otherAnswers) {
        this.currentWord = correctAnswer;
        this.question = correctAnswer.getWord();

        while (!otherAnswers.isEmpty()) {
            int index = (int) (Math.random() * otherAnswers.size());
            answers.add(otherAnswers.get(index).getDefinition());
            otherAnswers.remove(index);
        }

        this.correctAnswer = (int) (Math.random() * (answers.size() + 1));
        answers.add(this.correctAnswer, correctAnswer.getDefinition());
    }
}
