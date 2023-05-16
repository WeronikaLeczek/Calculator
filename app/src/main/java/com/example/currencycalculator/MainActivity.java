package com.example.currencycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    // Definicja zmiennych dla spinnerów i pola tekstowego.

    // Deklaruje prywatne zmienne instancji dla dwóch spinnerów (rozwijane menu)
    private Spinner sourceCurrencySpinner;
    private Spinner targetCurrencySpinner;
    // i pola tekstowego.
    private EditText valueInput;

    @Override
    // Metoda onCreate jest wywoływana, gdy aktywność jest tworzona.
    // Jest to miejsce, w którym inicjalizujesz swoje widoki i inne zmienne instancji.
    protected void onCreate(Bundle savedInstanceState) {
        // Wywołuje metodę onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Przypisuje widoki o określonych identyfikatorach do zmiennych instancji.
        sourceCurrencySpinner = findViewById(R.id.source_currency_spinner);
        targetCurrencySpinner = findViewById(R.id.target_currency_spinner);
        valueInput = findViewById(R.id.value_input);

        // Wywołuje metodę initCurrencySpinners, aby zainicjować spinnerów walut.
        initCurrencySpinners();
    }

    // Metoda initCurrencySpinners inicjalizuje spinnerów walut.
    private void initCurrencySpinners() {
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "PLN"};

        //Tworzy nowy ArrayAdapter, który będzie używany do przekształcenia tablicy walut na widoczne elementy w spinnerach.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        //Ustawia układ rozwijanej listy spinnera.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Przypisuje adapter do spinnerów, co powoduje wyświetlenie listy walut w każdym z nich.
        sourceCurrencySpinner.setAdapter(adapter);
        targetCurrencySpinner.setAdapter(adapter);
    }
}