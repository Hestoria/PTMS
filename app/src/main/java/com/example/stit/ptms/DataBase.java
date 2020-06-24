package com.example.stit.ptms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stit.ptms.Object.QuestionsLog;
import com.example.stit.ptms.Object.TestsLog;

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

    public List<TestsLog> getTestsLog(){
        List<TestsLog> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM TestsLog";
        try(Cursor cursor = db.rawQuery(sql,null)){
            if (cursor.moveToFirst()){
                do {
                    data.add(new TestsLog(
                            cursor.getString(cursor.getColumnIndex("testDate")),
                            cursor.getString(cursor.getColumnIndex("testTime")),
                            cursor.getString(cursor.getColumnIndex("duration")),
                            cursor.getInt(cursor.getColumnIndex("correctCount")),
                            cursor.getInt(cursor.getColumnIndex("testNo"))
                    ));
                }while (cursor.moveToNext());
            }
        }
        return data;
    };

    public List<QuestionsLog> getQuestionsLog(int id){
      List<QuestionsLog> data = new ArrayList<>();
      SQLiteDatabase db = this.getReadableDatabase();
      String sql = "SELECT * FROM QuestionsLog WHERE testNo ="+id;
        try(Cursor cursor = db.rawQuery(sql,null)){
          if (cursor.moveToFirst()){
              do {
                  QuestionsLog temp = new QuestionsLog();
                  temp.setAnswer(cursor.getString(cursor.getColumnIndex("yourAnswer")));
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

    public int GetCorrectCount(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(isCorrect) as CorrectCount from QuestionsLog WHere isCorrect = 1 ";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!= null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }

    public int GetWrongCount(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(isCorrect) as WrongCount from QuestionsLog WHERE isCorrect = 0 ";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!= null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }

    public int GetCorrect(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(isCorrect) as CorrectCount from QuestionsLog WHere isCorrect = 1 AND testNo = "+id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!= null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }

    public int GetWrong(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(isCorrect) as WrongCount from QuestionsLog WHERE isCorrect = 0 AND testNo = "+id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor!= null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }
}
