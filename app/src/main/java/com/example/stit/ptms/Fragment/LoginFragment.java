package com.example.stit.ptms.Fragment;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.R;

public class LoginFragment extends Fragment{
    DataBase dataBase;
    private TextView btn_reg,btn_resetPW;
    private EditText password,username;
    private CardView btn_login;
    private View v;
    private SharedPreferences prefs =null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        password = v.findViewById(R.id.user_login_password);
        username = v.findViewById(R.id.user_login_username);
        btn_login = v.findViewById(R.id.btn_login);
        btn_reg = v.findViewById(R.id.btn_reg);
        btn_resetPW = v.findViewById(R.id.btn_reestPW);
        dataBase = new DataBase(getActivity());
        prefs = getActivity().getSharedPreferences("ptms_1", 0);



        btn_resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResetPWFragment()).commit();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegFragment()).commit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().matches("")){
                    toastMsg("Please Enter username");
                }else if (password.getText().toString().matches("")){
                    toastMsg("Please Enter password");
                }else{
                    checklogin(username.getText().toString(),password.getText().toString());
                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void checklogin(final String username, final String password) {
        if (dataBase.login_check(username, password)) {
            //login
            toastMsg("Welcome " + username);
            prefs.edit().putString("userName", username).commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else {
            //login fail
            toastMsg("Login fail, Please try again");
        }
    }
}
