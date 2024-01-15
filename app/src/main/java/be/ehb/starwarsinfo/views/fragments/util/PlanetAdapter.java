package be.ehb.starwarsinfo.views.fragments.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import be.ehb.starwarsinfo.R;
import be.ehb.starwarsinfo.model.Planet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

    private ArrayList<Planet> items;

    public PlanetAdapter() {
        this.items = new ArrayList<>();
    }

    public void setItems(ArrayList<Planet> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PlanetAdapter.PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.planet_overview_item, parent, false);
        return new PlanetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetAdapter.PlanetViewHolder holder, int position) {
        Planet planet = items.get(position);
        holder.tvName.setText(planet.getName());
        holder.tvDiameter.setText(planet.getDiameter() + " km");

        Glide.with(holder.itemView)
                .load(planet.getImage_url())
                .into(holder.ivPlanet);

        if (planet.getPopulation().equals("unknown")) {
            holder.tvPopulation.setText("unknown");
        } else {
            try {
                Number number = NumberFormat.getInstance().parse(planet.getPopulation());
                holder.tvPopulation.setText(prettyCount(number) + " inhabitants");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivPlanet;
        TextView tvDiameter;
        TextView tvPopulation;
        RelativeLayout row;

        public PlanetViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.planet_item);
            tvName = itemView.findViewById(R.id.tv_planet_name);
            ivPlanet = itemView.findViewById(R.id.iv_planet_cover);
            tvDiameter = itemView.findViewById(R.id.tv_planet_diameter);
            tvPopulation = itemView.findViewById(R.id.tv_planet_population);

            row.setOnClickListener((View v) -> {
                Planet selectedPlanet = items.get(getAdapterPosition());
                Bundle data = new Bundle();
                data.putSerializable("selectedPlanet", selectedPlanet);

                Navigation.findNavController(itemView).navigate(R.id.action_planetOverviewFragment_to_planetDetailsFragment, data);
            });
        }
    }
}
