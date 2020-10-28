package com.example.stit.ptms.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.R;

public class LoginFragment extends Fragment{
    private DataBase dataBase = new DataBase(getContext());
    private EditText password,username;
    private CardView btn_login;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        password = v.findViewById(R.id.user_login_password);
        username = v.findViewById(R.id.user_login_username);
        btn_login = v.findViewById(R.id.btn_login);

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

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    // 1 = login 2 = not activated 3 = wong PW 4 = error
    public void checklogin(final String username, final String password) {
        SQLiteDatabase db = dataBase.getReadableDatabase();

    }
}
