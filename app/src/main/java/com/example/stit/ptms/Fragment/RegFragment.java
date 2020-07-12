package com.example.stit.ptms.Fragment;

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


public class RegFragment extends Fragment {
    private View v;
    private CardView btn_reg;
    private DataBase dataBase;
    private EditText username,password,email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_reg,container,false);
        btn_reg = v.findViewById(R.id.btn_reg);
        username = v.findViewById(R.id.reg_username);
        password = v.findViewById(R.id.reg_password);
        email = v.findViewById(R.id.reg_email);

        dataBase = new DataBase(getActivity());

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().matches("")){
                    toastMsg("Please Enter username");
                }else if (password.getText().toString().matches("")){
                    toastMsg("Please Enter password");
                }else if (email.getText().toString().matches("")){
                    toastMsg("Please Enter Email");
                }else{
                    if (dataBase.addUser(
                            username.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString()
                    )){
                    toastMsg("You registered successfully, Please Login");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
                    }else {
                        toastMsg("Your email account has been registered");
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toastMsg(String msg){
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }
}
