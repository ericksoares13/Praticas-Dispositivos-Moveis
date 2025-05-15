package com.example.mobile.projects.geolocation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;
import com.example.mobile.projects.geolocation.GeolocationView.ViewType;

import java.time.Instant;

public class Geolocation extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.geolocation_list);

        final ListView listView = this.findViewById(R.id.geolocationList);
        final String[] items = {ViewType.HOMETOWN.getDescription(),
                ViewType.CITY.getDescription(),
                ViewType.DEPARTMENT.getDescription(),
                "Relatório",
                "Fechar aplicação"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> this.handleItemClick(position));
    }

    private void handleItemClick(final int position) {
        final ViewType geolocationView = switch (position) {
            case 0 -> ViewType.HOMETOWN;
            case 1 -> ViewType.CITY;
            case 2 -> ViewType.DEPARTMENT;
            default -> null;
        };

        if (geolocationView != null) {
            this.insertLog(geolocationView.getDescription(), geolocationView.name());

            final Intent it = new Intent(this, GeolocationView.class);
            it.putExtra("view", geolocationView);
            this.startActivity(it);
            return;
        }

        if (position == 3) {
            final Intent it = new Intent(this, GeolocationReport.class);
            this.startActivity(it);
            return;
        }

        this.finish();
    }

    private void insertLog(final String description, final String message) {
        final Cursor c = DataBase.getInstance().search("Location",
                new String[]{"id"},
                "description = '" + description + "'",
                null);

        final String timestamp = Instant.now() + "";
        final int idLocation = c.moveToFirst() ? c.getInt(c.getColumnIndexOrThrow("id")) : -1;
        c.close();

        final ContentValues values = new ContentValues();
        values.put("timestamp", timestamp);
        values.put("id_location", idLocation);

        if (idLocation == -1) {
            values.put("msg", "Invalid");
        } else {
            values.put("msg", message);
        }

        DataBase.getInstance().insert("Logs", values);
    }
}
