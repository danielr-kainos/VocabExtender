package com.example.eti.vocabextender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.eti.vocabextender.model.Word;

public class NewWordActivity extends AppCompatActivity {

    private StringBuilder newWord = new StringBuilder();

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent myIntent = getIntent();
        Word word = (Word) myIntent.getSerializableExtra("word");

        newWord.append(word.getWord());
        newWord.append("\n");
        newWord.append(word.getCategory());
        newWord.append("\n\n");
        newWord.append(word.getDefinition());
        newWord.append("\n\n");

        ((TextView) findViewById(R.id.newWordText)).setText(newWord.toString());
    }
}
