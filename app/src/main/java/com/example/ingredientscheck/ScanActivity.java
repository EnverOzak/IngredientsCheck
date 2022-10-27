package com.example.ingredientscheck;

import static com.example.ingredientscheck.MainActivity.cancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.chip.ChipGroup;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private ChipGroup chipGroup;
    private TextView textView;
    private static final int PERMISSION = 100;
    private int count = 0;

    public static String[][] unhealthyIngredientsTr = {
            {"ASPARTAM", "aspartam", "0", "asdafas"},
            {"E951", "e951", "0"},
            {"ASESULFAM K", "asesülfam k", "0"},
            {"E950", "e950", "0"},
            {"MONOSODYUM GLUTAMAT", "monosodyum glutamat", "0"},
            {"E621", "e621", "0"},
            {"SAKKARIN", "sakkarin", "0"},
            {"E954", "e954", "0"},
            {"SUKRALOZ", "sukraloz", "0", "gadsad"},
            {"E955", "e955", "0"},
            {"MALTITOL", "maltitol", "0"},
            {"E965", "e965", "0"},
            {"KSILITOL", "ksilitol", "0"},
            {"E967", "e967", "0"},
            {"SPLENDA","splenda","0"},
            {"SORBITOL","sorbitol","0"},
            {"E927","e927","0"},
            {"ERITRITOL","eritritol","0"},
            {"E968","e968","0"}
    };
    public static String[][] unhealthyIngredientsEn = {
            {"ASPARTAME", "aspartame", "0", "loalsd"},
            {"E951", "e951", "0"},
            {"ACESULFAME K", "acesulfame k", "0"},
            {"E950", "e950", "0"},
            {"MONOSODIUM GLUTAMATE", "monosodium glutamate", "0"},
            {"E621", "e621", "0"},
            {"SACCHARIN", "saccharin", "0"},
            {"E954", "e954", "0"},
            {"SUCRALOSE", "sucralose", "0", "gadsad"},
            {"E955", "e955", "0"},
            {"MALTITOL", "maltitol", "0"},
            {"E965", "e965", "0"},
            {"XYLITOL", "xylitol", "0"},
            {"E967", "e967", "0"},
            {"SPLENDA","splenda","0"},
            {"SORBITOL","sorbitol","0"},
            {"E927","e927","0"},
            {"ERYTHRITOL","erythritol","0"},
            {"E968","e968","0"}
    };

    private Button[] ingredientBtn = new Button[unhealthyIngredientsTr.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        surfaceView = findViewById(R.id.camera);
        chipGroup = findViewById(R.id.chipGroup);
        textView = findViewById(R.id.textView);

        startCameraSource();
        boolean found = false;

        for(int i = 0; i < unhealthyIngredientsTr.length; i++)
        {
            if(unhealthyIngredientsTr[i][2].equals("1"))
            {
                surfaceView.setBackground(getDrawable(R.drawable.border));
                ingredientBtn[count] = new Button(ScanActivity.this);
                ingredientBtn[count].setText(unhealthyIngredientsTr[i][1]);
                ingredientBtn[count].setBackgroundColor(Color.TRANSPARENT);
                ingredientBtn[count].setAllCaps(false);
                ingredientBtn[count].setPadding(10,10,10,10);
                chipGroup.addView(ingredientBtn[count]);
                int finalI = i;
                found = true;
                ingredientBtn[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle(unhealthyIngredientsTr[finalI][1]);
                        builder.setMessage(unhealthyIngredientsTr[finalI][3]);
                        builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }

        for(int i = 0; i < unhealthyIngredientsEn.length; i++)
        {
            if(unhealthyIngredientsEn[i][2].equals("1"))
            {
                surfaceView.setBackground(getDrawable(R.drawable.border));
                ingredientBtn[count] = new Button(ScanActivity.this);
                ingredientBtn[count].setText(unhealthyIngredientsEn[i][1]);
                ingredientBtn[count].setBackgroundColor(Color.TRANSPARENT);
                ingredientBtn[count].setAllCaps(false);
                ingredientBtn[count].setPadding(10,10,10,10);
                chipGroup.addView(ingredientBtn[count]);
                int finalI = i;
                found = true;
                ingredientBtn[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle(unhealthyIngredientsEn[finalI][1]);
                        builder.setMessage(unhealthyIngredientsEn[finalI][3]);
                        builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }

        if(found) {
            if(cancer) {
                textView.setText("Aşağıdaki madde ya da maddeler rahatsızlığınızı tetikleyebilir\nBilgi almak istediğiniz maddenin üzerine tıklayabilirsiniz");
            }
            else {
                textView.setText("Aşağıdaki madde ya da maddeler bağırsak floranıza iyi gelmeyebilir\nBilgi almak istediğiniz maddenin üzerine tıklayabilirsiniz");
            }
        }

    }

    private void startCameraSource (){
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()) {
            Log.w("Tag", "Dependencies Not Loaded Yet");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280,1024)
                    .setAutoFocusEnabled(true).setRequestedFps(2.0f).build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    try {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA},
                                    PERMISSION);
                            return;
                        }
                        cameraSource.start(surfaceView.getHolder());
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    //Release source for cameraSource
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    //Detect all text from camera
                }

                @Override
                public void receiveDetections(@NonNull Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    if(items.size() != 0)
                    {
                        chipGroup.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                boolean found = false;
                                for(int i = 0; i < items.size(); i++)
                                {
                                    TextBlock item = items.valueAt(i);

                                    for (int j = 0; j < unhealthyIngredientsTr.length; j++) {
                                        if (item.getValue().toUpperCase().replace("İ", "I").replace("Ü", "U").replace("Ğ", "G").replace("Ö", "O").replace("Ç", "C").contains(unhealthyIngredientsTr[j][0])) {
                                            if(unhealthyIngredientsTr[j][2].equals("0")) {
                                                if(unhealthyIngredientsEn[j][2].equals("0")) {
                                                    found = true;
                                                    unhealthyIngredientsTr[j][2] = "1";
                                                    surfaceView.setBackground(getDrawable(R.drawable.border));
                                                }
                                            }
                                        }
                                    }
                                    if(!found) {
                                        for (int j = 0; j < unhealthyIngredientsEn.length; j++) {
                                            if (item.getValue().toUpperCase().replace("İ", "I").replace("Ü", "U").replace("Ğ", "G").replace("Ö", "O").replace("Ç", "C").contains(unhealthyIngredientsEn[j][0])) {
                                                if(unhealthyIngredientsEn[j][2].equals("0")) {
                                                    if(unhealthyIngredientsTr[j][2].equals("0")) {
                                                        found = true;
                                                        unhealthyIngredientsEn[j][2] = "1";
                                                        surfaceView.setBackground(getDrawable(R.drawable.border));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                chipGroup.removeAllViewsInLayout();

                                for(int i = 0; i < unhealthyIngredientsTr.length; i++)
                                {
                                    if(unhealthyIngredientsTr[i][2].equals("1"))
                                    {
                                        ingredientBtn[count] = new Button(ScanActivity.this);
                                        ingredientBtn[count].setText(unhealthyIngredientsTr[i][1]);
                                        ingredientBtn[count].setBackgroundColor(Color.TRANSPARENT);
                                        ingredientBtn[count].setAllCaps(false);
                                        ingredientBtn[count].setPadding(10,10,10,10);
                                        chipGroup.addView(ingredientBtn[count]);
                                        int finalI = i;
                                        ingredientBtn[count].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                                                builder.setCancelable(true);
                                                builder.setTitle(unhealthyIngredientsTr[finalI][1]);
                                                builder.setMessage(unhealthyIngredientsTr[finalI][3]);
                                                builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }
                                }

                                for(int i = 0; i < unhealthyIngredientsEn.length; i++)
                                {
                                    if(unhealthyIngredientsEn[i][2].equals("1"))
                                    {
                                        ingredientBtn[count] = new Button(ScanActivity.this);
                                        ingredientBtn[count].setText(unhealthyIngredientsEn[i][1]);
                                        ingredientBtn[count].setBackgroundColor(Color.TRANSPARENT);
                                        ingredientBtn[count].setAllCaps(false);
                                        ingredientBtn[count].setPadding(10,10,10,10);
                                        chipGroup.addView(ingredientBtn[count]);
                                        int finalI = i;
                                        ingredientBtn[count].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                                                builder.setCancelable(true);
                                                builder.setTitle(unhealthyIngredientsEn[finalI][1]);
                                                builder.setMessage(unhealthyIngredientsEn[finalI][3]);
                                                builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }
                                }

                                if(found) {
                                    if(cancer) {
                                        textView.setText("Aşağıdaki madde ya da maddeler rahatsızlığınızı tetikleyebilir\nBilgi almak istediğiniz maddenin üzerine tıklayabilirsiniz");
                                    }
                                    else {
                                        stringBuilder.append("Aşağıdaki madde ya da maddeler bağırsak floranıza iyi gelmeyebilir\nBilgi almak istediğiniz maddenin üzerine tıklayabilirsiniz");
                                        textView.setText(stringBuilder.toString());
                                    }
                                }

                            }
                        });
                    }
                }
            });
        }
    }
}