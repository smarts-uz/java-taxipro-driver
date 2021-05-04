package com.ourdevelops.ourdriver.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class EditKendaraanRequestJson {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;

    @SerializedName("merek")
    @Expose
    private String merek;

    @SerializedName("tipe")
    @Expose
    private String tipe;

    @SerializedName("no_kendaraan")
    @Expose
    private String no_kendaraan;

    @SerializedName("warna")
    @Expose
    private String warna;

    @SerializedName("id_kendaraan")
    @Expose
    private String id_kendaraan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
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

    public String getNo_kendaraan() {
        return no_kendaraan;
    }

    public void setNo_kendaraan(String no_kendaraan) {
        this.no_kendaraan = no_kendaraan;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getId_kendaraan() {
        return id_kendaraan;
    }

    public void setId_kendaraan(String id_kendaraan) {
        this.id_kendaraan = id_kendaraan;
    }


}
