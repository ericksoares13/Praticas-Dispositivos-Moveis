package com.example.mobile.projects.geolocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobile.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeolocationView extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean requestingLocationUpdates;
    @SuppressLint("MissingPermission")
    final ActivityResultLauncher<String[]> requestPermissionsLauncher = this.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        final Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
        final Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

        if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
            this.requestingLocationUpdates = true;
            this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest,
                    this.locationCallback,
                    Looper.getMainLooper());
        }

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this.getBaseContext(), "A localização é necessária para essa funcionalidade, ative nas configurações", Toast.LENGTH_SHORT).show();
        }
    });

    private Location currentLocation;
    private Marker currentMaker;

    private GoogleMap googleMap;
    private ViewType viewType;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.geolocation_view);

        this.viewType = this.getIntent().getSerializableExtra("view", ViewType.class);
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull final LocationResult locationResult) {
                if (locationResult.getLastLocation() == null) return;
                GeolocationView.this.currentLocation = locationResult.getLastLocation();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.requestingLocationUpdates = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.requestingLocationUpdates) {
            this.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.requestingLocationUpdates = true;
            this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest,
                    this.locationCallback,
                    Looper.getMainLooper());
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this.getBaseContext(), "A localização é necessária para essa funcionalidade", Toast.LENGTH_SHORT).show();
        }

        this.requestPermissionsLauncher.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
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

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.viewType.getLocale(), 15));
        Toast.makeText(this.getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void selectMyLocale(final View view) {
        if (!this.requestingLocationUpdates) {
            this.startLocationUpdates();
            return;
        }

        if (this.currentLocation != null) {
            if (this.currentMaker != null) {
                this.currentMaker.remove();
                this.currentMaker = null;
            }

            final LatLng latLng = new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude());
            final MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .title("Localização atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            this.currentMaker = this.googleMap.addMarker(markerOptions);

            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            Toast.makeText(this.getBaseContext(), "Localização atual", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getBaseContext(), "Localização ainda não disponível", Toast.LENGTH_SHORT).show();
        }
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
