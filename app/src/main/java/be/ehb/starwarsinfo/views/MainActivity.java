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
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> customPlanetImageUrls = new HashMap<String, String>() {{
            put("Tatooine", "https://static.wikia.nocookie.net/starwars/images/7/7f/Tatooine.jpg/revision/latest?cb=20070118145039&path-prefix=nl");
            put("Stewjon", "?");
            put("Corellia", "https://static.wikia.nocookie.net/starwars/images/d/d7/Corellia-SWCT.png/revision/latest?cb=20231206040152");
            put("Rodia", "https://static.wikia.nocookie.net/starwars/images/c/c3/Rodia_canon.png/revision/latest?cb=20150817013106");
            put("Nal Hutta", "https://static.wikia.nocookie.net/starwars/images/0/0d/NalHutta-HFZ.png/revision/latest?cb=20130517035000");
            put("Dantooine", "https://static.wikia.nocookie.net/starwars/images/a/a5/Dantooine_Resistance.jpg/revision/latest?cb=20200120190043");
            put("Bestine IV", "https://static.wikia.nocookie.net/starwars/images/f/f8/Bestine-BH28.jpg/revision/latest?cb=20221221163346");
            put("Ord Mantell", "https://static.wikia.nocookie.net/starwars/images/3/36/Ord_Mantell_EotECR.png/revision/latest?cb=20170222012958");
            put("Trandosha", "https://static.wikia.nocookie.net/starwars/images/4/40/Trandosha-PL.png/revision/latest?cb=20130530013144");
            put("Socorro", "?");
            put("Mon Cala", "https://static.wikia.nocookie.net/starwars/images/0/04/Dac-AORCR.png/revision/latest?cb=20170222012252");
            put("Chandrila", "https://static.wikia.nocookie.net/starwars/images/0/0c/Chandrila-AoRCR.png/revision/latest?cb=20170222012047");
            put("Sullust", "https://static.wikia.nocookie.net/starwars/images/7/72/Sullust_DICE.png/revision/latest?cb=20151106041909");
            put("Toydaria", "https://static.wikia.nocookie.net/starwars/images/d/d6/Toydaria-TCW.png/revision/latest?cb=20130506235119");
            put("Malastare", "https://static.wikia.nocookie.net/starwars/images/0/00/Malastare_TEA.jpg/revision/latest?cb=20200830111705");
            put("Dathomir", "https://static.wikia.nocookie.net/starwars/images/3/34/DathomirJFO.jpg/revision/latest?cb=20200222032237");
            put("Ryloth", "https://static.wikia.nocookie.net/starwars/images/9/96/Ryloth-Homecoming.png/revision/latest?cb=20200517155255");
            put("Aleen Minor", "https://static.wikia.nocookie.net/starwars/images/f/f6/Aleen_NEGAS.jpg/revision/latest?cb=20070630172856"); //legends
            put("Vulpter", "https://static.wikia.nocookie.net/starwars/images/b/be/Vulpter_FF7.jpg/revision/latest?cb=20070628190912"); //legends
            put("Troiken", "https://static.wikia.nocookie.net/starwars/images/c/ce/Troiken.jpg/revision/latest?cb=20190503110140");
            put("Tund", "https://static.wikia.nocookie.net/starwars/images/3/31/Tund_TEA.jpg/revision/latest?cb=20200908113330"); //legends
            put("Haruun Kal", "https://static.wikia.nocookie.net/starwars/images/7/75/HaruunKalCSWE.jpg/revision/latest?cb=20120821183509"); //legends
            put("Cerea", "https://static.wikia.nocookie.net/starwars/images/c/cc/Cerea-FDCR.png/revision/latest?cb=20180501023912"); //legends
            put("Glee Anselm", "https://static.wikia.nocookie.net/starwars/images/0/0c/GleeAnselm.jpg/revision/latest?cb=20201018023343");
            put("Iridonia", "https://static.wikia.nocookie.net/starwars/images/c/c5/Iridonia.jpg/revision/latest?cb=20061118121317"); //legends
            put("Tholoth", "?");
            put("Iktotch", "https://static.wikia.nocookie.net/starwars/images/f/f1/Iktotch_FDNP.png/revision/latest?cb=20180501025533"); //legends
            put("Quermia", "https://static.wikia.nocookie.net/starwars/images/2/29/Quermia_NEGAS.jpg/revision/latest?cb=20070701083603"); //legends
            put("Dorin", "https://starwars.fandom.com/wiki/Dorin/Legends?file=Dorin-FDCR.png"); //legends
            put("Champala", "https://starwars.fandom.com/wiki/Champala/Legends?file=Champala_NEGAS.jpg"); //legends
            put("Mirial", "https://static.wikia.nocookie.net/star-wars-pathfinder/images/e/e0/Mirial.jpg/revision/latest?cb=20170612022003");
            put("Serenno", "https://static.wikia.nocookie.net/starwars/images/b/b2/Serenno-Massacre.png/revision/latest?cb=20130607043719");
            put("Concord Dawn", "https://static.wikia.nocookie.net/starwars/images/8/84/Concord_Dawn_system.png/revision/latest?cb=20160128214904");
            put("Zolan", "https://static.wikia.nocookie.net/starwars/images/6/66/Zolan.jpg/revision/latest?cb=20070701111500"); //legends
            put("Ojom", "https://static.wikia.nocookie.net/starwars/images/9/9f/Ojom.jpg/revision/latest?cb=20061119201000"); //legends
            put("Skako", "https://starwars.fandom.com/wiki/Skako/Legends?file=Skako.jpg"); //legends
            put("Muunilinst", "https://static.wikia.nocookie.net/starwars/images/1/19/Muunilinst.jpg/revision/latest?cb=20071221131608"); //legends
            put("Shili", "https://static.wikia.nocookie.net/starwars/images/b/b8/ShiliNEGAS.jpg/revision/latest?cb=20061126102716"); //legends
            put("Kalee", "https://static.wikia.nocookie.net/starwars/images/c/c1/Kalee_TEA.jpg/revision/latest?cb=20200828221630"); //legends
            put("Umbara", "https://static.wikia.nocookie.net/starwars/images/8/82/Umbara_TVE.png/revision/latest?cb=20211110071751");
        }};

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
                    planet.setId((((currentPage-1) * 10) + i) + 1);

                    if (customPlanetImageUrls.containsKey(planet.getName())) {
                        planet.setImage_url(customPlanetImageUrls.get(planet.getName()));
                    } else {
                        planet.setImage_url("https://starwars-visualguide.com/assets/img/planets/" + planet.getId() + ".jpg");
                    }

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