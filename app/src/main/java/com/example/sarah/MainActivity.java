package com.example.sarah;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerSource, spinnerDest;
    EditText editInput;
    Button buttonConvert;
    TextView textResult;

    String[] units = {
            "Inch", "Foot", "Yard", "Mile", "Cm", "Km",
            "Pound", "Ounce", "Ton", "Kg", "Gram",
            "Celsius", "Fahrenheit", "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link UI components
        spinnerSource = findViewById(R.id.spinner_source);
        spinnerDest = findViewById(R.id.spinner_dest);
        editInput = findViewById(R.id.edit_input);
        buttonConvert = findViewById(R.id.button_convert);
        textResult = findViewById(R.id.text_result);

        // Set spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapter);
        spinnerDest.setAdapter(adapter);

        buttonConvert.setOnClickListener(v -> {
            String inputStr = editInput.getText().toString();
            if (inputStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            double inputValue;
            try {
                inputValue = Double.parseDouble(inputStr);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                return;
            }

            String from = spinnerSource.getSelectedItem().toString();
            String to = spinnerDest.getSelectedItem().toString();

            if (from.equals(to)) {
                textResult.setText("Converted Value: " + inputValue);
                return;
            }

            double result = convertUnits(from, to, inputValue);
            textResult.setText("Converted Value: " + result);
        });
    }

    private double convertUnits(String from, String to, double value) {
        Map<String, Double> lengthMap = new HashMap<>();
        lengthMap.put("Inch", 2.54);
        lengthMap.put("Foot", 30.48);
        lengthMap.put("Yard", 91.44);
        lengthMap.put("Mile", 160934.0);
        lengthMap.put("Cm", 1.0);
        lengthMap.put("Km", 100000.0);

        Map<String, Double> weightMap = new HashMap<>();
        weightMap.put("Pound", 453.592);
        weightMap.put("Ounce", 28.3495);
        weightMap.put("Ton", 907185.0);
        weightMap.put("Kg", 1000.0);
        weightMap.put("Gram", 1.0);

        if (lengthMap.containsKey(from) && lengthMap.containsKey(to)) {
            double inCm = value * lengthMap.get(from);
            return Math.round((inCm / lengthMap.get(to)) * 10000.0) / 10000.0;
        }

        if (weightMap.containsKey(from) && weightMap.containsKey(to)) {
            double inGrams = value * weightMap.get(from);
            return Math.round((inGrams / weightMap.get(to)) * 10000.0) / 10000.0;
        }

        // Temperature conversions
        switch (from + " to " + to) {
            case "Celsius to Fahrenheit":
                return (value * 1.8) + 32;
            case "Fahrenheit to Celsius":
                return (value - 32) / 1.8;
            case "Celsius to Kelvin":
                return value + 273.15;
            case "Kelvin to Celsius":
                return value - 273.15;
            case "Fahrenheit to Kelvin":
                return ((value - 32) / 1.8) + 273.15;
            case "Kelvin to Fahrenheit":
                return ((value - 273.15) * 1.8) + 32;
            default:
                Toast.makeText(this, "Conversion not supported", Toast.LENGTH_SHORT).show();
                return 0.0;
        }
    }
}
