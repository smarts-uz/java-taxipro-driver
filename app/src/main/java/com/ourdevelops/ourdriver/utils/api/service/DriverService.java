package com.ourdevelops.ourdriver.utils.api.service;

import com.ourdevelops.ourdriver.json.AcceptRequestJson;
import com.ourdevelops.ourdriver.json.AcceptResponseJson;
import com.ourdevelops.ourdriver.json.ChangePassRequestJson;
import com.ourdevelops.ourdriver.json.GetOnRequestJson;
import com.ourdevelops.ourdriver.json.UpdateLocationRequestJson;
import com.ourdevelops.ourdriver.json.AllTransResponseJson;
import com.ourdevelops.ourdriver.json.DetailRequestJson;
import com.ourdevelops.ourdriver.json.DetailTransResponseJson;
import com.ourdevelops.ourdriver.json.EditKendaraanRequestJson;
import com.ourdevelops.ourdriver.json.EditprofileRequestJson;
import com.ourdevelops.ourdriver.json.GetHomeRequestJson;
import com.ourdevelops.ourdriver.json.GetHomeResponseJson;
import com.ourdevelops.ourdriver.json.LoginRequestJson;
import com.ourdevelops.ourdriver.json.LoginResponseJson;
import com.ourdevelops.ourdriver.json.PrivacyRequestJson;
import com.ourdevelops.ourdriver.json.PrivacyResponseJson;
import com.ourdevelops.ourdriver.json.RegisterRequestJson;
import com.ourdevelops.ourdriver.json.RegisterResponseJson;
import com.ourdevelops.ourdriver.json.ResponseJson;
import com.ourdevelops.ourdriver.json.TopupRequestJson;
import com.ourdevelops.ourdriver.json.TopupResponseJson;
import com.ourdevelops.ourdriver.json.VerifyRequestJson;
import com.ourdevelops.ourdriver.json.WalletRequestJson;
import com.ourdevelops.ourdriver.json.WalletResponseJson;
import com.ourdevelops.ourdriver.json.WithdrawRequestJson;
import com.ourdevelops.ourdriver.json.WithdrawResponseJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface DriverService {

    @POST("driver/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("driver/update_location")
    Call<ResponseJson> updatelocation(@Body UpdateLocationRequestJson param);

    @POST("driver/syncronizing_account")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("driver/logout")
    Call<GetHomeResponseJson> logout(@Body GetHomeRequestJson param);

    @POST("driver/turning_on")
    Call<ResponseJson> turnon(@Body GetOnRequestJson param);

    @POST("driver/accept")
    Call<AcceptResponseJson> accept(@Body AcceptRequestJson param);

    @POST("driver/start")
    Call<AcceptResponseJson> startrequest(@Body AcceptRequestJson param);

    @POST("driver/finish")
    Call<AcceptResponseJson> finishrequest(@Body AcceptRequestJson param);

    @POST("driver/edit_profile")
    Call<LoginResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("driver/edit_kendaraan")
    Call<LoginResponseJson> editKendaraan(@Body EditKendaraanRequestJson param);

    @POST("driver/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("driver/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("driver/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("driver/register_driver")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @POST("driver/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);


    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("pelanggan/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("driver/withdraw")
    Call<WithdrawResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("pelanggan/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("driver/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("driver/verifycode")
    Call<ResponseJson> verifycode(@Body VerifyRequestJson param);


}
