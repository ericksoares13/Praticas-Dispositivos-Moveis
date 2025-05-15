package com.example.mobile.projects.geolocation;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;
import com.google.android.gms.maps.model.LatLng;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class GeolocationReport extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.geolocation_report);

        final ListView listView = this.findViewById(R.id.geolocationListReport);
        final TextView emptyView = this.findViewById(R.id.emptyView);

        listView.setEmptyView(emptyView);

        final Map<String, LatLng> logsLocale = this.getLogsLocale();
        final String[] items = logsLocale.keySet().toArray(new String[0]);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            final String key = items[position];
            final LatLng latLng = logsLocale.get(key);

            if (latLng != null) {
                final String message = String.format(Locale.getDefault(), "Latitude: %.6f%nLongitude: %.6f", latLng.latitude, latLng.longitude);
                Toast.makeText(this.getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public Map<String, LatLng> getLogsLocale() {
        final Map<String, LatLng> logsLocale = new LinkedHashMap<>();

        final Cursor c = DataBase.getInstance().search("Logs log, Location loc",
                new String[]{"log.msg msg", "log.timestamp timestamp", "loc.latitude latitude", "loc.longitude longitude"},
                "log.id_location = loc.id",
                "log.id ASC");

        while (c.moveToNext()) {
            final String msg = c.getString(c.getColumnIndexOrThrow("msg"));
            final String timestamp = c.getString(c.getColumnIndexOrThrow("timestamp"));
            final double latitude = c.getDouble(c.getColumnIndexOrThrow("latitude"));
            final double longitude = c.getDouble(c.getColumnIndexOrThrow("longitude"));

            final Instant timestampInstant = Instant.parse(timestamp);
            final LocalDateTime timestampDateTime = LocalDateTime.ofInstant(timestampInstant, ZoneId.systemDefault());
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            final String timestampFormatted = dateTimeFormatter.format(timestampDateTime);

            final String log = msg + " - " + timestampFormatted;
            final LatLng locale = new LatLng(latitude, longitude);

            logsLocale.put(log, locale);
        }

        c.close();

        return logsLocale;
    }
}
