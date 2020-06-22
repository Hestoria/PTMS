package com.example.stit.ptms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {
    private SharedPreferences prefs =null;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("ptms_1",0);
        if (prefs.getBoolean("first",true)) {
            setContentView(R.layout.welcome_page);
            prefs.edit().putBoolean("first",false).commit();
            //welcome page set up

        }else {
            setContentView(R.layout.activity_home);
            //set up home page
            dataBase = new DataBase(this);
            SQLiteDatabase db = dataBase.getWritableDatabase();
            ContentValues values= new ContentValues();
            values.put("testNo",1);
            values.put("questions","quesitons");
            values.put("yourAnswer","50000");
            values.put("isCorrect",1);
            db.insert("QuestionsLog",null,values);


            dataBase.getQuestions();
            startActivity(new Intent(this,MainActivity.class));
        }


    }
}