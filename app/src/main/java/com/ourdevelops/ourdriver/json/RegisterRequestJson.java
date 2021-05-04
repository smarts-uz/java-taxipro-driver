package com.ourdevelops.ourdriver.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class RegisterRequestJson {

    @SerializedName("nama_driver")
    @Expose
    private String namadriver;

    @SerializedName("no_ktp")
    @Expose
    private String noktp;

    @SerializedName("tgl_lahir")
    @Expose
    private String tglLahir = "-";

    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("job")
    @Expose
    private String job;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("alamat_driver")
    @Expose
    private String alamat;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("merek")
    @Expose
    private String merek;

    @SerializedName("tipe")
    @Expose
    private String tipe;

    @SerializedName("nomor_kendaraan")
    @Expose
    private String nomorkendaraan;

    @SerializedName("warna")
    @Expose
    private String warna;

    @SerializedName("foto_ktp")
    @Expose
    private String fotoktp;

    @SerializedName("foto_sim")
    @Expose
    private String fotosim;

    @SerializedName("id_sim")
    @Expose
    private String idsim;

    @SerializedName("checked")
    @Expose
    private String checked;

    @SerializedName("countrycode")
    @Expose
    private String countrycode;


    public String getNamadriver() {
        return namadriver;
    }

    public void setNamadriver(String namadriver) {
        this.namadriver = namadriver;
    }

    public String getNoktp() {
        return noktp;
    }

    public void setNoktp(String noktp) {
        this.noktp = noktp;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getNomorkendaraan() {
        return nomorkendaraan;
    }

    public void setNomorkendaraan(String nomorkendaraan) {
        this.nomorkendaraan = nomorkendaraan;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getFotoktp() {
        return fotoktp;
    }

    public void setFotoktp(String fotoktp) {
        this.fotoktp = fotoktp;
    }

    public String getFotosim() {
        return fotosim;
    }

    public void setFotosim(String fotosim) {
        this.fotosim = fotosim;
    }

    public String getIdsim() {
        return idsim;
    }

    public void setIdsim(String idsim) {
        this.idsim = idsim;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }


}
