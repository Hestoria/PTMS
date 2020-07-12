package com.example.stit.ptms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.stit.ptms.Adapter.Questions_Adapter;
import com.example.stit.ptms.Object.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Questions_Adapter questionsAdapter;
    private LinearLayout layoutonboarding;
    private Button btn_next,btn_prev;
    private List<Questions> questionsList = new ArrayList<>();
    private List<Integer> selected_ans = new ArrayList<>();
    private Chronometer timer;
    private long countPauseSet;
    private boolean counting;
    private ImageView pause_resume_btn,back_home_btn;
    private DataBase dataBase = new DataBase(this);
    private LoadingDiaLog loadingDiaLog;
    private ViewPager2 ViewPager;
    private RelativeLayout pauseScreen;
    private SharedPreferences prefs =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("ptms_1",0);
        timer = findViewById(R.id.timer);
        layoutonboarding = findViewById(R.id.onboarding);
        btn_next = findViewById(R.id.questions_next);
        btn_prev = findViewById(R.id.questions_prev);
        pause_resume_btn = findViewById(R.id.pause_resume_btn);
        back_home_btn = findViewById(R.id.back_home_btn);
        pauseScreen = findViewById(R.id.pause_screen);

        // default values
        for (int i = 0 ;i < 5 ; i ++) {
            selected_ans.add(-1);
        }
        final String Date =  new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String Time =  new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        setQuestions();

        ViewPager = findViewById(R.id.ViewPager);
        ViewPager.setAdapter(questionsAdapter);
        ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                setcurrentOnboardingiactors(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                RadioGroup answers = findViewById(R.id.questions_ans);

                answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        RadioButton ans = findViewById(id);
                        setans(ViewPager.getCurrentItem(),Integer.parseInt(ans.getText().toString()));
                    }
                });
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ViewPager.getCurrentItem() + 1 < questionsAdapter.getItemCount()){
                    ViewPager.setCurrentItem(ViewPager.getCurrentItem() + 1);
                } else {
                    //check selection , confirm submitting the ans and return result
                    if (!selected_ans.contains(-1)){
                        SubbmitAns(Date,Time);
                    }else {
                        ViewPager.setCurrentItem(selected_ans.indexOf(-1));
                        Toast.makeText(MainActivity.this, "Please finish all the questions.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ViewPager.getCurrentItem() > 0){
                    ViewPager.setCurrentItem(ViewPager.getCurrentItem() - 1);
                }
            }
        });

        //back to home page button
        back_home_btn.bringToFront();
        back_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to home
                backToHome();
            }
        });

        //set up pause and resume button
        pause_resume_btn.setTag(R.drawable.ic_pause);
        pause_resume_btn.bringToFront();
        pause_resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)pause_resume_btn.getTag() == R.drawable.ic_pause){
                    pause();
                }else if ((int)pause_resume_btn.getTag() == R.drawable.ic_play){
                    resume();
                }
            }
        });
    }

    // pause the game
    public void pause(){
        if (counting){
            timer.stop();
            countPauseSet = SystemClock.elapsedRealtime() - timer.getBase();
            counting = false;
            pause_resume_btn.setImageResource(R.drawable.ic_play);
            pause_resume_btn.setTag(R.drawable.ic_play);
            ViewPager.setVisibility(View.GONE);
            //pause screen show
            pauseScreen.setVisibility(View.VISIBLE);
        }
    }
    // resume the game
    public void resume(){
        if (!counting){
            timer.setBase(SystemClock.elapsedRealtime() - countPauseSet);
            timer.start();
            counting = true;
            pause_resume_btn.setImageResource(R.drawable.ic_pause);
            pause_resume_btn.setTag(R.drawable.ic_pause);
            ViewPager.setVisibility(View.VISIBLE);
            //pause screen dismiss
            pauseScreen.setVisibility(View.GONE);
        }
    }
    public void SubbmitAns(String Date,String Time){
        int correct_count = 0;
        SQLiteDatabase db = dataBase.getWritableDatabase();
        ContentValues values = new ContentValues();

        // stop counting
        if (counting){
            timer.stop();
            countPauseSet = SystemClock.elapsedRealtime() - timer.getBase();
            counting = false;
        }
        // insert new TestsLog
        values.put("testDate",Date);
        values.put("UserID",dataBase.getUserID(prefs.getString("userName","")));
        values.put("testTime",Time);
        values.put("duration",(int)Math.floor(countPauseSet/1000));
        values.put("correctCount",-1);
        db.insert("TestsLog",null,values);
        values.clear();

        // get test ID
        int testsID = dataBase.GetTestsID();
        // insert for each question
        for (int i = 0 ; i< questionsList.size() ; i++){
            values.put("testNo",testsID);
            values.put("questions",questionsList.get(i).getQuestion());
            values.put("yourAnswer",selected_ans.get(i));
            if(Integer.parseInt(questionsList.get(i).getCorrect_ans()) == selected_ans.get(i)){
                //check answer correct
                correct_count++;
                // make the insert value of isCorrect true
                values.put("isCorrect",1);
            }
            else {
                //the wrong answer
                // make the insert value of isCorrect false
                values.put("isCorrect", 0);
            }
            db.insert("QuestionsLog",null,values);
            values.clear();
        }
        // update correct count from TestsLog
        dataBase.UpdateCorrectCount(testsID,correct_count);
        // back to home
        backToHome();
    }

    //input ans into array
    public void setans(int page,int ans){
        selected_ans.set(page,ans);
    }

    //get json data and set up the question and ans
    public void setQuestions(){
        String url = "https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment";
        new getjson().execute(url);
        questionsAdapter = new Questions_Adapter(questionsList);
    }

    //get question
    private class getjson extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading screan
            loadingDiaLog = new LoadingDiaLog(MainActivity.this);
            loadingDiaLog.setCancelable(false);
            loadingDiaLog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line="";
                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                JSONObject jo = new JSONObject(buffer.toString());
                JSONArray ja = jo.getJSONArray("questions");

                //selection of 10 questions
                List<Integer> question_select = new ArrayList<Integer>();
                for (int i = 0 ;i < ja.length();i++){
                    question_select.add(i);
                }
                Collections.shuffle(question_select);

                for (int i = 0 ; i < 5 ; i ++){
                    //get the real ans
                    int ans_temp = Integer.parseInt(
                            ja.getJSONObject(question_select.get(i))
                                    .getString("answer"));
                    //log out the selected question and the correct answer
                    Log.d("select q","question number : "+question_select.get(i)+
                            "\n ans : "+ ja.getJSONObject(question_select.get(i))
                            .getString("answer"));

                    //generate answer
                    List<Integer> ans = new ArrayList<>() ,temp = new ArrayList<>();
                    for (int k = (ans_temp - 10);k < (ans_temp + 10);k++){
                        if (k == ans_temp)
                            continue;
                        else
                            temp.add(k);
                    }
                    Collections.shuffle(temp);
                    //add correct answer ans generated answer to list
                    ans.add(ans_temp);
                    for (int k = 0; k < 3 ; k++){
                        ans.add(temp.get(k));
                    }
                    Collections.shuffle(ans);
                    // add question and answers to viewpager
                    questionsList.add(new Questions(
                            ja.getJSONObject(question_select.get(i)).getString("question"),
                            ""+ans.get(0),
                            ""+ans.get(1),
                            ""+ans.get(2),
                            ""+ans.get(3),
                            ans_temp+""
                    ));
                }
                //for log check
                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e("error",""+e);
            } catch (IOException e) {
                Log.e("error",""+e);
            } catch (JSONException e) {
                Log.e("error",""+e);
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e("error",""+e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //update view
            questionsAdapter.notifyDataSetChanged();
            //update on boarding view
            setupOnboardingactors();
            setcurrentOnboardingiactors(0);
            //loading off
            if (loadingDiaLog.isShowing()){
                loadingDiaLog.dismiss();
                //create alert dialog
                showDialog();
            }
        }
    }

    //alert dialog for user ready to answer and start to  count up the time
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_start, null);
        Button start_btn = view.findViewById(R.id.start_btn);
        builder.setView(view);
        final AlertDialog start_dialog = builder.create();
        start_dialog.setCancelable(false);
        start_dialog.show();
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_dialog.dismiss();
                if (!counting){
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    counting = true;
                }
            }
        });
    }

    //set up on boarding actors
    private void setupOnboardingactors(){
        ImageView[] indicators = new ImageView[questionsAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0 ; i <indicators.length ; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutonboarding.addView(indicators[i]);
        }
    }

    //update on boarding actors
    private void setcurrentOnboardingiactors(int index){
        int childcount = layoutonboarding.getChildCount();
        for (int i = 0; i < childcount; i++){
            ImageView imageView = (ImageView)layoutonboarding.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactive)
                );
            }
        }
        if(index == questionsAdapter.getItemCount() -1 ){
            btn_next.setText("Submit");
        } else {
            btn_next.setText("Next");
        }
        if (index == 0){
            btn_prev.setEnabled(false);
            btn_prev.setVisibility(View.GONE);
        } else {
            btn_prev.setVisibility(View.VISIBLE);
            btn_prev.setEnabled(true);
            btn_prev.setText("Prev");
        }
    }

    public void backToHome(){
        startActivity(new Intent(this,HomeActivity.class));
    }

}
