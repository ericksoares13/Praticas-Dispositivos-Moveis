package com.example.mobile.projects.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobile.R;

import java.util.Optional;

public class SimpleCalculator extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.simple_calculator);

        final TextView result = this.findViewById(R.id.result);
        result.setText(this.getString(R.string.result_text, ""));
    }

    private double extractDoubleValue(final EditText value) {
        return Optional.of(value.getText().toString())
                .filter(s -> !s.isEmpty())
                .map(Double::valueOf)
                .orElse(0.0);
    }

    public void clickButtonAdd(final View view) {
        final double value1 = this.extractDoubleValue(this.findViewById(R.id.value1));
        final double value2 = this.extractDoubleValue(this.findViewById(R.id.value2));
        final double sum = value1 + value2;

        final TextView result = this.findViewById(R.id.result);
        result.setText(this.getString(R.string.result_text, Double.toString(sum)));
    }

    public void clickButtonMin(final View view) {
        final double value1 = this.extractDoubleValue(this.findViewById(R.id.value1));
        final double value2 = this.extractDoubleValue(this.findViewById(R.id.value2));
        final double min = value1 - value2;

        final TextView result = this.findViewById(R.id.result);
        result.setText(this.getString(R.string.result_text, Double.toString(min)));
    }

    public void clickButtonMul(final View view) {
        final double value1 = this.extractDoubleValue(this.findViewById(R.id.value1));
        final double value2 = this.extractDoubleValue(this.findViewById(R.id.value2));
        final double mul = value1 * value2;

        final TextView result = this.findViewById(R.id.result);
        result.setText(this.getString(R.string.result_text, Double.toString(mul)));
    }

    public void clickButtonDiv(final View view) {
        final double value1 = this.extractDoubleValue(this.findViewById(R.id.value1));
        final double value2 = this.extractDoubleValue(this.findViewById(R.id.value2));

        final TextView result = this.findViewById(R.id.result);

        if (value2 == 0.0) {
            result.setText(this.getString(R.string.result_invalid));
        } else {
            final double div = value1 / value2;
            result.setText(this.getString(R.string.result_text, Double.toString(div)));
        }
    }
}
