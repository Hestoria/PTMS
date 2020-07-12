package com.example.stit.ptms.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Adapter.QuestionsLog_Adapter;
import com.example.stit.ptms.Adapter.Questions_Adapter;
import com.example.stit.ptms.MainActivity;
import com.example.stit.ptms.Object.QuestionsLog;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    View v;
    private SharedPreferences prefs;
    private TextView welcome_txt;
    private ImageView logout_btn;
    private Button start;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);
        prefs = getActivity().getSharedPreferences("ptms_1", 0);
        welcome_txt = v.findViewById(R.id.welcome_user);
        start = v.findViewById(R.id.start_btn);
        logout_btn = v.findViewById(R.id.logout_btn);



        if (prefs.getString("userName","").matches("")){
            welcome_txt.setText("Please Login");
            start.setEnabled(false);
        }else {
            welcome_txt.setText("Welcome !"+prefs.getString("userName",""));
            start.setEnabled(true);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getString("userName","").matches("")){
                    Toast.makeText(getContext(),"Please login",Toast.LENGTH_SHORT).show();
                }else {
                    prefs.edit().putString("userName","").commit();
                    Toast.makeText(getContext(),"Logout successfully",Toast.LENGTH_SHORT).show();
                    welcome_txt.setText("");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
                }
            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
