package com.example.mad_project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.TreeDataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.charts.TreeMap;
import com.anychart.core.ui.Title;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Orientation;
import com.anychart.enums.SelectionMode;
import com.anychart.enums.TreeFillingMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class DailyFragment extends Fragment {
    private FirebaseFirestore db;
    CollectionReference colRef;
    String [] lis;
    int quantity;
    TreeMap treeMap;// = AnyChart.treeMap();
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();


        view = inflater.inflate(R.layout.fragment_daily, container, false);
        Context context = view.getContext();
        treeMap = AnyChart.treeMap();


        db.collection("inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value,
                               @Nullable FirebaseFirestoreException e) {
               DocumentReference docref;
               String item;

               if (e != null) {
                   Log.w(TAG, "Listen failed.", e);
                   return;
               }
               AnyChartView anyChartViewtree = (AnyChartView) view.findViewById(R.id.any_chart_view1);
               Log.d(TAG,"outside list");

               colRef = db.collection("inventory");


               treeMap = AnyChart.treeMap();
               List<DataEntry> data = new ArrayList<>();
               data.add(new CustomTreeDataEntry("Quantity", null, "Quantity in KG"));
               data.add(new CustomTreeDataEntry("Output", "Quantity", "Output", 692000));
               data.add(new CustomTreeDataEntry("input", "Quantity", "input", 597000));
               data.add(new CustomTreeDataEntry("Processed", "Quantity", "Processed", 597000));

               for (DocumentChange doc : value.getDocumentChanges()) {
                   QueryDocumentSnapshot document = doc.getDocument();
                   Log.d(TAG, doc.getType().toString());
                   switch (doc.getType()) {
                       case ADDED:
                           Log.d(TAG, "New city: " + doc.getDocument().getData());
                           break;
                       case MODIFIED:
                           Log.d(TAG, "Modified city: " + doc.getDocument().getData());
                           break;
                       case REMOVED:
                           Log.d(TAG, "Removed city: " + doc.getDocument().getData());
                           break;
                   }

                   String dataof = doc.getDocument().getData().toString();
                   String[] ddataof = dataof.split("[a-z]*=");
                   int l = ddataof.length;
                   for (int i = 1; i < ddataof.length; i++) {
                       ddataof[i] = ddataof[i].substring(0, ddataof[i].length() - 2);

                       Log.d(TAG, ddataof[i]);
                   }

                   String output = ddataof[1];
                   String input = ddataof[2];
                   String processed = "0";
                   if (l > 3)
                       processed = ddataof[3];
                   Log.d(TAG, input + " " + output + " " + processed);


                   data.add(new CustomTreeDataEntry(doc.getDocument().getId(), "Output",doc.getDocument().getId(), Integer.parseInt(output)));
                   data.add(new CustomTreeDataEntry(doc.getDocument().getId(), "input",doc.getDocument().getId(), Integer.parseInt(input)));
                   data.add(new CustomTreeDataEntry(doc.getDocument().getId(), "Processed",doc.getDocument().getId(),Integer.parseInt( processed)));
               }

                   Log.d(TAG,"entries added"+data.toString());
                   treeMap.data(data, TreeFillingMethod.AS_TABLE);

                   Title title = treeMap.title();
                   title.enabled(true);
                   title.useHtml(true);
                   title.padding(0d, 0d, 20d, 0d);
                   title.text("Products Quantity<br/>' +\n" +
                           "      '<span style=\"color:#212121; font-size: 13px;\">Quantity in KG)</span>");

                   treeMap.colorScale().ranges(new String[]{
                           "{ less: 250 }",
                           "{ from: 250, to: 300 }",
                           "{ from: 300, to: 400 }",
                           "{ from: 400, to: 500 }",
                           "{ from: 500, to: 1000 }",
                           "{ greater: 1000 }"
                   });

                   treeMap.colorScale().colors(new String[]{
                           "#ffee58", "#fbc02d", "#f57f17", "#c0ca33", "#689f38", "#2e7d32"
                   });

                   treeMap.padding(10d, 10d, 10d, 20d);
                   treeMap.maxDepth(2d);
                   treeMap.hovered().fill("#bdbdbd", 1d);
                   treeMap.selectionMode(SelectionMode.NONE);

                   treeMap.legend().enabled(true);
                   treeMap.legend()
                           .padding(0d, 0d, 0d, 20d)
                           .position(Orientation.RIGHT)
                           .align(Align.TOP)
                           .itemsLayout(LegendLayout.VERTICAL);

                   treeMap.labels().useHtml(true);
                   treeMap.labels().fontColor("#212121");
                   treeMap.labels().fontSize(12d);
                   treeMap.labels().format(
                           "function() {\n" +
                                   "      return this.getData('product');\n" +
                                   "    }");

                   treeMap.headers().format(
                           "function() {\n" +
                                   "    return this.getData('product');\n" +
                                   "  }");

                   treeMap.tooltip()
                           .useHtml(true)
                           .titleFormat("{%product}")
                           .format("function() {\n" +
                                   "      return '<span style=\"color: #bfbfbf\">Quantity: </span>$' +\n" +
                                   "        anychart.format.number(this.value, {\n" +
                                   "          groupsSeparator: ' '\n" +
                                   "        });\n" +
                                   "    }");

                   anyChartViewtree.setChart(treeMap);
//           }
//        });


        treeMap.padding(10d, 10d, 10d, 20d);
        treeMap.maxDepth(2d);
        treeMap.hovered().fill("#bdbdbd", 1d);
        treeMap.selectionMode(SelectionMode.NONE);

        treeMap.legend().enabled(true);
        treeMap.legend()
                .padding(0d, 0d, 0d, 20d)
                .position(Orientation.RIGHT)
                .align(Align.TOP)
                .itemsLayout(LegendLayout.VERTICAL);

        treeMap.labels().useHtml(true);
        treeMap.labels().fontColor("#212121");
        treeMap.labels().fontSize(12d);
        treeMap.labels().format(
                "function() {\n" +
                        "      return this.getData('product');\n" +
                        "    }");

        treeMap.headers().format(
                "function() {\n" +
                        "    return this.getData('product');\n" +
                        "  }");

        treeMap.tooltip()
                .useHtml(true)
                .titleFormat("{%product}")
                .format("function() {\n" +
                        "      return '<span style=\"color: #bfbfbf\">Revenue: </span>$' +\n" +
                        "        anychart.format.number(this.value, {\n" +
                        "          groupsSeparator: ' '\n" +
                        "        });\n" +
                        "    }");

        anyChartViewtree.setChart(treeMap);
           }
        });

        return view;
    }
}

class CustomTreeDataEntry extends TreeDataEntry {
    CustomTreeDataEntry(String id, String parent, String product, Integer value) {
        super(id, parent, value);
        setValue("product", product);
        Log.d(TAG,id +" "+parent + product+ value );
    }

    CustomTreeDataEntry(String id, String parent, String product) {
        super(id, parent);
        setValue("product", product);
    }
}
