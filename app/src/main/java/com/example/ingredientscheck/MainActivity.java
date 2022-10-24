package com.example.ingredientscheck;

import static com.example.ingredientscheck.ScanActivity.unhealthyIngredientsEn;
import static com.example.ingredientscheck.ScanActivity.unhealthyIngredientsTr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    private Button ScanButton, ManualButton;
    private CheckBox cancerCheckBox;
    private static final int PERMISSION = 100;
    public static boolean cancer;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScanButton = findViewById(R.id.captureBtnId);
        ManualButton = findViewById(R.id.ManualBtn);
        cancerCheckBox = findViewById(R.id.cancerCheckBox);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        cancer = settings.getBoolean("cancer", cancer);

        cancerCheckBox.setChecked(cancer);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION);
        }

        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int j = 0; j < unhealthyIngredientsTr.length; j++)
                {
                    unhealthyIngredientsTr[j][2] = "0";
                }

                for(int j = 0; j < unhealthyIngredientsEn.length; j++)
                {
                    unhealthyIngredientsEn[j][2] = "0";
                }

                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });

        ManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ManualActivity.class);
                startActivity(i);
            }
        });

        cancerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancer = cancerCheckBox.isChecked();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("cancer", cancer);
                editor.commit();
            }
        });
    }
}