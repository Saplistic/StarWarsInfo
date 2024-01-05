package be.ehb.starwarsinfo.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {Planet.class})
public abstract class SWInfoDatabase extends RoomDatabase {

    private static SWInfoDatabase instance;

    public static SWInfoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, SWInfoDatabase.class, "SWInfo.db").build();
        }
        return instance;
    }

    public abstract PlanetDAO getPlanetDAO();
}
