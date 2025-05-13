package com.example.mobile.projects.geolocation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeolocationView extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    ViewType viewType;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geolocation_view);

        this.viewType = getIntent().getSerializableExtra("view", ViewType.class);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        this.googleMap.addMarker(new MarkerOptions().position(ViewType.HOMETOWN.getLocale())
                .title(ViewType.HOMETOWN.getDescription()));
        this.googleMap.addMarker(new MarkerOptions().position(ViewType.CITY.getLocale())
                .title(ViewType.CITY.getDescription()));
        this.googleMap.addMarker(new MarkerOptions().position(ViewType.DEPARTMENT.getLocale())
                .title(ViewType.DEPARTMENT.getDescription()));

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.viewType.getLocale(), 15));
    }

    public void selectLocale(final View view) {
        final String tag = view.getTag().toString();
        String message = "";

        if (tag.equals(ViewType.HOMETOWN.toString())) {
            this.viewType = ViewType.HOMETOWN;
            message = this.viewType.getDescription();
        } else if (tag.equals(ViewType.CITY.toString())) {
            this.viewType = ViewType.CITY;
            message = this.viewType.getDescription();
        } else if (tag.equals(ViewType.DEPARTMENT.toString())) {
            this.viewType = ViewType.DEPARTMENT;
            message = this.viewType.getDescription();
        }

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.viewType.getLocale(), 15));
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    public enum ViewType {
        HOMETOWN("Minha casa na cidade natal", new LatLng(-19.4619317, -42.5872633)),
        CITY("Minha casa em Viçosa", new LatLng(-20.751637, -42.870514)),
        DEPARTMENT("Meu departamento", new LatLng(-20.765000, -42.868430));

        private final String description;
        private final LatLng locale;

        ViewType(final String description, final LatLng locale) {
            this.description = description;
            this.locale = locale;
        }

        public String getDescription() {
            return this.description;
        }

        public LatLng getLocale() {
            return this.locale;
        }
    }
}
