package com.example.eti.vocabextender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        View learnButton = findViewById(R.id.learnMode);
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent learnIntent = new Intent(MainMenuActivity.this, GameActivity.class);
                learnIntent.putExtra("mode", "learn");
                startActivity(learnIntent);
            }
        });

        View testButton = findViewById(R.id.testMode);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent learnIntent = new Intent(MainMenuActivity.this, GameActivity.class);
                learnIntent.putExtra("mode", "test");
                startActivity(learnIntent);
            }
        });
    }
}
