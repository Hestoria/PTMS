package com.example.stit.ptms.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Object.Result;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class Result_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<Result> data = new ArrayList<>();
    private static int TYPE_Correct = 1,TYPE_Wrong = 2;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public Result_Adapter(Context context, List<Result> data) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_Correct){
            v = LayoutInflater.from(context).inflate(R.layout.result_item_correct,parent,false);
            return new CorrectViewHolder(v);
        }else {
            v = LayoutInflater.from(context).inflate(R.layout.result_item_wrong,parent,false);
            return new WrongViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_Correct){
            ((CorrectViewHolder)holder).setCorrectItem(data.get(position),listener);
        }else
            ((WrongViewHolder)holder).setWrongItem(data.get(position),listener);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isCorrect()){
            return TYPE_Correct;
        }else
            return TYPE_Wrong;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CorrectViewHolder extends RecyclerView.ViewHolder {
        private TextView q,a;
        public CorrectViewHolder(@NonNull View itemView) {
            super(itemView);
            q = itemView.findViewById(R.id.result_question);
            a = itemView.findViewById(R.id.result_ans);
        }

        public void setCorrectItem(Result data,final  OnItemClickListener listener) {
            q.setText(data.getQuestion());
            a.setText(data.getAns());
        }
    }

    public class WrongViewHolder extends RecyclerView.ViewHolder{
        private TextView q,a;
        public WrongViewHolder(View itemView){
            super(itemView);
            q = itemView.findViewById(R.id.result_question);
            a = itemView.findViewById(R.id.result_ans);
        }

        public void setWrongItem(Result data,final OnItemClickListener listener){
            q.setText(data.getQuestion());
            a.setText(data.getAns());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.OnItemClick(postition);
                        }
                    }
                }
            });
        }
    }
}
