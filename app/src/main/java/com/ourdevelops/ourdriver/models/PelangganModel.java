package com.ourdevelops.ourdriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public class PelangganModel extends RealmObject implements Serializable{

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("fullnama")
    private String fullnama;

    @Expose
    @SerializedName("no_telepon")
    private String noTelepon;

    @Expose
    @SerializedName("fotopelanggan")
    private String foto;

    @Expose
    @SerializedName("token")
    private String token;



    public String getFullnama() {
        return fullnama;
    }

    public void setFullnama(String fullnama) {
        this.fullnama = fullnama;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
