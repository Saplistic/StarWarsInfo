package be.ehb.starwarsinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tv_result);

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mClient = new OkHttpClient();
                    Request mRequest = new Request.Builder()
                            .url("https://swapi.dev/api/planets/1/")
                            .get()
                            .build();
                    Response mResponse = mClient.newCall(mRequest).execute();
                    String responseText = mResponse.body().string();

                    tvResult.setText(responseText);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        backgroundThread.start();
    }
}