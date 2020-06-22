package com.example.stit.ptms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "PTMS.db";

    public DataBase(@Nullable Context context) {
        super(context, _DBName, null, _DBVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE IF NOT EXISTS QuestionsLog(" +
                "QuestionNo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "testNo INTEGER NOT NULL,"+
                "questions varchar(50)," +
                "yourAnswer varchar(5)," +
                "isCorrect INTEGER(1)"+
                ");";
        db.execSQL(SQL);

        SQL = "CREATE TABLE IF NOT EXISTS TestsLog(" +
                "testNo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "testDate DATE,"+
                "testTime TIME," +
                "duration INTEGER," +
                "correctCount INTEGER"+
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void getQuestions(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM QuestionsLog";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()){
                do {
                    Log.d("data",cursor.getString(cursor.getColumnIndex("QuestionNo")));
                }while (cursor.moveToNext());
            }
        }
    }

    public void getTests(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM TestsLog";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()){
                do {
                    Log.d("data",cursor.getString(cursor.getColumnIndex("testNo")));
                }while (cursor.moveToNext());
            }
        }
    }
}
