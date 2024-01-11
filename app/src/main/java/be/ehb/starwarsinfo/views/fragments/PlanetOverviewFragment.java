package be.ehb.starwarsinfo.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import be.ehb.starwarsinfo.R;
import be.ehb.starwarsinfo.model.Planet;
import be.ehb.starwarsinfo.model.SWInfoDatabase;
import be.ehb.starwarsinfo.views.fragments.util.PlanetAdapter;

public class PlanetOverviewFragment extends Fragment {

    private SWInfoDatabase SWInfoDB;
    RecyclerView mRecyclerView;

    public PlanetOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planet_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SWInfoDB = SWInfoDatabase.getInstance(getContext());

        mRecyclerView = view.findViewById(R.id.rv_planets_overview);
        PlanetAdapter mAdapter = new PlanetAdapter();

        SWInfoDB.getPlanetDAO().getAllPlanets().observe(getViewLifecycleOwner(), planets -> {
            mAdapter.setItems((ArrayList<Planet>) planets);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        });
    }
}