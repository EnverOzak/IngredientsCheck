package com.example.ingredientscheck;

import static com.example.ingredientscheck.ScanActivity.unhealthyIngredientsEn;
import static com.example.ingredientscheck.ScanActivity.unhealthyIngredientsTr;
import static com.example.ingredientscheck.MainActivity.cancer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManualActivity extends AppCompatActivity {

    private Button backButton, checkButton;
    private EditText ingredientInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        backButton = findViewById(R.id.backBtn);
        checkButton = findViewById(R.id.checkBtn);
        ingredientInput = findViewById(R.id.ingredientInput);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManualActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(ingredientInput.getText());
                text = text.toUpperCase().replace("İ", "I").replace("Ü", "U").replace("Ğ", "G").replace("Ö", "O").replace("Ç", "C");

                boolean found = false;

                for (int j = 0; j < unhealthyIngredientsTr.length; j++) {
                    if (text.toUpperCase().replace("İ", "I").contains(unhealthyIngredientsTr[j][0])) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Kontrol Edildi");
                        if(cancer) {
                            builder.setMessage(unhealthyIngredientsTr[j][1] + " maddesi rahatsızlığınızı tetikleyebilir");
                        }
                        else {
                            builder.setMessage(unhealthyIngredientsTr[j][1] + " maddesi bağırsak floranıza iyi gelmeyebilir");
                        }
                        builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                        found = true;
                    }
                }
                for (int j = 0; j < unhealthyIngredientsEn.length; j++) {
                    if (text.toUpperCase().replace("İ", "I").contains(unhealthyIngredientsEn[j][0])) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Kontrol Edildi");
                        if(cancer) {
                            builder.setMessage(unhealthyIngredientsEn[j][1] + " maddesi rahatsızlığınızı tetikleyebilir");
                        }
                        else {
                            builder.setMessage(unhealthyIngredientsEn[j][1] + " maddesi bağırsak floranıza iyi gelmeyebilir");
                        }
                        builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                        found = true;
                    }
                }

                if(!found) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Kontrol Edildi");
                    builder.setMessage(text + " maddesini tüketmenizde bir sakınca yoktur");
                    builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }
}