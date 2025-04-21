package com.example.mobile.projects.imc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mobile.R;

public class IMCReport extends Activity {

    private static final String LIFETIME = "Ciclo de vida";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LIFETIME, this.getLocalClassName() + ".onCreate() chamado.");

        this.setContentView(R.layout.imc_report);

        final Intent intent = this.getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle == null) {
            return;
        }

        final String name = bundle.getString("name");
        final int age = bundle.getInt("age");
        final double weight = bundle.getDouble("weight");
        final double height = bundle.getDouble("height");
        final double imc = Math.round((weight / (height * height)) * 100.0) / 100.0;
        final String classification = this.getIMCClassification(imc);

        ((TextView) this.findViewById(R.id.nameValue)).setText(name);
        ((TextView) this.findViewById(R.id.ageValue)).setText(String.format(Integer.toString(age)));
        ((TextView) this.findViewById(R.id.weightValue)).setText(String.format(Double.toString(weight)));
        ((TextView) this.findViewById(R.id.heightValue)).setText(String.format(Double.toString(height)));
        ((TextView) this.findViewById(R.id.imcValue)).setText(String.format(Double.toString(imc)));
        ((TextView) this.findViewById(R.id.classificationValue)).setText(classification);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LIFETIME, this.getLocalClassName() + ".onStart() chamado.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LIFETIME, this.getLocalClassName() + ".onRestart() chamado.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LIFETIME, this.getLocalClassName() + ".onResume() chamado.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LIFETIME, this.getLocalClassName() + ".onPause() chamado.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LIFETIME, this.getLocalClassName() + ".onStop() chamado.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LIFETIME, this.getLocalClassName() + ".onDestroy() chamado.");
    }

    private String getIMCClassification(final double imc) {
        if (imc < 18.5) {
            return "Abaixo do Peso";
        } else if (imc < 25) {
            return "Saudável";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade Grau I";
        } else if (imc < 40) {
            return "Obesidade Grau II (severa)";
        } else {
            return "Obesidade Grau III (mórbida)";
        }
    }

    public void newCalculation(final View view) {
        final Intent intent = new Intent(this, IMCCalculator.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();
    }
}
