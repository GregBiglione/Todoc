package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProject(Project project);

    @Update
    void updateProject(Project project);

    @Query("SELECT * FROM Project WHERE id = :id")
    LiveData<List<Project>> getProject(long id);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();
}