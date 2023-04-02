package com.example.firebase_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditBmn extends AppCompatActivity {
    TextView txt_kode_bmn, edit_nup, edit_nama_barang, edit_merk, edit_tahun_perolehan, txt_kodeBarang;
    EditText  edit_user, edit_lokasi, edit_kondisi, edit_keterangan;
    private String kode_bmn;
    private String nup;
    private String nama_barang;
    private String merk;
    private String tahun_perolehan;
    private String user;
    private String lokasi;
    private String kondisi;
    private String keterangan;
    private String kode_barang;

    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_bmn_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance("https://fir-login-50c04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        txt_kode_bmn = findViewById(R.id.text_kodeBmn);
        txt_kodeBarang = findViewById(R.id.text_kodeBarang);
        txt_kodeBarang.setText("");
        edit_nup = findViewById(R.id.edit_NUP);
        edit_nama_barang = findViewById(R.id.edit_namaBarang);
        edit_merk = findViewById(R.id.edit_merkTipe);
        edit_tahun_perolehan = findViewById(R.id.edit_tahunPerolehan);
        edit_user = findViewById(R.id.edit_pengguna);
        edit_lokasi = findViewById(R.id.edit_lokasiRuang);
        edit_kondisi = findViewById(R.id.edit_kondisi);
        edit_keterangan = findViewById(R.id.edit_keterangan);

        Button btn_cancel_edit = findViewById(R.id.btn_cancel_edit);
        Button btn_submit_edit = findViewById(R.id.btn_submit_edit);

        Intent intent = getIntent();
        //String str = intent.getStringExtra("kode_bmn");
        HashMap<String, String> map = (HashMap<String, String>) intent.getSerializableExtra("hashMapBmnString");

        //tampilan form dengan hint
        if (map != null) {
            //RETRIEVE VALUES
            kode_bmn = map.get("kode_bmn");//.toString();
             //Toast.makeText(getApplicationContext(), "dapet kode bmn " +kode_bmn, Toast.LENGTH_SHORT).show();
            kode_barang = map.get("kode_barang");//map.get("kode_barang").toString();
            nup = map.get("nup");//.toString();
            merk = map.get("merk");
            nama_barang = map.get("nama_barang");//.toString();
            tahun_perolehan = map.get("tahun_perolehan");//.toString();
             //Toast.makeText(getApplicationContext(), "Tahun Perolehan " +tahun_perolehan, Toast.LENGTH_SHORT).show();
            user =  map.get("user");//.toString();
            lokasi =  map.get("lokasi");//.toString();
            kondisi =  map.get("kondisi");//.toString();
            keterangan = map.get("keterangan");//.toString();

            txt_kode_bmn.setText(kode_bmn);
            txt_kodeBarang.setText(kode_barang);
            edit_nup.setText(nup);
            edit_nama_barang.setHint(nama_barang);
            edit_merk.setHint(merk);

            //edit_tahun_perolehan.setHint(tahun_perolehan);
            edit_tahun_perolehan.setText(tahun_perolehan);

            edit_user.setHint(user + " (Edit Here)");
            edit_lokasi.setHint(lokasi + " (Edit Here)");
            edit_kondisi.setHint(kondisi + " (Edit Here)");
            edit_keterangan.setHint(keterangan + " (Edit Here)");
        }

        //cancel edit, back to main activity
        btn_cancel_edit.setOnClickListener(v -> {
            //delete currently in use tree
            databaseReference.child("currently_in_use").child(firebaseUser.getUid()).removeValue();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();//to prevent back button navigate to previous stage
        });
        //submit edit
        btn_submit_edit.setOnClickListener(v -> {

            //Toast.makeText(getApplicationContext(), "Start Updating", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            // read and check input

            //String new_kode_bmn = String.valueOf(map.get("kode_bmn"));
            //String new_kode_barang = String.valueOf(map.get("kode_barang"));
            //String new_nup = String.valueOf(map.get("nup"));

            String new_merk = edit_merk.getText().toString();
            //Toast.makeText(getApplicationContext(), "NEW MERK INPUT " +new_merk, Toast.LENGTH_SHORT).show();
            String new_nama_barang = edit_nama_barang.getText().toString();

            String new_user = edit_user.getText().toString();
            String new_lokasi = edit_lokasi.getText().toString();
            String new_kondisi = edit_kondisi.getText().toString();
            String new_keterangan = edit_keterangan.getText().toString();

            HashMap<String, String> newMap = new HashMap<>();
            //map the input if input not empty
            if (checkInput(merk, new_merk)||checkInput(nama_barang, new_nama_barang)
                    ||checkInput(user, new_user)
                    ||checkInput(lokasi, new_lokasi)||checkInput(kondisi, new_kondisi)
                    ||checkInput(keterangan, new_keterangan)) {

                //merk string dari map, new_map string dari input edit
                if(new_merk != null && !new_merk.isEmpty() && !new_merk.trim().isEmpty()){
                    newMap.put("merk", new_merk);
                }else{newMap.put("merk", merk);}
                if(new_nama_barang != null && !new_nama_barang.isEmpty() && !new_nama_barang.trim().isEmpty()){
                    newMap.put("nama_barang", new_nama_barang);
                }else{newMap.put("nama_barang", nama_barang);}
                if(new_user != null && !new_user.isEmpty() && !new_user.trim().isEmpty()){
                    newMap.put("user", new_user);
                }else{newMap.put("user", user);}
                if(new_lokasi != null && !new_lokasi.isEmpty() && !new_lokasi.trim().isEmpty()){
                    newMap.put("lokasi", new_lokasi);
                }else{newMap.put("lokasi", lokasi);}
                if(new_kondisi != null && !new_kondisi.isEmpty() && !new_kondisi.trim().isEmpty()){
                    newMap.put("kondisi", new_kondisi);
                }else{newMap.put("kondisi", kondisi);}
                if(new_keterangan != null && !new_keterangan.isEmpty() && !new_keterangan.trim().isEmpty()){
                    newMap.put("keterangan", new_keterangan);
                }else{newMap.put("keterangan", keterangan);}

                //put preserved values
                newMap.put("kode_bmn", kode_bmn);
                //Toast.makeText(getApplicationContext(), "dapet kode bmn 2 " +kode_bmn, Toast.LENGTH_SHORT).show();
                newMap.put("kode_barang", kode_barang);
                newMap.put("nup", nup);
                //Toast.makeText(getApplicationContext(), "new merk PUT " +newMap.get("merk"), Toast.LENGTH_SHORT).show();

                /**
                newMap.put("nama_barang", new_nama_barang);
                //newMap.put("tahun_perolehan", new_tahun_perolehan);
                newMap.put("user", new_user);
                newMap.put("lokasi", new_lokasi);
                newMap.put("kondisi", new_kondisi);
                newMap.put("keterangan", new_keterangan);
                **/

                updateData(newMap);
            }
            finish();//to prevent back button navigate to previous stage
        });
    }

    protected boolean checkInput(Object awal, Object baru){
        //return !baru.isEmpty();
        return baru != null && !Objects.equals(awal, baru);
    }

    protected void updateData(HashMap<String, String> newMap){
        try{
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance("https://fir-login-50c04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

            //Toast.makeText(getApplicationContext(), "Merk UPDATE  "+ newMap.get("merk"), Toast.LENGTH_SHORT).show();
            databaseReference.child("bmn_by_kode").child(Objects.requireNonNull(newMap.get("kode_bmn"))).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //new HasMap named updateMap, this HashMap is used to update the BMN
                            Map<String, Object> updateMap = new HashMap<>();
                            for(DataSnapshot mySnapshot : snapshot.getChildren()){
                                updateMap.put(mySnapshot.getKey(), mySnapshot.getValue());
                            }
                            updateMap.put("merk", newMap.get("merk"));
                            updateMap.put("keterangan", newMap.get("keterangan"));
                            updateMap.put("kondisi", newMap.get("kondisi"));
                            updateMap.put("lokasi", newMap.get("lokasi"));
                            updateMap.put("user", newMap.get("user"));

                            databaseReference.child("bmn_by_kode").child(Objects.requireNonNull(newMap.get("kode_bmn"))).updateChildren(updateMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            Toast.makeText(getApplicationContext(), "Sukses Update", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Gagal Update", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}