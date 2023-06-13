package com.example.currencycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner sourceCurrencySpinner;
    private Spinner targetCurrencySpinner;
    private EditText valueInput;
    private Map<String, Double> currencyRates = new HashMap<>();
    private TextView convertedValueText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceCurrencySpinner = findViewById(R.id.source_currency_spinner);
        targetCurrencySpinner = findViewById(R.id.target_currency_spinner);
        valueInput = findViewById(R.id.value_input);
        convertedValueText = findViewById(R.id.converted_value);

        initCurrencySpinners();

        fetchCurrencyRates();

        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertCurrency();
            }
        });

        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrencyRates();
                Toast.makeText(MainActivity.this, "Kursy walut zaktualizowane", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initCurrencySpinners() {
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "PLN"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sourceCurrencySpinner.setAdapter(adapter);
        targetCurrencySpinner.setAdapter(adapter);
    }

    private void fetchCurrencyRates() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.exchangeratesapi.io/latest?access_key=de85ad2bdc9a662498294efc34a99467";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            System.out.println(response);
                            Iterator<String> keys = rates.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                double rate = rates.getDouble(key);
                                currencyRates.put(key, rate);

                                Log.d("CurrencyRates", "Currency: " + key + ", Rate: " + rate);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CurrencyRates", "Error: ", error);
                    }
                });

        queue.add(jsonObjectRequest);
    }


    private void convertCurrency() {
        String sourceCurrency = sourceCurrencySpinner.getSelectedItem().toString();
        String targetCurrency = targetCurrencySpinner.getSelectedItem().toString();

        Double inputValue;
        try {
            inputValue = Double.parseDouble(valueInput.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Invalid input value", Toast.LENGTH_SHORT).show();
            return;
        }

        Double sourceRate = currencyRates.get(sourceCurrency);
        Double targetRate = currencyRates.get(targetCurrency);
        Double convertedValue = (inputValue / sourceRate) * targetRate;

        if (sourceRate == null || targetRate == null) {
            Toast.makeText(MainActivity.this, "Rate not available", Toast.LENGTH_SHORT).show();
            return;
        }

        convertedValueText.setText("Przeliczona wartość: " + String.format(Locale.getDefault(), "%.2f", convertedValue));
    }
}
