package com.cleanup.todoc.database.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    //--------- SINGLETON --------------------------------------------------------------------------
    private static TodocDatabase instance;

    //--------- DAO --------------------------------------------------------------------------------
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    //--------- INSTANCE ---------------------------------------------------------------------------
    public static synchronized TodocDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TodocDatabase.class, "TodocDatabase.db")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //--------- CALLBACK ---------------------------------------------------------------------------
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PrePopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PrePopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private TaskDao mTaskDao;
        private ProjectDao mProjectDao;

        private PrePopulateDbAsyncTask(TodocDatabase db){
            mTaskDao = db.taskDao();
            mProjectDao = db.projectDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //--------- PROJECTS -------------------------------------------------------------------
            mProjectDao.createProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
            mProjectDao.createProject(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
            mProjectDao.createProject(new Project(3L, "Projet Circus", 0xFFA3CED2));

            //--------- TASKS ----------------------------------------------------------------------
            mTaskDao.insertTask(new Task(1, 1, "Nettoyer les vitres", 1572020));
            mTaskDao.insertTask(new Task(2, 2, "Vider le lave vaisselle", 2372020));
            mTaskDao.insertTask(new Task(3, 2 ,"Passer l'aspirateur", 2772020));
            mTaskDao.insertTask(new Task(4, 1, "Arroser les plantes", 28072020));
            mTaskDao.insertTask(new Task(5, 3,"Nettoyer les toilettes", 3172020));
            return null;
        }
    }
}
