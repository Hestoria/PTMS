package com.example.stit.ptms.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stit.ptms.Adapter.TestsLog_Adapter;
import com.example.stit.ptms.DataBase;
import com.example.stit.ptms.Object.TestsLog;
import com.example.stit.ptms.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView.LayoutManager TestsLayoutManager;
    private RecyclerView TestsRecyclerView;
    private TestsLog_Adapter testsLogAdapter;
    private List<TestsLog> data = new ArrayList<>();
    private DataBase dataBase;
    private LinearLayout graph;
    private Chart chart;
    private SharedPreferences prefs =null;
    private int getdata = 0;
    private TextView Nodata_msg;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history,container,false);
        TestsRecyclerView = v.findViewById(R.id.TestsLog_container);
        graph = v.findViewById(R.id.graph);
        Nodata_msg = v.findViewById(R.id.Nodata_msg);


        TestsLayoutManager = new LinearLayoutManager(getActivity());
        testsLogAdapter = new TestsLog_Adapter(getContext(),data);
        TestsRecyclerView.setLayoutManager(TestsLayoutManager);
        TestsRecyclerView.setAdapter(testsLogAdapter);

        // draw chart
        if(getdata==0){
            graph.setVisibility(View.GONE);
            Nodata_msg.setVisibility(View.VISIBLE);
        }else {
            Nodata_msg.setVisibility(View.GONE);
            chart = new Chart(this.getContext());
            graph.addView(chart);
        }
        testsLogAdapter.setOnItemClickListener(new TestsLog_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID",data.get(position).getId());
                bundle.putInt("pos",position);
                bundle.putInt("dur",Integer.parseInt(
                        data.get(position).getDuration()
                ));
                QuestionsLogFragment fragment = new QuestionsLogFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("ptms_1",0);
        setvalue();
    }

    public void setvalue(){
        dataBase = new DataBase(getContext());
        if (dataBase.getUserID(prefs.getString("userName","")) != -1){
            data = dataBase.getTestsLog(dataBase.getUserID(prefs.getString("userName","")));
            getdata = 1;
        }
        else{
            getdata = 0;
            Log.d("data","nodata");
        }
    }

    class Chart extends View {
        public Chart(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            super.onDraw(canvas);
            Paint paint = new Paint();

            int front,behind;
            String Front,Behind;
            float tmp=0,tem1,tem2;
            int WrongCount = dataBase.GetWrongCount(),
            CorrectCount = dataBase.GetCorrectCount();

            // underline
            paint.setColor(Color.BLACK);
            paint.setTextSize(35);
            canvas.drawLine(200,100,200,500,paint);
            canvas.drawLine(200,500,650,500,paint);

            // set front chart and behind chart data
            if (CorrectCount > WrongCount){
                 front = Color.GREEN;
                 behind = Color.RED;
                 Behind = "Wrong :"+WrongCount;
                 Front = "Correct :" + CorrectCount;
                 tem1 = WrongCount;
                 tem2 = CorrectCount;
                 tmp = tem1/tem2*370;

            }else if(CorrectCount == WrongCount){
                front = Color.GREEN;
                behind = Color.RED;
                Behind = "Both :"+CorrectCount;
                Front = "";
                tem1 = WrongCount;
                tem2 = CorrectCount;
                tmp = tem1/tem2*370;
            }
            else{
                 behind = Color.GREEN;
                 front = Color.RED;
                 Front = "Wrong :"+WrongCount;
                 Behind = "Correct :" +CorrectCount;
                 tem2 = WrongCount;
                 tem1 = CorrectCount;
                 tmp = (tem1/tem2)*370;
            }

            paint.setColor(front);
            canvas.drawRect(280, 130, 365, 500, paint);

            paint.setColor(behind);
            canvas.drawRect(485, 500-tmp, 570, 500, paint);

            paint.setColor(Color.BLACK);
            canvas.drawLine(180,130,200,130,paint);
            Rect bound = new Rect();

            paint.getTextBounds(Front,0, Front.length(), bound);
            canvas.drawText(Front, 170-bound.width(), 130+bound.height()/2, paint);

            canvas.drawLine(180,500-tmp,200,500-tmp,paint);
            paint.getTextBounds(Behind,0, Behind.length(), bound);
            canvas.drawText(Behind, 170-bound.width(), 500-tmp+bound.height()/2, paint);

        }

    }
}
