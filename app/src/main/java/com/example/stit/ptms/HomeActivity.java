package com.example.stit.ptms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.stit.ptms.Adapter.Welcome_Adapter;
import com.example.stit.ptms.Fragment.HistoryFragment;
import com.example.stit.ptms.Fragment.HomeFragment;
import com.example.stit.ptms.Object.Welcome;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private SharedPreferences prefs =null;
    private Welcome_Adapter welcomeAdapter;
    private LinearLayout layoutonboarding;
    private List<Welcome> welcomeList = new ArrayList<>();
    private Button welcome_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("ptms_1",0);
        if (prefs.getBoolean("first",true)) {
            setContentView(R.layout.welcome_page);
            prefs.edit().putBoolean("first",false).commit();
            //welcome page set up

            welcome_btn = findViewById(R.id.welcome_btn);
            final ViewPager2 welcome_viewpage = findViewById(R.id.welcome_ViewPager);
            layoutonboarding = findViewById(R.id.welcome_onboarding);

            welcome_viewpage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setcurrentOnboardingiactors(position);
                }
            });

            welcome_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (welcome_viewpage.getCurrentItem() + 1 < welcomeAdapter.getItemCount()){
                        welcome_viewpage.setCurrentItem(welcome_viewpage.getCurrentItem() + 1);
                    } else {
                        // go main
                        recreate();
                    }
                }
            });

            setWelcomePageValue();

            // build
            welcomeAdapter = new Welcome_Adapter(welcomeList);
            welcome_viewpage.setAdapter(welcomeAdapter);

            setupOnboardingactors();
            setcurrentOnboardingiactors(0);

        }else {
            setContentView(R.layout.activity_home);
            //set up home page
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
            bottomNavigationView.bringToFront();
            bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    public void setWelcomePageValue(){
        for (int i = 0 ; i < 3 ; i++) {
            welcomeList.add(new Welcome(R.drawable.ic_launcher_foreground, "title"+i, "desc"));
        }
    }


    public void setupOnboardingactors(){
        ImageView[] indicators = new ImageView[welcomeAdapter.getItemCount()];
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

    public void setcurrentOnboardingiactors(int index){
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
        if(index == welcomeAdapter.getItemCount() -1 ){
            welcome_btn.setText("Start");
        } else {
            welcome_btn.setText("Next");
        }
    }
}