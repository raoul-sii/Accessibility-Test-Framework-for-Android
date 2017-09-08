package com.google.android.apps.common.testing.accessibility.framework.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.google.android.apps.common.testing.accessibility.framework.database.entity.AbbreviationFr;

import java.util.List;

/**
 * Created by raoul on 30/08/17.
 */
@Dao
public interface AbbreviationFrDao {
    @Query("SELECT * FROM abbreviation_fr")
    List<AbbreviationFr> getAll();

    @Query("SELECT * FROM abbreviation_fr WHERE uid IN (:abbreviationIds)")
    List<AbbreviationFr> loadAllByIds(int[] abbreviationIds);

    @Query("SELECT * FROM abbreviation_fr WHERE abbreviation LIKE :abbreviation LIMIT 1")
    AbbreviationFr findByAbbreviation(String abbreviation);

    @Insert
    void insertAll(AbbreviationFr... abbreviations);

    @Delete
    void delete(AbbreviationFr abbreviationFr);
}