package com.example.mycalldemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {
    PieChartView pieChartView;
    PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        pieChartView=findViewById(R.id.pie);
        List<PieChartView.PieData> pieDataList =new ArrayList<>();
        PieChartView.PieData bean1=new PieChartView.PieData("已完成",0.5f, R.color.colorAccent);
        PieChartView.PieData bean2=new PieChartView.PieData("待完成",0.3f, R.color.colorPrimaryDark);
        PieChartView.PieData bean3=new PieChartView.PieData("进行中",0.2f, R.color.colorPrimary);
        pieDataList.add(bean1);
        pieDataList.add(bean2);
        pieDataList.add(bean3);
        pieChartView.setPieDataList(pieDataList);

        pieChart=findViewById(R.id.pies);
        List<PieChart.PieChartData> pieList =new ArrayList<>();
        PieChart.PieChartData beans1=new PieChart.PieChartData(R.color.colorAccent,120f,0.3f, 20f);
        PieChart.PieChartData beans2=new PieChart.PieChartData(R.color.colorPrimaryDark,90f,0.2f, 30f);
        PieChart.PieChartData beans3=new PieChart.PieChartData(R.color.colorPrimary,150f,0.5f, 50f);
        pieList.add(beans1);
        pieList.add(beans2);
        pieList.add(beans3);
        pieChart.setStartAngle(90);
        pieChart.setValues(pieList);
    }
}
