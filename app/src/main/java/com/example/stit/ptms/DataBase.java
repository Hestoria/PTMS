package com.example.stit.ptms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stit.ptms.Object.Result;

import java.util.ArrayList;
import java.util.List;

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

    public List<Result> getTestsLog(){
      List<Result> data = new ArrayList<>();
      SQLiteDatabase db = this.getReadableDatabase();
      String sql = "SELECT * FROM QuestionsLog";
        try(Cursor cursor = db.rawQuery(sql,null)){
          if (cursor.moveToFirst()){
              do {
                  Result temp = new Result();
                  temp.setAns(cursor.getString(cursor.getColumnIndex("yourAnswer")));
                  temp.setQuestion(cursor.getString(cursor.getColumnIndex("questions")));
                  if (cursor.getInt(cursor.getColumnIndex("isCorrect"))==1)
                      temp.setCorrect(true);
                  else
                      temp.setCorrect(false);
                  data.add(temp);
              }while (cursor.moveToNext());
          }
      }
      return data;
    };

    public int GetTestsID(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select testNo from TestsLog where testNo = (select max(testNo) from TestsLog)";
        Cursor cursor    = db.rawQuery(sql,null);
        if (cursor!=null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }

    public void UpdateCorrectCount(int ID,int Count){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE TestsLog SET correctCount = "+Count
                +" WHERE testNo = "+ID;
        Log.d("SQL",sql);
        db.execSQL(sql);
    }

}
