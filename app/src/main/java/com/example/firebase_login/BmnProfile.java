package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BmnProfile extends AppCompatActivity {
    //private String idBmn;
    TextView txt_kodeBarang, txt_nup, txt_nama_barang, txt_merk, txt_tahun_perolehan, txt_user, txt_lokasi, txt_kondisi, txt_keterangan, txt_kode_bmn;

    FirebaseAuth mAuth;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmn_profile);

        mAuth = FirebaseAuth.getInstance();

        txt_kodeBarang = findViewById(R.id.text_kodeBarang);
        txt_kodeBarang.setText("");
        txt_nup = findViewById(R.id.text_NUP);
        txt_nama_barang = findViewById(R.id.text_namaBarang);
        txt_merk = findViewById(R.id.text_merkTipe);
        txt_tahun_perolehan = findViewById(R.id.text_tahunPerolehan);
        txt_user = findViewById(R.id.text_pengguna);
        txt_lokasi = findViewById(R.id.text_lokasiRuang);
        txt_kondisi = findViewById(R.id.text_kondisi);
        txt_keterangan = findViewById(R.id.text_keterangan);
        txt_kode_bmn = findViewById(R.id.text_kodeBmn);

        Button btn_edit = findViewById(R.id.btn_edit);
        Button btn_no_edit = findViewById(R.id.btn_no_edit);

        Intent intent = getIntent();
        String str = intent.getStringExtra("QRCODE");
        if(str.length()==0){
            //notify null result
            Toast.makeText(getApplicationContext(), "Maaf, data tidak ditemukan", Toast.LENGTH_SHORT).show();
            //hide edit button
            btn_edit.setVisibility(View.GONE);
            btn_edit.setClickable(false);
            btn_edit.setFocusable(false);
            btn_edit.setBackgroundColor(Color.GRAY);
        }
        //String asal = intent.getStringExtra("ASAL");
        //txt_kodeBarang.setText(str);
        //Toast.makeText(getApplicationContext(), "QRCODE:\t" +str +"\nASAL :\t" +asal, Toast.LENGTH_SHORT).show();


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance("https://fir-login-50c04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //DatabaseReference currentlyInUseRef = mDatabaseReference.child("currently_in_use").getRef();

        mDatabaseReference.child("bmn_by_kode").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("Hasil Firebase","============================");
                //Log.d("Hasil Firebase", snapshot.getValue().toString());
                //System.out.println(snapshot.getValue().toString());
                try{

                    Map<String, Object> hashMapBmnObject = (Map<String, Object>) snapshot.getValue();

                    if(hashMapBmnObject!=null){
                        HashMap<String, String> hashMapBmnString = new HashMap<>();
                        String kode_barang = hashMapBmnObject.get("kode_barang").toString();// map.get("kode_barang").toString();
                        hashMapBmnString.put("kode_barang", kode_barang);
                        String nup = hashMapBmnObject.get("nup").toString();
                        hashMapBmnString.put("nup", nup);
                        String merk = hashMapBmnObject.get("merk").toString();
                        hashMapBmnString.put("merk", merk);
                        String nama_barang = hashMapBmnObject.get("nama_barang").toString();
                        hashMapBmnString.put("nama_barang", nama_barang);
                        String tahun_perolehan = hashMapBmnObject.get("tahun_perolehan").toString();
                        hashMapBmnString.put("tahun_perolehan", tahun_perolehan);
                        String user = hashMapBmnObject.get("user").toString();
                        hashMapBmnString.put("user", user);
                        String lokasi = hashMapBmnObject.get("lokasi").toString();
                        hashMapBmnString.put("lokasi", lokasi);
                        String kondisi = hashMapBmnObject.get("kondisi").toString();
                        hashMapBmnString.put("kondisi", kondisi);
                        String keterangan = hashMapBmnObject.get("keterangan").toString();
                        hashMapBmnString.put("keterangan", keterangan);
                        String kode_bmn = hashMapBmnObject.get("kode_bmn").toString();
                        hashMapBmnString.put("kode_bmn", kode_bmn);

                        txt_kodeBarang.setText(kode_barang); txt_nup.setText(nup); txt_nama_barang.setText(nama_barang); txt_merk.setText(merk); txt_tahun_perolehan.setText(tahun_perolehan);
                        txt_user.setText(user); txt_lokasi.setText(lokasi); txt_kondisi.setText(kondisi); txt_keterangan.setText(keterangan); txt_kode_bmn.setText(kode_bmn);

                        btn_edit.setOnClickListener(v -> {

                            //check flag
                            mDatabaseReference.child("currently_in_use").orderByChild("kode_bmn").equalTo(kode_bmn).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //check result
                                    flag = snapshot.exists();

                                    Toast.makeText(BmnProfile.this, "snapshot key: " +snapshot.getRef().getKey(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(BmnProfile.this, "uid: " +firebaseUser.getUid(), Toast.LENGTH_SHORT).show();

                                    //if check result is flagged as not in edit or if flagged as edit but this user is the editor
                                    if(!flag || snapshot.getRef().equals(firebaseUser.getUid())){
                                        try{
                                            //put record currently in use in DB tree
                                            mDatabaseReference.child("currently_in_use").child(firebaseUser.getUid()).setValue(hashMapBmnString);
                                            Log.i("Using","\tClaiming Use");
                                            flag = true;
                                        }catch(Exception e){
                                            Log.i("Error","\tERROR set value to currently in use");
                                        }

                                        //Start new activity EditBmn
                                        Intent intent_edit = new Intent(getApplicationContext(), EditBmn.class);
                                        //harusnya ngasih array yg udah dipunya, jangan satu string buat query
                                        //intent_edit.putExtra("kode_bmn", String.valueOf(txt_kode_bmn));
                                        //change is here
                                        intent_edit.putExtra("hashMapBmnString", hashMapBmnString);
                                        try{
                                            startActivity(intent_edit);
                                            finish();
                                            //Jangan di finish, nanti ketutup app nya setelah activity edit selesai,
                                            //biarkan saja biar balik lagi ke ...??
                                        }catch (ActivityNotFoundException e){
                                            Toast.makeText(getApplicationContext(), "Fail to Proceed Editing", Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        Toast.makeText(BmnProfile.this, "Edit is Denied", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(BmnProfile.this, "Currently in Edit By Someone Else", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(BmnProfile.this, "CHECK FLAG CANCELLED", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Toast.makeText(BmnProfile.this, "CHECK FLAG 2 : "+ Boolean.toString(flag), Toast.LENGTH_SHORT).show();
                       });
                    ////
                    }else{
                        if(flag){
                            Toast.makeText(getApplicationContext(), "EXITING EDIT, Currently in Use", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Maaf, data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        btn_edit.setVisibility(View.GONE);
                        btn_edit.setClickable(false);
                        btn_edit.setFocusable(false);
                        btn_edit.setBackgroundColor(Color.GRAY);
                    }
                }catch (Exception e){
                    //e.printStackTrace();
                    Log.i("Error","\tFail to try creating hash map");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //tadinya dsini btn_edit.setOnClickListener

        btn_no_edit.setOnClickListener(v->{
            //remove value from currently in use
            if(flag){
                mDatabaseReference.child("currently_in_use").child(firebaseUser.getUid()).removeValue();
            }
            Toast.makeText(this, "Cancel Edit", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BmnProfile.this, MainActivity.class));
            finish();//to prevent back button navigate to previous stage
        });
    }
}