package be.ehb.starwarsinfo.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import be.ehb.starwarsinfo.R;
import be.ehb.starwarsinfo.model.Planet;
import be.ehb.starwarsinfo.model.SWInfoDatabase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String API_URL = "https://swapi.dev/api/planets";
    SWInfoDatabase SWInfoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread backgroundThread = new Thread(() ->
        {
            try {
                SWInfoDB = SWInfoDatabase.getInstance(getApplicationContext());
                SWInfoDB.getPlanetDAO().nukePlanetsTable();

                OkHttpClient mClient = new OkHttpClient();

                int currentPage = 1;
                boolean lastPageReached = false;

                while (!lastPageReached) {
                    Request mRequest = new Request.Builder()
                            .url(API_URL + "/?page=" + currentPage)
                            .get()
                            .build();
                    Response mResponse = mClient.newCall(mRequest).execute();
                    String responseText = mResponse.body().string();
                    JSONObject ResultJSONObject = new JSONObject(responseText);
                    JSONArray planetsJSON = new JSONArray(ResultJSONObject.getString("results"));

                    //Log.d("LOG", responseText);

                    if (ResultJSONObject.isNull("next")) {
                        lastPageReached = true;
                    }

                    int length = planetsJSON.length();
                    ArrayList<Planet> retrievedPlanets = new ArrayList<>(length);

                    for (int i = 0; i < length; i++) {
                        JSONObject tempPlanet = planetsJSON.getJSONObject(i);

                        Planet planet = new Planet(
                            tempPlanet.getString("name"),
                            tempPlanet.getString("rotation_period"),
                            tempPlanet.getString("orbital_period"),
                            tempPlanet.getString("diameter"),
                            tempPlanet.getString("climate"),
                            tempPlanet.getString("gravity"),
                            tempPlanet.getString("terrain"),
                            tempPlanet.getString("surface_water"),
                            tempPlanet.getString("population")
                        );
                        retrievedPlanets.add(planet);
                    }
                    SWInfoDB.getPlanetDAO().insertAll(retrievedPlanets);

                    currentPage++;
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });

        backgroundThread.start();
    }
}