package com.ourdevelops.ourdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class User extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nama_driver")
    @Expose
    private String fullnama;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("alamat_driver")
    @Expose
    private String alamat;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("tgl_lahir")
    @Expose
    private String tglLahir;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("reg_id")
    @Expose
    private String token;

    @SerializedName("foto")
    @Expose
    private String fotodriver;
////
    @SerializedName("id_k")
    @Expose
    private String idk;

    @SerializedName("merek")
    @Expose
    private String merek;

    @SerializedName("tipe")
    @Expose
    private String tipe;

    @SerializedName("jenis")
    @Expose
    private String jenis;

    @SerializedName("nomor_kendaraan")
    @Expose
    private String nomor_kendaraan;

    @SerializedName("warna")
    @Expose
    private String warna;

    @SerializedName("countrycode")
    @Expose
    private String countrycode;

    @SerializedName("saldo")
    @Expose
    private long walletSaldo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullnama() {
        return fullnama;
    }

    public void setFullnama(String fullnama) {
        this.fullnama = fullnama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getWalletSaldo() {
        return walletSaldo;
    }

    public void setWalletSaldo(long walletSaldo) {
        this.walletSaldo = walletSaldo;
    }

    public String getFotodriver() {
        return fotodriver;
    }

    public void setFotodriver(String fotodriver) {
        this.fotodriver = fotodriver;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getNomorkendaraan() {
        return nomor_kendaraan;
    }

    public void setNomorkendaraan(String nomor_kendaraan) {
        this.nomor_kendaraan = nomor_kendaraan;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getIdk() {
        return idk;
    }

    public void setIdk(String idk) {
        this.idk = idk;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
