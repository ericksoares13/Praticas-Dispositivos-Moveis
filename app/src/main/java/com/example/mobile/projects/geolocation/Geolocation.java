package com.example.mobile.projects.geolocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mobile.R;
import com.example.mobile.projects.geolocation.GeolocationView.ViewType;

public class Geolocation extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geolocation_list);

        final ListView listView = findViewById(R.id.geolocationList);
        final String[] items = {ViewType.HOMETOWN.getDescription(),
                ViewType.CITY.getDescription(),
                ViewType.DEPARTMENT.getDescription(),
                "Fechar aplicação"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> handleItemClick(position));
    }

    private void handleItemClick(int position) {
        final ViewType geolocationView = switch (position) {
            case 0 -> ViewType.HOMETOWN;
            case 1 -> ViewType.CITY;
            case 2 -> ViewType.DEPARTMENT;
            default -> null;
        };

        if (geolocationView == null) {
            finish();
            return;
        }

        final Intent it = new Intent(this, GeolocationView.class);
        it.putExtra("view", geolocationView);
        startActivity(it);
    }
}
