package com.example.stit.ptms.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Adapter.Result_Adapter;
import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.Object.Result;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView.LayoutManager resultLayoutManager;
    private RecyclerView resultRecyclerView;
    private Result_Adapter resultAdapter;
    private List<Result> data = new ArrayList<>();
    private DataBase dataBase;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history,container,false);
        resultRecyclerView = v.findViewById(R.id.result_container);
        resultLayoutManager = new LinearLayoutManager(getActivity());
        resultAdapter = new Result_Adapter(getContext(),data);
        resultRecyclerView.setLayoutManager(resultLayoutManager);
        resultRecyclerView.setAdapter(resultAdapter);

        resultAdapter.setOnItemClickListener(new Result_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Toast(data.get(position).getQuestion());
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setvalue();
    }

    public void Toast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void setvalue(){
        dataBase = new DataBase(getContext());
        data = dataBase.getTestsLog();
    }
}
