package com.google.android.apps.common.testing.accessibility.framework.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by raoul on 30/08/17.
 */
@Entity(tableName = "abbreviation_fr")
public class AbbreviationFr {
    @PrimaryKey(autoGenerate=true)
    private int uid;

    @ColumnInfo(name = "abbreviation")
    private String abbreviation;

    @ColumnInfo(name = "meaning")
    private String meaning;

    public AbbreviationFr(String abbreviation, String meaning) {
        this.abbreviation = abbreviation;
        this.meaning      = meaning;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}