package be.ehb.starwarsinfo.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private final String API_URL = "https://swapi.dev/api/planets";
    SWInfoDatabase SWInfoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isNetworkAvailable()){
            backgroundThread.start();
        } else {
            Toast.makeText(getApplicationContext(),"No data was updated, no internet connection found", Toast.LENGTH_LONG).show();
        }

        Toolbar mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

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

            runOnUiThread(() -> Toast.makeText(getApplicationContext(),"Succesfully updated data", Toast.LENGTH_LONG).show());

        } catch (IOException e) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (JSONException e) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    });

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}