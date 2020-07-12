package com.example.stit.ptms.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.R;

public class ResetPWFragment extends Fragment {
    View v;
    private TextView username,email;
    private CardView btn_reset;
    private DataBase database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_resetpw,container,false);
        username = v.findViewById(R.id.reset_name);
        email = v.findViewById(R.id.reset_email);
        btn_reset = v.findViewById(R.id.reset_resetPW);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().matches("")){
                    toastMsg("Please Enter username");
                }else if (email.getText().toString().matches("")){
                    toastMsg("Please Enter email");
                }else{
                    // reset user password 0 = error
                    String temppw = database.reset_password(username.getText().toString(),email.getText().toString());
                    if (temppw.equals("0")){
                        toastMsg("User not found.");
                    }else {
                        toastMsg("password reset to "+temppw);
                        Log.d("pw",temppw+"");
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DataBase(getActivity());
    }
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

}
