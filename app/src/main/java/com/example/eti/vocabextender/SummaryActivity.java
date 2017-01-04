package com.example.eti.vocabextender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.eti.vocabextender.model.Word;

import java.util.ArrayList;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {

    private StringBuilder summary = new StringBuilder();

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SummaryActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        TextView summaryText = (TextView) findViewById(R.id.summaryText);

        View mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Intent myIntent = getIntent();
        switch (myIntent.getStringExtra("mode")) {
            case "learn":
                summary.append("Congratulations!\nYou have learned the following words:\n");
                ArrayList<Word> words = (ArrayList<Word>) myIntent.getSerializableExtra("learnedWords");
                if (words == null) words = new ArrayList<>();

                for (Word word : words) {
                    summary.append(String.format(Locale.ENGLISH, "- %s\n", word.getWord()));
                }

                break;
            case "test":
                if (myIntent.getStringExtra("reason").equals("outOfHealth"))
                    summary.append("You run out of health!\n");
                else if (myIntent.getStringExtra("reason").equals("outOfTime"))
                    summary.append("You run out of time!\n");

                summary.append(String.format(Locale.ENGLISH, "You have earned %d points.\n", myIntent.getIntExtra("points", 0)));
                break;
            default:
                throw new IllegalStateException();
        }

        summaryText.setText(summary.toString());
    }
}
