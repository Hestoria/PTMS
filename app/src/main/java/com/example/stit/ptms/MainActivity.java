package com.example.stit.ptms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.stit.ptms.Adapter.Questions_Adapter;
import com.example.stit.ptms.Object.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Questions_Adapter questionsAdapter;
    private LinearLayout layoutonboarding;
    private Button btn_next,btn_prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutonboarding = findViewById(R.id.onboarding);
        btn_next = findViewById(R.id.questions_next);
        btn_prev = findViewById(R.id.questions_prev);

        setQuestions();

        final ViewPager2 ViewPager = findViewById(R.id.ViewPager);
        ViewPager.setAdapter(questionsAdapter);

        setupOnboardingicators();
        setcurrentOnboardingicators(0);

        ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setcurrentOnboardingicators(position);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ViewPager.getCurrentItem() + 1 < questionsAdapter.getItemCount()){
                    ViewPager.setCurrentItem(ViewPager.getCurrentItem() + 1);
                } else {
                    //check selection and confirm submitting and ans and return result
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

    }

    public void setQuestions(){//get json data and set up the question and ans
        List<Questions> questionsList = new ArrayList<>();

        questionsList.add(new Questions("11, 13, 17, 19, 23, 29, 31, 37, 41, ? ","34","a2","a3","a4"));
        questionsList.add(new Questions("11, 10, ?, 100, 1001, 1000, 10001","34","a2","a3","a4"));
        questionsList.add(new Questions("20, 19, 17, ?, 10, 5","34","a2","a3","a4"));
        questionsList.add(new Questions("9, 12, 11, 14, 13, ?, 15","34","a2","a3","a4"));
        questionsList.add(new Questions("4, 6, 12, 14, 28, 30, ?","34","a2","a3","a4"));
        questionsList.add(new Questions("36, 34, 30, 28, 24, ?","34","a2","a3","a4"));
        questionsList.add(new Questions("1, 4, 27, 16, ?, 36, 343","34","a2","a3","a4"));
        questionsList.add(new Questions("6, 11, 21, 36, 56, ?","34","a2","a3","a4"));
        questionsList.add(new Questions("2, 3, 5, 7, 11, ?, 17","34","a2","a3","a4"));
        questionsList.add(new Questions("2, 7, 14, 23, ?, 47","34","a2","a3","a4"));

        String url = "https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment";
        //new getjson().execute(url);

        questionsAdapter = new Questions_Adapter(questionsList);
    }

    private class getjson extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading screan
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
                    Log.i("data",line);
                }



                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e("error",""+e);
            } catch (IOException e) {
                Log.e("error",""+e);
            }finally {
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
            Log.i("data",s);
        }
    }

    private void setupOnboardingicators(){
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

    private void setcurrentOnboardingicators(int index){
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
        if (index == 0 ){
            btn_prev.setEnabled(false);
        } else {
            btn_prev.setEnabled(true);
        }
    }

}
