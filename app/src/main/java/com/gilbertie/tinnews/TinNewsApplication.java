package com.gilbertie.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.ashokvarma.gander.Gander;
import com.ashokvarma.gander.imdb.GanderIMDB;
import com.facebook.stetho.Stetho;
import com.gilbertie.tinnews.database.AppDatabase;

public class TinNewsApplication extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        Gander.setGanderStorage(GanderIMDB.getInstance());
        Stetho.initializeWithDefaults(this);
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tin_db").build();
    }

    // a nonstandard singleton, because TinNewsApplication will only be created once during "an app's lifecycle"
    public static AppDatabase getDatabase() {
        return database;
    }
}
