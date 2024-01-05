package be.ehb.starwarsinfo.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanetDAO {

    @Insert
    void insert(Planet planet);

    @Insert
    void insertAll(List<Planet> planets);

    @Query("SELECT * FROM Planet")
    LiveData<List<Planet>> getAllPlanets();

    @Query("DELETE FROM Planet")
    void nukePlanetsTable();

}
