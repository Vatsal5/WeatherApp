package com.androidstudio.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etCity.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please enter the city!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,Show.class);
                    intent.putExtra("city",etCity.getText().toString().trim());
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                }
            }
        });

    }
}
