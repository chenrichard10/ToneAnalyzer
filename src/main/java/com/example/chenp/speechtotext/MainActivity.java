package com.example.chenp.speechtotext;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneChatOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.UtteranceAnalyses;
import com.ibm.watson.developer_cloud.util.GsonSingleton;
import com.ibm.watson.developer_cloud.util.RequestUtils;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        IamOptions options = new IamOptions.Builder()
                .apiKey("ugwAYpcq-zamqkBDNdekkhQ5_m9fuv4E67hZPzXMbYf5")
                .build();
        final ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", options);
        toneAnalyzer.setEndPoint("https://gateway-wdc.watsonplatform.net/tone-analyzer/api");
        Button analyzeButton = (Button) findViewById(R.id.analyze_button);
        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userInput = (EditText) findViewById(R.id.user_input);
                String textToAnalyze = userInput.getText().toString();
                ToneOptions options = new ToneOptions.Builder()
                        .text(textToAnalyze)
                        .build();
                ToneAnalysis analysis = toneAnalyzer.tone(textToAnalyze, options).execute();
               List<ToneScore> scores = analysis.getDocumentTone()
                        .getTones();
                String detectedTones = "";
                for (ToneScore score : scores) {
                    if (score.getScore() > 0.5f) {
                        detectedTones += score.getToneName() + " ";
                    }

                }
                final String toastMessage=
                        "The following emotions were detected:\n\n"
                                + detectedTones.toUpperCase();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),
                                toastMessage, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}





