package com.example.firebase_login;

import java.util.Map;

public class MyBmnProfile {
    //String kode_bmn, kode_barang, nup, nama_barang, merk, tahun_perolehan, user, lokasi, kondisi, keterangan;
    Map<String , String> myMap;
    boolean validEdit;

    public MyBmnProfile(Map<String, String> map){
        myMap = map;
    }

    public String get_kode_bmn(){
        return myMap.get("kode_bmn");
    }
    public String get_kode_barang(){
        return myMap.get("kode_barang");
    }
    public String get_nup(){
        return myMap.get("nup");
    }
    public String get_nama_barang(){
        return myMap.get("nama_barang");
    }
    public String get_merk(){
        return myMap.get("merk");
    }
    public String get_tahun_perolehan(){
        return myMap.get("tahun_perolehan");
    }
    public String get_user(){
        return myMap.get("user");
    }
    public String get_lokasi(){
        return myMap.get("lokasi");
    }
    public String get_kondisi(){
        return myMap.get("kondisi");
    }
    public String get_keterangan(){
        return myMap.get("keterangan");
    }

    boolean isValidEdit(){
        validEdit = !myMap.isEmpty();

        return validEdit;
    }

}
