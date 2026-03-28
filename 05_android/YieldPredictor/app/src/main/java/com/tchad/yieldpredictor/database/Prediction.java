package com.tchad.yieldpredictor.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "predictions")
public class Prediction {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String region;
    public float eviMax;
    public float eviMean;
    public float pluie;
    public float rendementPredit;
    public String date;
    public String evaluation; // "BONNE", "MOYENNE", "FAIBLE"
}