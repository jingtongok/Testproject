package com.mosi.http;



import com.mosi.update.utils.ApiConstant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author gjt66888
 * Description: okhttp请求接口类
 * Time 2019/4/13  16:42
 */

public interface ApiService {

    /***
     *  *********************************  app 接口如下 ： **********************************
     */


    /**
     * app 全量更新
     *
     * @param range
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> executeDownload(@Header("Range") String range, @Url() String url);

    // 注册
    @FormUrlEncoded
    @POST(ApiConstant.URL + "/api/basicinfo/baseapi/functionbasiccom")
    Observable<String> getRegister(
            @Field("devicebrand") String devicebrand,
            @Field("deviceno") String deviceno,
            @Field("appversion") String appversion,
            @Field("keyinfo") String keyinfo);

    // 登录
    @FormUrlEncoded
    @POST(ApiConstant.URL + "/api/User/loginother")
    Observable<String> getLogin(
            @Field("userName") String usercode,
            @Field("password") String userpwd);

    // 授权
    @FormUrlEncoded
    @POST(ApiConstant.URL + "/api/basicinfo/baseapi/functionbasiccheck")
    Observable<String> getBasiccheck(
            @Field("devicebrand") String devicebrand,
            @Field("deviceno") String deviceno,
            @Field("appversion") String appversion,
            @Field("keyinfo") String keyinfo);

    // 上报数据
    @POST()
    Observable<String> getHomeInfo(@Url() String urls, @Body RequestBody body);

    // 更新
    @FormUrlEncoded
    @POST(ApiConstant.URL + "/api/basicinfo/baseapi/functionbasicappupdate")
    Observable<String> getUpdate(
            @Field("appversion") String appversion,
            @Field("token") String token,
            @Field("deviceno") String deviceno,
            @Field("devicebrand") String devicebrand,
            @Field("keyinfo") String keyinfo);


}
