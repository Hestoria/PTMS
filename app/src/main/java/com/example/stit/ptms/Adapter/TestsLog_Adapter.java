package com.example.stit.ptms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Object.TestsLog;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class TestsLog_Adapter extends RecyclerView.Adapter<TestsLog_Adapter.OnboardingViewHolder> {
    List<TestsLog> data = new ArrayList<>();
    Context context;
    private OnItemClickListener listener;

    public TestsLog_Adapter(Context context, List<TestsLog> data) {
        this.data = data;
        this.context = context;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.testslog_item,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingitem(data.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder {
        TextView date,time,duration,correct;
        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.TestsLog_Date);
            time = itemView.findViewById(R.id.TestsLog_Time);
            duration = itemView.findViewById(R.id.TestsLog_duration);
            correct = itemView.findViewById(R.id.TestsLog_Corrects);

        }

        public void setOnboardingitem(TestsLog data, final OnItemClickListener listener) {
            date.setText("Test Date:"+data.getDate());
            time.setText("Test Time:"+data.getTime());
            duration.setText("Duration:"+data.getDuration()+"(Sec)");
            correct.setText("Score: "+data.getCorrect()+"/5");

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
