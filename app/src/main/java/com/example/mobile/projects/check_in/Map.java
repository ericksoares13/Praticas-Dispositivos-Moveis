package com.example.mobile.projects.check_in;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng latLng;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.check_in_map);

        final String latitude = this.getIntent().getStringExtra("latitude");
        final String longitude = this.getIntent().getStringExtra("longitude");
        if (latitude != null && longitude != null) {
            this.latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }

        final SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapCheckIn);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.check_in_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_back) {
            this.finish();
        } else if (id == R.id.action_management) {
            this.startActivity(new Intent(Map.this, Management.class));
        } else if (id == R.id.action_places) {
            this.startActivity(new Intent(Map.this, Report.class));
        } else if (id == R.id.action_normal_map) {
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.action_hybrid_map) {
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        return true;
    }

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        final java.util.Map<Integer, String> categories = this.getCategories();

        for (final Home.CheckIn checkIn : this.getLocales()) {
            final String latitude = checkIn.latitude();
            final String longitude = checkIn.longitude();

            if (latitude != null && longitude != null) {
                final LatLng checkInLocale = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                final String category = categories.get(checkIn.categoryId());

                this.googleMap.addMarker(new MarkerOptions().position(checkInLocale)
                        .title(checkIn.locale()).snippet("Categoria: " + category + "; Visitas: " + checkIn.visitsNumber()));
            }
        }

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLng, 15));
    }

    private List<Home.CheckIn> getLocales() {
        final List<Home.CheckIn> checkIns = new ArrayList<>();

        final Cursor c = DataBase.getInstance().search("CheckIn",
                null,
                "",
                null);

        while (c.moveToNext()) {
            final String locale = c.getString(c.getColumnIndexOrThrow("locale"));
            final int visitsNumber = c.getInt(c.getColumnIndexOrThrow("visitsNumber"));
            final int categoryId = c.getInt(c.getColumnIndexOrThrow("cat"));
            final String latitude = c.getString(c.getColumnIndexOrThrow("latitude"));
            final String longitude = c.getString(c.getColumnIndexOrThrow("longitude"));
            checkIns.add(new Home.CheckIn(locale, visitsNumber, categoryId, latitude, longitude));
        }

        c.close();

        return checkIns;
    }

    private java.util.Map<Integer, String> getCategories() {
        final java.util.Map<Integer, String> categories = new HashMap<>();

        final Cursor c = DataBase.getInstance().search("Category",
                null,
                "",
                null);

        while (c.moveToNext()) {
            final int id = c.getInt(c.getColumnIndexOrThrow("idCategory"));
            final String name = c.getString(c.getColumnIndexOrThrow("name"));
            categories.put(id, name);
        }

        c.close();

        return categories;
    }
}
