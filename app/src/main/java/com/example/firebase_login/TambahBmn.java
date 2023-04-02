package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Objects;

public class TambahBmn extends AppCompatActivity {
    EditText input_kodeBarang, input_NUP, input_namaBarang, input_merkTipe, input_tahunPerolehan,
                input_pengguna, input_lokasiRuang, input_kondisi, input_keterangan;
    Button btn_cancel_tambah, btn_submit_tambah;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bmn);

        input_kodeBarang = findViewById(R.id.input_kodeBarang);
        input_NUP = findViewById(R.id.input_NUP);
        input_namaBarang = findViewById(R.id.input_namaBarang);
        input_merkTipe = findViewById(R.id.input_merkTipe);
        input_tahunPerolehan = findViewById(R.id.input_tahunPerolehan);
        input_pengguna = findViewById(R.id.input_pengguna);
        input_lokasiRuang = findViewById(R.id.input_lokasiRuang);
        input_kondisi = findViewById(R.id.input_kondisi);
        input_keterangan = findViewById(R.id.input_keterangan);

        btn_cancel_tambah = findViewById(R.id.btn_cancel_tambah);
        btn_submit_tambah = findViewById(R.id.btn_submit_tambah);

        btn_cancel_tambah.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });

        btn_submit_tambah.setOnClickListener(v->{

            isAllFieldsChecked = CheckAllFields();
            if(isAllFieldsChecked){
                final String data_input_kodeBarang = input_kodeBarang.getText().toString();
                final String data_input_Nup = input_NUP.getText().toString();
                final String data_input_namaBarang = input_namaBarang.getText().toString();
                final String data_input_merkTipe = input_merkTipe.getText().toString();
                final String data_input_tahunPerolehan = input_tahunPerolehan.getText().toString();
                final String data_input_pengguna = input_pengguna.getText().toString();
                final String data_input_lokasiRuang = input_lokasiRuang.getText().toString();
                final String data_input_kondisi = input_kondisi.getText().toString();
                final String data_input_keterangan = input_keterangan.getText().toString();
                final String data_input_kode_bmn = data_input_kodeBarang+data_input_Nup;

                HashMap<String, Object> mapTambah = new HashMap<>();
                mapTambah.put("keterangan", data_input_keterangan);
                mapTambah.put("kode_barang", data_input_kodeBarang);
                mapTambah.put("nup", data_input_Nup);
                mapTambah.put("kode_bmn", data_input_kode_bmn);
                mapTambah.put("kondisi", data_input_kondisi);
                mapTambah.put("lokasi", data_input_lokasiRuang);
                mapTambah.put("merk", data_input_merkTipe);
                mapTambah.put("nama_barang", data_input_namaBarang);
                mapTambah.put("tahun_perolehan", data_input_tahunPerolehan);
                mapTambah.put("user", data_input_pengguna);

                TambahDataBMN(mapTambah);
            }
        });
    }

    private void TambahDataBMN(HashMap<String,Object> newMap){
        try{
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance("https://fir-login-50c04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            databaseReference.child("bmn_by_kode").orderByChild("kode_bmn").equalTo(Objects.requireNonNull(newMap.get("kode_bmn")).toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Data BMN Already exists", Toast.LENGTH_SHORT).show();

                            }else{
                                databaseReference.child("bmn_by_kode").child(Objects.requireNonNull(newMap.get("kode_bmn")).toString()).updateChildren(newMap);

                                Toast.makeText(getApplicationContext(), "Sukses Update", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Gagal Update", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean CheckAllFields() {
        final String data_input_kodeBarang = input_kodeBarang.getText().toString();
        final String data_input_Nup = input_NUP.getText().toString();
        final String data_input_namaBarang = input_namaBarang.getText().toString();
        final String data_input_merkTipe = input_merkTipe.getText().toString();
        final String data_input_tahunPerolehan = input_tahunPerolehan.getText().toString();
        final String data_input_pengguna = input_pengguna.getText().toString();
        final String data_input_lokasiRuang = input_lokasiRuang.getText().toString();
        final String data_input_kondisi = input_kondisi.getText().toString();
        final String data_input_keterangan = input_keterangan.getText().toString();

        if(data_input_kodeBarang.length() == 0){
            input_kodeBarang.setError("Required not null nor empty");
            return false;
        }
        if(data_input_Nup.length() == 0){
            input_NUP.setError("Required not null nor empty");
            return false;
        }
        if(data_input_namaBarang.length() == 0){
            input_namaBarang.setError("Required not null nor empty");
            return false;
        }
        if(data_input_merkTipe.length() == 0){
            input_merkTipe.setError("Required not null nor empty");
            return false;
        }
        if(data_input_tahunPerolehan.length() == 0){
            input_tahunPerolehan.setError("Required not null nor empty");
            return false;
        }
        if(data_input_pengguna.length() == 0){
            input_pengguna.setError("Required not null nor empty");
            return false;
        }
        if(data_input_lokasiRuang.length() == 0){
            input_lokasiRuang.setError("Required not null nor empty");
            return false;
        }
        if(data_input_kondisi.length() == 0){
            input_kondisi.setError("Required not null nor empty");
            return false;
        }
        if(data_input_keterangan.length() == 0){
            input_keterangan.setError("Required not null nor empty");
            return false;
        }

        // after all validation return true.
        return true;
    }
}