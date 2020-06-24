package com.example.stit.ptms.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.CircularGauge;
import com.anychart.charts.Pie;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.LegendLayout;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;
import com.example.stit.ptms.Adapter.QuestionsLog_Adapter;
import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.Object.QuestionsLog;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class QuestionsLogFragment extends Fragment {
    View v;
    private RecyclerView quest_recyclerView;
    private RecyclerView.LayoutManager quest_layoutManager;
    private QuestionsLog_Adapter questionsLogAdapter;
    private List<QuestionsLog> data = new ArrayList<>();
    private TextView dur;
    private DataBase dataBase;
    private ImageView back_btn;
    private TextView title;
    double duration;
    private int taskID,correct,wrong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_questionslog,container,false);
        quest_recyclerView = v.findViewById(R.id.QuestionLog_container);
        back_btn = v.findViewById(R.id.back_btn);
        title = v.findViewById(R.id.title);
        dur = v.findViewById(R.id.duration_per_question);

        AnyChartView anyChartView = v.findViewById(R.id.chart);
        anyChartView.setProgressBar(v.findViewById(R.id.progress_bar));
        anyChartView.setChart(chartSetup(correct,wrong));
        dur.setText("Duration per question:"+duration+" Sec(s)");
        title.setText("TEST "+(taskID+1));

        quest_layoutManager = new LinearLayoutManager(getActivity());
        questionsLogAdapter = new QuestionsLog_Adapter(getContext(), data);
        quest_recyclerView.setLayoutManager(quest_layoutManager);
        quest_recyclerView.setAdapter(questionsLogAdapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoryFragment()).commit();
            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        taskID = bundle.getInt("pos");
        setValue(bundle.getInt("ID"));

        duration = bundle.getInt("dur")/5.0;
    }

    public void setValue(int testID){
        dataBase = new DataBase(getContext());
        data = dataBase.getQuestionsLog(testID);
        correct = dataBase.GetCorrect(testID);
        wrong = dataBase.GetWrong(testID);
    }

    public Pie chartSetup(int correct,int wrong){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("correct",correct));
        data.add(new ValueDataEntry("wrong",wrong));


        pie.data(data);

        pie.labels().position("outside");



        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Score")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);



        return pie;
    }
}
