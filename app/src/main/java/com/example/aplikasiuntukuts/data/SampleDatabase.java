
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.aplikasiuntukuts.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database.
 */
@Database(entities = {Cheese.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Cheese table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract CheeseDao cheese();
    private static volatile SampleDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool (NUMBER_OF_THREADS);

    /** The only instance */
    private static SampleDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized SampleDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), SampleDatabase.class, "ex")
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    public static SampleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SampleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SampleDatabase.class, "Cheese_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                SampleDatabase.class).build();
    }

    private void populateInitialData() {
        if (cheese().count() == 0) {
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Cheese cheese = new Cheese();
                    for (int i = 0; i < Cheese.CHEESES.length; i++) {
                        cheese.name = Cheese.CHEESES[i];
                        cheese().insert(cheese);
                    }
                }
            });
        }
    }

}
