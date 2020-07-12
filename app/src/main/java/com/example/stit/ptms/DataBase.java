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
import java.util.Random;

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
                "userID INTEGER NOT NULL,"+
                "testDate DATE,"+
                "testTime TIME," +
                "duration INTEGER," +
                "correctCount INTEGER"+
                ");";
        db.execSQL(SQL);

        SQL = "CREATE TABLE IF NOT EXISTS User("+
                "userID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "userName varchar(30) UNIQUE,"+
                "userPassword varchar(15),"+
                "userEmail varchar(50) UNIQUE"+
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("Datbase","DB upgraded");
    }

    public List<TestsLog> getTestsLog(int id){
        List<TestsLog> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM TestsLog WHERE userID = "+id;
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
            return data;
        }catch (Exception e){
            Log.e("error",e+"");
            return data;
        }
    }

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
                    }while(cursor.moveToNext());
                }
            }
        return data;
    }

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


    public boolean addUser(String name,String email,String password){
        SQLiteDatabase db = getWritableDatabase();
        try{
            String sql = "INSERT INTO User (userName,userPassword,userEmail) VALUES('"
                    +name+"','"+
                    password+"','"+
                    email+"');";
            db.execSQL(sql);
            return true;
        }catch (Exception e){
            Log.e("Error",e+"");
            return false;
        }
    }

    public boolean login_check(String name,String password) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT userPassword FROM User WHERE userName = '"+name+"'";
        try{
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()){
                if (cursor.getString(cursor.getColumnIndex("userPassword")).equals(password)){
                    return true;
                }else
                    return false;
            }
        }catch (Exception e){
            Log.d("login",e+"");
        }
        return false;
    }

    public int getUserID(String name){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT userID FROM User WHERE userName = '"+name+"'";
        try{
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor != null && cursor.moveToFirst()){
                return cursor.getInt(cursor.getColumnIndex("userName"));
            }else
                return -1;
        }catch (Exception e){
            Log.d("get ID",e+"");
            return -1;
        }
    }

    public String reset_password(String username,String email){
        SQLiteDatabase db = getWritableDatabase();
        String pw = "";
        char[] temp = generatePswd(6);
        for (int i = 0 ; i<temp.length; i ++ ){
            pw += temp[i];
        }
        String sql = "UPDATE User SET userPassword ='"+pw+
                "' WHERE userName ='"+username+
                "' AND userEmail ='"+email+"'";
        try{
            db.execSQL(sql);
            return pw;
        }catch (Exception e){
            return "0";
        }
    }

    static char[] generatePswd(int len) {
        String charsCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";

        String passSymbols = charsCaps + chars + nums;
        Random rnd = new Random();

        char[] password = new char[len];
        for (int i = 0; i < len; i++) {
            password[i] = passSymbols.charAt(rnd.nextInt(passSymbols.length()));
        }
        return password;
    }
}
