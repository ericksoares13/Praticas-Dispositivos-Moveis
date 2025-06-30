package com.example.mobile.projects.check_in;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Home extends AppCompatActivity {

    private final List<CheckIn> checkIns = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean requestingLocationUpdates;
    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher = this.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        final Boolean fineGranted = result.containsKey(Manifest.permission.ACCESS_FINE_LOCATION)
                ? result.get(Manifest.permission.ACCESS_FINE_LOCATION)
                : Boolean.FALSE;

        final Boolean coarseGranted = result.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION)
                ? result.get(Manifest.permission.ACCESS_COARSE_LOCATION)
                : Boolean.FALSE;

        if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
            this.requestingLocationUpdates = true;
            this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest,
                    this.locationCallback,
                    Looper.getMainLooper());
            return;
        }

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this.getBaseContext(), "A localização é necessária para essa funcionalidade, ative nas configurações", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.check_in_home);

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull final LocationResult locationResult) {
                if (locationResult.getLastLocation() == null) return;

                final Location currentLocation = locationResult.getLastLocation();
                final String latitude = String.valueOf(currentLocation.getLatitude());
                final String longitude = String.valueOf(currentLocation.getLongitude());

                ((TextView) Home.this.findViewById(R.id.latitudeText)).setText(latitude);
                ((TextView) Home.this.findViewById(R.id.longitudeText)).setText(longitude);
            }
        };

        this.setSpinnerCategories();
        this.setAutoCompleteLocales();
    }

    private void setSpinnerCategories() {
        final Cursor c = DataBase.getInstance().search("Category",
                null,
                "",
                null);

        while (c.moveToNext()) {
            final int id = c.getInt(c.getColumnIndexOrThrow("idCategory"));
            final String name = c.getString(c.getColumnIndexOrThrow("name"));
            this.categories.add(new Category(id, name));
        }

        c.close();

        this.categories.sort(Comparator.comparing(Category::name));
        final Spinner spinner = this.findViewById(R.id.localCategorySpinner);
        final ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                this.categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setAutoCompleteLocales() {
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
            this.checkIns.add(new CheckIn(locale, visitsNumber, categoryId, latitude, longitude));
        }

        c.close();

        this.checkIns.sort(Comparator.comparing(CheckIn::locale));
        final AutoCompleteTextView autoComplete = this.findViewById(R.id.localNameEditText);
        final ArrayAdapter<CheckIn> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                this.checkIns
        );
        autoComplete.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.check_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_map) {
            //this.startActivity(new Intent(Home.this, ));
        } else if (id == R.id.action_map) {
            //this.startActivity(new Intent(Home.this, ));
        } else if (id == R.id.action_map) {
            //this.startActivity(new Intent(Home.this, ));
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.requestingLocationUpdates = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!this.requestingLocationUpdates) {
            this.startLocationUpdates();
        }
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

    public void checkIn(final View view) {
        if (!this.requestingLocationUpdates) {
            this.startLocationUpdates();
            return;
        }

        final String locale = ((AutoCompleteTextView) this.findViewById(R.id.localNameEditText)).getText().toString();

        if (locale.isEmpty()) {
            Toast.makeText(this.getBaseContext(), "Digite um local", Toast.LENGTH_SHORT).show();
            return;
        }

        final Category category = (Category) ((Spinner) this.findViewById(R.id.localCategorySpinner)).getSelectedItem();

        if (category == null) {
            Toast.makeText(this.getBaseContext(), "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        final String latitude = ((TextView) this.findViewById(R.id.latitudeText)).getText().toString();
        final String longitude = ((TextView) this.findViewById(R.id.longitudeText)).getText().toString();

        if (latitude.equals("carregando...") || longitude.equals("carregando...")) {
            Toast.makeText(this.getBaseContext(), "Espere a localização carregar", Toast.LENGTH_SHORT).show();
            return;
        }

        final Optional<CheckIn> optionalCheckIn = this.checkIns.stream()
                .filter(c -> c.locale.equals(locale))
                .findFirst();

        if (optionalCheckIn.isPresent()) {
            final CheckIn checkIn = optionalCheckIn.get();
            this.insertCheckIn(locale, checkIn.visitsNumber + 1, -1, "", "", true);
        } else {
            this.insertCheckIn(locale, 1, category.id, latitude, longitude, false);
        }

        final Intent intent = this.getIntent();
        this.finish();
        this.startActivity(intent);
    }

    private void insertCheckIn(final String locale, final int visitsNumber, final int categoryId,
                               final String latitude, final String longitude, final boolean update) {
        final ContentValues values = new ContentValues();
        values.put("visitsNumber", visitsNumber);

        if (update) {
            DataBase.getInstance().update("CheckIn", values, null);
            return;
        }

        values.put("locale", locale);
        values.put("cat", categoryId);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        DataBase.getInstance().insert("CheckIn", values);
    }

    private record Category(int id, String name) {
        @NonNull
        @Override
        public String toString() {
            return this.name;
        }
    }

    private record CheckIn(String locale, int visitsNumber, int categoryId, String latitude,
                           String longitude) {
        @NonNull
        @Override
        public String toString() {
            return this.locale;
        }
    }
}
