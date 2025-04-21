package com.example.mobile.projects.imc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mobile.R;

public class IMCCalculator extends Activity {

    private static final String LIFETIME = "Ciclo de vida";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LIFETIME, this.getLocalClassName() + ".onCreate() chamado.");

        this.setContentView(R.layout.imc_calculator);
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

    public void generateReport(final View view) {
        final EditText nameValue = this.findViewById(R.id.nameValue);
        final EditText ageValue = this.findViewById(R.id.ageValue);
        final EditText weightValue = this.findViewById(R.id.weightValue);
        final EditText heightValue = this.findViewById(R.id.heightValue);

        boolean isInvalid = false;
        isInvalid |= this.isEmptyField(nameValue);
        isInvalid |= this.isEmptyField(ageValue);
        isInvalid |= this.isEmptyField(weightValue);
        isInvalid |= this.isEmptyField(heightValue);
        isInvalid |= this.isZeroValue(weightValue);
        isInvalid |= this.isZeroValue(heightValue);


        if (isInvalid) {
            return;
        }

        final Bundle bundle = new Bundle();
        bundle.putString("name", nameValue.getText().toString());
        bundle.putInt("age", Integer.parseInt(ageValue.getText().toString()));
        bundle.putDouble("weight", Double.parseDouble(weightValue.getText().toString()));
        bundle.putDouble("height", Double.parseDouble(heightValue.getText().toString()));

        final Intent intent = new Intent(this, IMCReport.class);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private boolean isEmptyField(final EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Este campo é obrigatório");
            editText.requestFocus();
            return true;
        }

        return false;
    }

    private boolean isZeroValue(final EditText editText) {
        final String textValue = editText.getText().toString();

        if (textValue.isEmpty()) {
            return true;
        }

        if (Double.parseDouble(textValue) == 0) {
            editText.setError("Valor precisa ser diferente de 0");
            editText.requestFocus();
            return true;
        }

        return false;
    }
}
