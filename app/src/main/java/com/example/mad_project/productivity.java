package com.example.mad_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Radar;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.JumpLine;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;



public class productivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_productivity, container, false);
        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        super.onCreate(savedInstanceState);
        Cartesian vertical = AnyChart.vertical();

        vertical.animation(true)
                .title("Vertical Combination of Bar and Jump Line Chart");

        List<DataEntry> data = new ArrayList<>();
        data.add(new CustomDataEntry("todays", 5000));
        data.add(new CustomDataEntry("Max", 20000));
        data.add(new CustomDataEntry("Mean", 10000));

        Set set = Set.instantiate();
        set.data(data);
        Mapping barData = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping jumpLineData = set.mapAs("{ x: 'x', value: 'jumpLine' }");

        Bar bar = vertical.bar(barData);
        bar.labels().format("${%Value} mln");

        JumpLine jumpLine = vertical.jumpLine(jumpLineData);
        jumpLine.stroke("2 #60727B");
        jumpLine.labels().enabled(false);

        vertical.yScale().minimum(0d);

        vertical.labels(true);

        vertical.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .positionMode(TooltipPositionMode.POINT)
                .unionFormat(
                        "function() {\n" +
                                "      return 'Plain: $' + this.points[1].value + ' mln' +\n" +
                                "        '\\n' + 'Fact: $' + this.points[0].value + ' mln';\n" +
                                "    }");

        vertical.interactivity().hoverMode(HoverMode.BY_X);

        vertical.xAxis(true);
        vertical.yAxis(true);
        vertical.yAxis(0).labels().format("${%Value} mln");

        anyChartView.setChart(vertical);
        return view;
    }

    private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value) {
            super(x, value);

        }
    }


    /*private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }*/

}

