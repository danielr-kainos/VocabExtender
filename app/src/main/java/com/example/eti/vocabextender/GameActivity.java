package com.example.eti.vocabextender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eti.vocabextender.model.GameMode;
import com.example.eti.vocabextender.model.Question;
import com.example.eti.vocabextender.model.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    // constants
    static final int NUMBER_OF_WORDS_TO_LEARN = 3;
    static final int NUMBER_OF_ANSWERS = 4 - 1;
    static final int NEEDED_CORRECT_ANSWERS = 3;
    static final int SECONDS_TO_ADD_AFTER_CORRECT_ANSWER = 3;
    static final int INITIAL_TIME_IN_SECONDS = 10;

    // logic related variables
    private WordsDictionary wordsDictionary;
    private GameMode gameMode;
    private int points = 0;
    private int health = 3;
    private long timeLeft = INITIAL_TIME_IN_SECONDS;
    private ArrayList<Word> words;
    private Question currentQuestion;
    private Timer timer = new Timer();

    // user interface
    private Intent summaryIntent;
    private TextView questionText;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private TextView timeValText;
    private List<ImageView> hearts = new ArrayList<>(3);

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        summaryIntent = new Intent(GameActivity.this, SummaryActivity.class);
        wordsDictionary = new WordsDictionary(this);

        // save references to UI elements
        questionText = (TextView) findViewById(R.id.question);
        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);
        timeValText = (TextView) findViewById(R.id.timeValText);
        hearts.add((ImageView) findViewById(R.id.heart1));
        hearts.add((ImageView) findViewById(R.id.heart2));
        hearts.add((ImageView) findViewById(R.id.heart3));

        // set onClick actions for buttons with answers
        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(0);
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(1);
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(2);
            }
        });
        answer4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(3);
            }
        });

        // set correct game mode
        Intent myIntent = getIntent();
        switch (myIntent.getStringExtra("mode")) {
            case "learn":
                this.gameMode = GameMode.LEARN_MODE;
                summaryIntent.putExtra("mode", "learn");

                // hide health and time UI
                timeValText.setAlpha(0);
                findViewById(R.id.timeDescText).setAlpha(0);
                for (int i = 0; i < hearts.size(); i++) {
                    hearts.get(i).setImageAlpha(0);
                }
                break;
            case "test":
                this.gameMode = GameMode.TEST_MODE;
                summaryIntent.putExtra("mode", "test");

                // decrease timeLeft every second
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timeLeft -= 1;

                        if (timeLeft <= 0) {
                            timer.cancel();
                            summaryIntent.putExtra("reason", "outOfTime");
                            summaryIntent.putExtra("points", points);
                            startActivity(summaryIntent);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeValText.setText(String.format(Locale.ENGLISH, "%1$d:%2$02d", timeLeft / 60, timeLeft % 60));
                            }
                        });
                    }
                }, 0, 1000);
                break;
            default:
                throw new IllegalStateException();
        }

        // populate words list
        switch (gameMode) {
            case LEARN_MODE:
                words = wordsDictionary.GetWordsToLearn(NUMBER_OF_WORDS_TO_LEARN);
                if (words.isEmpty()) { noWords(); return; }
                summaryIntent.putExtra("learnedWords", (Serializable) words.clone());

                for (Word newWord : words) {
                    Intent newWordIntent = new Intent(GameActivity.this, NewWordActivity.class);
                    newWordIntent.putExtra("word", newWord);
                    startActivity(newWordIntent);
                }
                break;
            case TEST_MODE:
                words = wordsDictionary.GetWordsToRepeat();
                if (words.isEmpty()) { noWords(); return; }
                break;
            default:
                throw new IllegalStateException();
        }

        showNextQuestion();
    }

    private void showNextQuestion() {
        if (health <= 0) {
            timer.cancel();
            summaryIntent.putExtra("reason", "outOfHealth");
            summaryIntent.putExtra("points", points);
            startActivity(summaryIntent);
            return;
        }

        if (words.size() == 0) {
            startActivity(summaryIntent);
            return;
        }

        // TODO: frequency based on memorizedValue for testMode
        Word nextWord = words.get((int) (Math.random() * words.size()));
        List<Word> otherAnswers = wordsDictionary.GetWordsFromCategory(nextWord.getCategory(), nextWord.getId(), NUMBER_OF_ANSWERS);

        this.currentQuestion = new Question(nextWord, otherAnswers);

        questionText.setText(currentQuestion.getQuestion());
        answer1Button.setText(currentQuestion.getAnswer(0));
        answer2Button.setText(currentQuestion.getAnswer(1));
        answer3Button.setText(currentQuestion.getAnswer(2));
        answer4Button.setText(currentQuestion.getAnswer(3));
    }

    private void handleAnswer(int selectedAnswer) {
        switch (gameMode) {
            case LEARN_MODE:
                if (selectedAnswer == currentQuestion.getCorrectAnswer()) { // correct answer
                    Word selected = currentQuestion.getCurrentWord();
                    int correctAnswers = selected.getCorrectAnswers() + 1;

                    if (correctAnswers >= NEEDED_CORRECT_ANSWERS) {
                        words.remove(selected);
                        wordsDictionary.MarkAsLearned(selected.getId());
                    } else {
                        selected.setCorrectAnswers(correctAnswers);
                    }
                }
                break;
            case TEST_MODE:
                if (selectedAnswer == currentQuestion.getCorrectAnswer()) { // correct answer
                    this.points += 1;
                    this.timeLeft += SECONDS_TO_ADD_AFTER_CORRECT_ANSWER;
                    wordsDictionary.MarkCorrectAnswer(currentQuestion.getCurrentWord().getId());
                } else { // incorrect answer
                    this.health -= 1;
                    hearts.get(health).setImageResource(R.drawable.empty_heart);
                    wordsDictionary.MarkIncorrectAnswer(currentQuestion.getCurrentWord().getId());
                }
                break;
            default:
                throw new IllegalStateException();
        }

        showNextQuestion();
    }

    private void noWords() {
        timer.cancel();
        Intent intent = new Intent(GameActivity.this, NoWordsLeftActivity.class);
        startActivity(intent);
    }
}
