package com.google.android.apps.common.testing.accessibility.framework.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.google.android.apps.common.testing.accessibility.framework.database.dao.AbbreviationFrDao;
import com.google.android.apps.common.testing.accessibility.framework.database.entity.AbbreviationFr;

/**
 * Created by raoul on 30/08/17.
 */
@Database(entities = {AbbreviationFr.class}, version = 1)
public abstract class AbbreviationDatabase extends RoomDatabase {
    public abstract AbbreviationFrDao abbreviationFrDao();
}