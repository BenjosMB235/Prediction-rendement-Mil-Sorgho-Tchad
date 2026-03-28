package com.tchad.yieldpredictor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PredictionDao {

    @Insert
    void insert(Prediction prediction);

    @Query("SELECT * FROM predictions ORDER BY id DESC")
    LiveData<List<Prediction>> getAllPredictions();

    @Query("DELETE FROM predictions")
    void deleteAll();
}