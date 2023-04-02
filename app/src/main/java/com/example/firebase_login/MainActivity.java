package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button btnScanner, btnSearch, btnTambah, btnLogOut;
    EditText editText;
    FirebaseAuth mAuth;

    //HashMap<String, String> requestArray = new HashMap<String, String>();

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnScanner = findViewById(R.id.btnScanner);
        btnSearch = findViewById(R.id.btnSearch);
        btnTambah = findViewById(R.id.btnTambah);
        btnLogOut = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        //Listener btnScanner
        btnScanner.setOnClickListener(view-> {
            //Check Permission
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }else{
                startActivity(new Intent(MainActivity.this, MyScanner.class));
                //finish();//optional
            }
        });

        //Listener btnTambah
        btnTambah.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), TambahBmn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(view->{
            //startActivity(new Intent(MainActivity.this, SearchActivity.class));
            searchDialog();

        });

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();//optional
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, MyScanner.class));
                //finish();//optional
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void searchDialog() {
        editText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Penelusuran")
                .setMessage("Silahkan Ketik Nomor Barang:")
                .setView(editText)
                .setPositiveButton("Search", (dialogInterface, i) -> {
                    String editTextInput = editText.getText().toString();
                    //Log.d("onclick","editext value is: "+ editTextInput);
                    Intent intent = new Intent(getApplicationContext(), BmnProfile.class);
                    intent.putExtra("QRCODE", editTextInput);
                    intent.putExtra("ASAL", "Search Dialog Main Activity");
                    startActivity(intent);
                    //finish();// biarkan saja ketika aktivitas tujuan sdh selesai, kembali ke sini
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();//optional
        }else{
            onAuthSuccess(user);


        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        //welcome message
        String username = usernameFromEmail(Objects.requireNonNull(user.getEmail()));
        //String useruid = user.getUid();

        String tString = getString(R.string.text_welcome, username);
        TextView t = findViewById(R.id.welcome_message);
        t.setText(tString);

        //create tree if not exist
        String userUid = user.getUid();
        userTree(userUid, username);
        //Log.i("user uid", "\t"+userUid);

    }

    private void userTree(String uid, String username) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://fir-login-50c04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        DatabaseReference uIdRef = rootRef.child("users").child(uid);
        uIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Log.i("user tree", "\tADA USER TREE");
                    Toast.makeText(MainActivity.this, "welcome back", Toast.LENGTH_SHORT).show();
                }else{
                    //Log.i("user tree", "\tNOT EXIST USER TREE");
                    Map<String, String> userRequest = new HashMap<>();

                    //put epoch time
                    long currentTime = System.currentTimeMillis();
                    userRequest.put("created", String.valueOf(currentTime));
                    //put username
                    userRequest.put("username", username);
                    //put UID
                    userRequest.put("uid", uid);

                    //processing user request
                    rootRef.child("users").child(uid).push().setValue(userRequest);
                    Toast.makeText(MainActivity.this, "A Tree Has Been Grown", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Koneksi Database Batal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}