package com.example.firebase_login;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

//import com.budiyev.android.codescanner.DecodeCallback; import com.google.zxing.Result;

public class MyScanner extends AppCompatActivity {
    private boolean firstVisit;
    private CodeScanner mCodeScanner;
    private TextView scanResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firstVisit = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscanner);
        scanResultView = findViewById(R.id.text_scan_result);
        scanResultView.setText("Hasil Scan :");
        myScanning();
    }

    protected void myScanning() {
        //setContentView(R.layout.activity_myscanner);
        //TextView scanResultView = findViewById(R.id.text_scan_result);
        Button btnProceed = findViewById(R.id.btn_proceed);
        btnProceed.setVisibility(View.GONE);

        CodeScannerView scannerView = findViewById(R.id.myscanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();//ok?
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            firstVisit = false;
            scanResultView.setText(result.getText()); //QR code Apa adanya hasil scan


            // Mau dikasih alert dialog ? tapi mungkin user butuh melihat kecocokan foto dengan hasil di frame layout//
            btnProceed.setVisibility(View.VISIBLE);
            btnProceed.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), BmnProfile.class);
                //intent.putExtra("QRCODE", result.getText());

                intent.putExtra("QRCODE", getKodeBmn(result.getText()));
                intent.putExtra("ASAL", "Button Proceed class MyScanner");
                try{
                    startActivity(intent);
                    //finish(); //perhaps needs finish here 19 06 2022
                }catch (ActivityNotFoundException e){
                    Toast.makeText(MyScanner.this, "Fail to Proceed", Toast.LENGTH_SHORT).show();
                }
            });
            //Toast.makeText(MyScanner.this, result.getText(), Toast.LENGTH_SHORT).show();
        }));

        scannerView.setOnClickListener(view -> {
            scanResultView.setText("Hasil Scan :");
            mCodeScanner.startPreview();
            }
        );

    }

    //Ngambil Kode BMN dari dua terakhir INV-20210414145917581354000-054012900636892000KD-3100102001-253
    protected String getKodeBmn(String currentString){
        String newString = "";
        if(currentString.contains("*")&&currentString.contains("INV")){

            //Log.d("getKodeBmn currentString : ", "" +currentString);
            String[] separated = currentString.split("\\*");
            int panjang = separated.length-1; //karena indeks mulai dari 0, panjang mulai dari 1, kita mau ambil index dari parameter panjang
            /**
            for (int i = 0; i<separated.length; i++){
                Log.d("getKodeBmn separated : ", "separated ke " +i +": " +separated[i]);
            }
            */

            newString = String.join("",separated[panjang-1].toString(),separated[panjang].toString());
            //Log.d("getKodeBmn new String : ", "" +newString);
            return newString;
        }else{
            return newString;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!firstVisit){
            myScanning();
        }else{
            scanResultView.setText("Hasil Scan :");
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        if(!firstVisit){
            myScanning();
        }
    }

}