package com.example.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.mobile.projects.calculator.CompleteCalculator;
import com.example.mobile.projects.calculator.SimpleCalculator;
import com.example.mobile.projects.geolocation.Geolocation;
import com.example.mobile.projects.imc.IMCCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String PRACTICE = "PRACTICE";
    private static final String ITEM = "ITEM";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(this.getSimpleExpandableListAdapterProjects());
        this.manageListClick(expandableListView);
    }

    private SimpleExpandableListAdapter getSimpleExpandableListAdapterProjects() {
        final List<Map<String, String>> groupList = new ArrayList<>();
        final List<List<Map<String, String>>> childList = new ArrayList<>();

        this.fillWithCalculatorProject(groupList, childList);
        this.fillWithIMCProject(groupList, childList);
        this.fillWithGeolocationProject(groupList, childList);

        return new SimpleExpandableListAdapter(
                this,
                groupList,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{PRACTICE},
                new int[]{android.R.id.text1},
                childList,
                android.R.layout.simple_list_item_1,
                new String[]{ITEM},
                new int[]{android.R.id.text1}
        );
    }

    private void fillWithCalculatorProject(final List<Map<String, String>> groupList, final List<List<Map<String, String>>> childList) {
        final Map<String, String> group = new HashMap<>();
        group.put(PRACTICE, "Prática 1");
        groupList.add(group);

        final List<Map<String, String>> children = new ArrayList<>();

        final Map<String, String> child1 = new HashMap<>();
        child1.put(ITEM, "Calculadora Simples");
        children.add(child1);

        final  Map<String, String> child2 = new HashMap<>();
        child2.put(ITEM, "Calculadora Completa");
        children.add(child2);

        childList.add(children);
    }

    private void fillWithIMCProject(final List<Map<String, String>> groupList, final List<List<Map<String, String>>> childList) {
        final Map<String, String> group = new HashMap<>();
        group.put(PRACTICE, "Prática 2");
        groupList.add(group);

        final List<Map<String, String>> children = new ArrayList<>();

        final Map<String, String> child1 = new HashMap<>();
        child1.put(ITEM, "Calculadora IMC");
        children.add(child1);

        childList.add(children);
    }

    private void fillWithGeolocationProject(final List<Map<String, String>> groupList, final List<List<Map<String, String>>> childList) {
        final Map<String, String> group = new HashMap<>();
        group.put(PRACTICE, "Prática 3");
        groupList.add(group);

        final List<Map<String, String>> children = new ArrayList<>();

        final Map<String, String> child1 = new HashMap<>();
        child1.put(ITEM, "Localização Geográfica");
        children.add(child1);

        childList.add(children);
    }

    private void manageListClick(final ExpandableListView expandableListView) {
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (groupPosition == 0) {
                if (childPosition == 0) {
                    startActivity(new Intent(MainActivity.this, SimpleCalculator.class));
                } else if (childPosition == 1) {
                    startActivity(new Intent(MainActivity.this, CompleteCalculator.class));
                }
            } else if (groupPosition == 1 && childPosition == 0) {
                startActivity(new Intent(MainActivity.this, IMCCalculator.class));
            } else if (groupPosition == 2 && childPosition == 0) {
                startActivity(new Intent(MainActivity.this, Geolocation.class));
            }

            return true;
        });
    }
}
