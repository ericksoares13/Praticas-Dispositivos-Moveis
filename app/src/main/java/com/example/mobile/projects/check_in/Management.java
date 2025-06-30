package com.example.mobile.projects.check_in;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.database.DataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Management extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.check_in_management);

        final LinearLayout lines = this.findViewById(R.id.lines);

        for (final String locale : this.getLocales()) {
            final LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            final TextView localText = this.getTextView(locale);
            final ImageButton deleteButton = this.getImageButton(locale);

            line.addView(localText);
            line.addView(deleteButton);
            lines.addView(line);
        }
    }

    private TextView getTextView(final String locale) {
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

    private ImageButton getImageButton(final String locale) {
        final ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.delete);
        deleteButton.setBackgroundColor(Color.TRANSPARENT);
        deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        deleteButton.setContentDescription("Excluir");

        final int sizeInDp = 40;
        final float scale = this.getResources().getDisplayMetrics().density;
        final int sizeInPx = (int) (sizeInDp * scale + 0.5f);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                sizeInPx,
                1
        );
        params.gravity = Gravity.CENTER;
        deleteButton.setLayoutParams(params);

        deleteButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Exclusão")
                .setMessage("Tem certeza que deseja excluir \"" + locale + "\"?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    DataBase.getInstance().delete("CheckIn", "locale = \"" + locale + "\"");
                    final Intent intent = this.getIntent();
                    this.finish();
                    this.startActivity(intent);
                })
                .setNegativeButton("Não", null)
                .show()
        );

        return deleteButton;
    }

    private List<String> getLocales() {
        final List<String> locales = new ArrayList<>();

        final Cursor c = DataBase.getInstance().search("CheckIn",
                null,
                "",
                null);

        while (c.moveToNext()) {
            final String locale = c.getString(c.getColumnIndexOrThrow("locale"));
            locales.add(locale);
        }

        c.close();

        Collections.sort(locales);

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
