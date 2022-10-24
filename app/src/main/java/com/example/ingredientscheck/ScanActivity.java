package com.example.ingredientscheck;

import static com.example.ingredientscheck.MainActivity.cancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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

import java.io.IOException;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private TextView textView;
    private CameraSource cameraSource;
    private static final int PERMISSION = 100;

    public static String[][] unhealthyIngredientsTr = {{"SODYUM NITRAT", "sodyum nitrat", "0"}, {"GIDA BOYASI", "gıda boyası", "0"}, {"TATLANDIRICI", "tatlandırıcı", "0"}, {"TRANS YAG", "trans yağ", "0"}, {"RENKLENDIRICI", "renklendirici", "0"}};
    public static String[][] unhealthyIngredientsEn = {{"SODIUM NITRATE", "sodium nitrate", "0"}, {"FOOD COLORING", "food coloring", "0"}, {"SWEETENER", "sweetener", "0"}, {"TRANS FAT", "trans fat", "0"}, {"COLORANT", "colorant", "0"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        surfaceView = findViewById(R.id.camera);
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        startCameraSource();

        StringBuilder firstStringBuilder = new StringBuilder();
        boolean found = false;

        for(int i = 0; i < unhealthyIngredientsTr.length; i++)
        {
            if(unhealthyIngredientsTr[i][2].equals("1"))
            {
                firstStringBuilder.append(unhealthyIngredientsTr[i][1]);
                firstStringBuilder.append("\n");
                found = true;
            }
        }

        for(int i = 0; i < unhealthyIngredientsEn.length; i++)
        {
            if(unhealthyIngredientsEn[i][2].equals("1"))
            {
                firstStringBuilder.append(unhealthyIngredientsEn[i][1]);
                firstStringBuilder.append("\n");
                found = true;
            }
        }

        if(found) {
            if(cancer) {
                firstStringBuilder.append("Yukarıdaki madde ya da maddeler rahatsızlığınızı tetikleyebilir");
                textView.setText(firstStringBuilder.toString());
            }
            else {
                firstStringBuilder.append("Yukarıdaki madde ya da maddeler bağırsak floranıza iyi gelmeyebilir");
                textView.setText(firstStringBuilder.toString());
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
                        textView.post(new Runnable() {
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
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                for(int i = 0; i < unhealthyIngredientsTr.length; i++)
                                {
                                    if(unhealthyIngredientsTr[i][2].equals("1"))
                                    {
                                        stringBuilder.append(unhealthyIngredientsTr[i][1]);
                                        stringBuilder.append("\n");
                                        found = true;
                                    }
                                }

                                for(int i = 0; i < unhealthyIngredientsEn.length; i++)
                                {
                                    if(unhealthyIngredientsEn[i][2].equals("1"))
                                    {
                                        stringBuilder.append(unhealthyIngredientsEn[i][1]);
                                        stringBuilder.append("\n");
                                        found = true;
                                    }
                                }

                                if(found) {
                                    if(cancer) {
                                        stringBuilder.append("Yukarıdaki madde ya da maddeler rahatsızlığınızı tetikleyebilir");
                                        textView.setText(stringBuilder.toString());
                                    }
                                    else {
                                        stringBuilder.append("Yukarıdaki madde ya da maddeler bağırsak floranıza iyi gelmeyebilir");
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