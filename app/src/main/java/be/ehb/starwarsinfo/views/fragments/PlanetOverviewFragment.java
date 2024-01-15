package be.ehb.starwarsinfo.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import be.ehb.starwarsinfo.R;
import be.ehb.starwarsinfo.model.Planet;
import be.ehb.starwarsinfo.model.SWInfoDatabase;
import be.ehb.starwarsinfo.views.fragments.util.PlanetAdapter;

public class PlanetOverviewFragment extends Fragment {

    private SWInfoDatabase SWInfoDB;
    private RecyclerView mRecyclerView;
    private PlanetAdapter mAdapter;
    private ArrayList<Planet> items;

    public PlanetOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
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
        mAdapter = new PlanetAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Update the adapter items each time the database table is updated
        SWInfoDB.getPlanetDAO().getAllPlanets().observe(getViewLifecycleOwner(), planets -> {
            items = (ArrayList<Planet>) planets;
            mAdapter.setItems(items);
            mRecyclerView.setAdapter(mAdapter);
        });
    }

    // Search menu setup
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.overview_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Filter adapter items on text change
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.setItems(filterList(items, newText));
                mRecyclerView.setAdapter(mAdapter);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Filter planet list on search query
    private ArrayList<Planet> filterList(ArrayList<Planet> list, String filterQuery) {
        ArrayList<Planet> filteredList = new ArrayList<>();

        for (Planet planet : list) {
            if (planet.getName().toLowerCase().contains(filterQuery.toLowerCase())) {
                filteredList.add(planet);
            }
        }
        return filteredList;
    }
}