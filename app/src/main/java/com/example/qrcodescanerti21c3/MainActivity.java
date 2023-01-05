package com.example.qrcodescanerti21c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view object
    private Button buttonScanning;
    private TextView textViewName,textViewClass,textViewId;
    //qr scanning object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view object
        buttonScanning = (Button) findViewById(R.id.buttonscan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass = (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNim);
        //Inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi onclick Listener
        buttonScanning.setOnClickListener(this);

    }
    //untuk mendapatkan hasil scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);

        if (result !=null){
            //jika qrcode tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this,"Hasil Scanning tidak ada",
                        Toast.LENGTH_LONG).show();
            }else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            } else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                String number = String.valueOf(result.getContents());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
                try {
                    startActivity(Intent.createChooser(callIntent, "waiting..."));
            }catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There ada no phone apk client installed.", Toast.LENGTH_SHORT).show();
            }
            //jika qr code tidak ditemukan datanya
            try {
                //konversi datanya ke json
                JSONObject obj = new JSONObject(result.getContents());
                //diset nilai datanya ke textviews
                textViewName.setText(obj.getString("nama :"));
                textViewClass.setText(obj.getString("kelas :"));
                textViewId.setText(obj.getString("nim :"));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, result.getContents(),
                        Toast.LENGTH_LONG).show();
            }
        } }{
        try {
            String geoUri = result.getContents();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            //Set Package
            intent.setPackage("com.google.android.apps.maps");

            //Set Flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }finally {

            }
        } {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        //inisialisasi qrCode scanning
        qrScan.initiateScan();

    }
}