package com.example.mobile.projects.check_in;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Report extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.check_in_management);

        final LinearLayout lines = this.findViewById(R.id.lines);

        for (final Home.CheckIn locale : this.getLocales()) {
            final LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            final TextView localText = this.getLocaleTextView(locale.locale());
            final TextView visitsText = this.getVisitsTextView(locale.visitsNumber());

            line.addView(localText);
            line.addView(visitsText);
            lines.addView(line);
        }
    }

    private TextView getLocaleTextView(final String locale) {
        final TextView localText = new TextView(this);
        localText.setText(locale);
        localText.setTextSize(20);
        localText.setTextColor(Color.parseColor("#333333"));

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                3
        );
        localText.setLayoutParams(params);

        return localText;
    }

    private TextView getVisitsTextView(final int visitsNumber) {
        final TextView localText = new TextView(this);
        localText.setText(String.valueOf(visitsNumber));
        localText.setTextSize(20);
        localText.setTextColor(Color.parseColor("#333333"));
        localText.setGravity(Gravity.CENTER);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        localText.setLayoutParams(params);

        return localText;
    }

    private List<Home.CheckIn> getLocales() {
        final List<Home.CheckIn> locales = new ArrayList<>();

        final Cursor c = DataBase.getInstance().search("CheckIn",
                null,
                "",
                null);

        while (c.moveToNext()) {
            final String locale = c.getString(c.getColumnIndexOrThrow("locale"));
            final int visitsNumber = c.getInt(c.getColumnIndexOrThrow("visitsNumber"));
            locales.add(new Home.CheckIn(locale, visitsNumber, 0, "", ""));
        }

        c.close();

        locales.sort(Comparator.comparing(Home.CheckIn::visitsNumber).reversed());

        return locales;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.check_in_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_back) {
            this.finish();
        }

        return true;
    }
}
