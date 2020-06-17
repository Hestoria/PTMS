package com.example.stit.ptms.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Object.Questions;
import com.example.stit.ptms.R;

import java.util.List;

public class Questions_Adapter extends RecyclerView.Adapter<Questions_Adapter.OnboardingViewHolder> {
    private static List<Questions> data;

    public Questions_Adapter(List<Questions> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.questions_item,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private RadioGroup answers;
        private RadioButton ans1,ans2,ans3,ans4;
        private TextView question,question_num;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            question_num = itemView.findViewById(R.id.questions_num);
            answers = itemView.findViewById(R.id.questions_ans);
            ans1 = itemView.findViewById(R.id.ans1);
            ans2 = itemView.findViewById(R.id.ans2);
            ans3 = itemView.findViewById(R.id.ans3);
            ans4 = itemView.findViewById(R.id.ans4);
        }

        void setOnboardingData(Questions data,int position){
            question.setText(data.getQuestion());
            question_num.setText("Question : "+(position+1));
            ans1.setText(data.getAns1());
            ans2.setText(data.getAns2());
            ans3.setText(data.getAns3());
            ans4.setText(data.getAns4());
        }
    }
}
