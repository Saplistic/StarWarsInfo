package be.ehb.starwarsinfo.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import be.ehb.starwarsinfo.R;
import be.ehb.starwarsinfo.model.Planet;

public class PlanetDetailsFragment extends Fragment {

    TextView name;
    TextView rotation_period;
    TextView orbital_period;
    TextView diameter;
    TextView climate;
    TextView gravity;
    TextView terrain;
    TextView surface_water;
    TextView population;

    public PlanetDetailsFragment() {
    }

    public static PlanetDetailsFragment newInstance() {
        PlanetDetailsFragment fragment = new PlanetDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planet_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Planet selectedPlanet = (Planet) getArguments().getSerializable("selectedPlanet");

        name = view.findViewById(R.id.tv_planet_details_title);
        population = view.findViewById(R.id.tv_planet_details_value);
        diameter = view.findViewById(R.id.tv_planet_details_value1);
        terrain = view.findViewById(R.id.tv_planet_details_value2);
        climate = view.findViewById(R.id.tv_planet_details_value3);
        surface_water = view.findViewById(R.id.tv_planet_details_value4);
        gravity = view.findViewById(R.id.tv_planet_details_value5);
        rotation_period = view.findViewById(R.id.tv_planet_details_value6);
        orbital_period = view.findViewById(R.id.tv_planet_details_value7);

        name.setText(selectedPlanet.getName());

        if (selectedPlanet.getPopulation().equals("unknown")) {
            population.setText("unknown");
        } else {
            try {
                Number number = NumberFormat.getInstance().parse(selectedPlanet.getPopulation());
                population.setText(prettyCount(number) + " inhabitants");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        diameter.setText(selectedPlanet.getDiameter());
        terrain.setText(selectedPlanet.getTerrain());
        climate.setText(selectedPlanet.getClimate());
        surface_water.setText(selectedPlanet.getSurface_water() + "%");
        gravity.setText(selectedPlanet.getGravity());
        rotation_period.setText(selectedPlanet.getRotation_period() + " hours");
        orbital_period.setText(selectedPlanet.getOrbital_period() + " days");
    }

    public String prettyCount(Number number) { // Imported code
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }
}