package com.example.stit.ptms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Object.QuestionsLog;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionsLog_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<QuestionsLog> data = new ArrayList<>();
    private static int TYPE_Correct = 1,TYPE_Wrong = 2;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public QuestionsLog_Adapter(Context context, List<QuestionsLog> data) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_Correct){
            v = LayoutInflater.from(context).inflate(R.layout.questionslog_correct_item,parent,false);
            return new CorrectViewHolder(v);
        }else {
            v = LayoutInflater.from(context).inflate(R.layout.questionslog_wrong_item,parent,false);
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
        private TextView ans,quest;
        public CorrectViewHolder(@NonNull View itemView) {
            super(itemView);

            quest = itemView.findViewById(R.id.quest_log_questions);
            ans = itemView.findViewById(R.id.quest_log_ans);
        }

        public void setCorrectItem(QuestionsLog data,final  OnItemClickListener listener) {
            quest.setText("Question: "+data.getQuestion());
            ans.setText("Your Answer: "+data.getAnswer());
        }
    }

    public class WrongViewHolder extends RecyclerView.ViewHolder{
        private TextView ans,quest;
        public WrongViewHolder(View itemView){
            super(itemView);
            quest = itemView.findViewById(R.id.quest_log_questions);
            ans = itemView.findViewById(R.id.quest_log_ans);
        }

        public void setWrongItem(QuestionsLog data,final OnItemClickListener listener){
            quest.setText("Question: "+data.getQuestion());
            ans.setText("Your Answer: "+data.getAnswer());
        }
    }
}
