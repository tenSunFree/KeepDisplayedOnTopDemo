package com.example.administrator.keepdisplayedontopdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView loopTextView;
    private int increasingNumbers;
    private boolean isAutomaticRecovery;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageButton btnSpeakImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("more", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAutomaticRecovery = true;
        increasingNumbers = 0;
        loopTextView = findViewById(R.id.loopTextView);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(loopTextView, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(loopTextView, 5, 100, 1, TypedValue.COMPLEX_UNIT_SP);

        constantlyUpdatedFigures();

        btnSpeakImageButton = findViewById(R.id.btnSpeakImageButton);
        btnSpeakImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
    }

    private void constantlyUpdatedFigures() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                loopTextView.setText("" + increasingNumbers);
                increasingNumbers++;
                constantlyUpdatedFigures();
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        Log.v("more", "onPause()");
        if (isAutomaticRecovery == true) {
            Intent resumeIntent = new Intent(this, MainActivity.class);
            startActivity(resumeIntent);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.v("more", "onResume()");
        super.onResume();
        isAutomaticRecovery = true;
    }

    @Override
    protected void onDestroy() {
        Log.v("more", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.v("more", "onKeyDown()");
            isAutomaticRecovery = false;
            finish();
            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);                    // 通过Intent传递语音识别的模式, 开启语音
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,                                 // 语言模式和自由模式的语音识别
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "注意!! 觸發onPause()");            // 提示语音开始
        try {
            isAutomaticRecovery = false;
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);                                  // 开始语音识别
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "orry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }
}