package com.example.stit.ptms.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Object.Welcome;
import com.example.stit.ptms.R;

import java.util.List;

public class Welcome_Adapter extends RecyclerView.Adapter<Welcome_Adapter.OnboardingViewHolder> {

    private List<Welcome> data;

    public Welcome_Adapter(List<Welcome> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.welcome_page_item,parent,false
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
        private ImageView welcome_img;
        private TextView welcome_title,welcome_desc;
        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);

            welcome_img = itemView.findViewById(R.id.welcome_img);
            welcome_title = itemView.findViewById(R.id.welcome_title);
            welcome_desc = itemView.findViewById(R.id.welcome_desc);
        }

        void setOnboardingData(Welcome data, int position) {
            welcome_img.setImageResource(data.getImg());
            welcome_title.setText(data.getTitle());
            welcome_desc.setText(data.getDesc());
        }
    }
}
