package com.drkj.foxconn.net;

import com.drkj.foxconn.bean.EndTaskBean;
import com.drkj.foxconn.bean.EndTaskResultBean;
import com.drkj.foxconn.bean.EquipmentFaultBean;
import com.drkj.foxconn.bean.EquipmentFaultResultBean;
import com.drkj.foxconn.bean.EquipmentResultBean;
import com.drkj.foxconn.bean.FeedbackBean;
import com.drkj.foxconn.bean.FeedbackResultBean;
import com.drkj.foxconn.bean.RegionResultBean;
import com.drkj.foxconn.bean.StartTaskBean;
import com.drkj.foxconn.bean.StartTaskResultBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ganlong on 2018/1/24.
 */

public interface ServerApi {

    @FormUrlEncoded
    @POST(ConstantUrl.API_TOKEN)
    Observable<Response<ResponseBody>> getToken(@Field("username")String username, @Field("password")String password);


    @Headers("Content-Type: application/json")
    @POST(ConstantUrl.API_START_TASK)
    Observable<StartTaskResultBean> createTask(@Header("X-AUTH-TOKEN")String token, @Body StartTaskBean bean);

    @GET(ConstantUrl.API_GET_REGION)
    Observable<RegionResultBean> getRegion(@Header("X-AUTH-TOKEN") String token);

    @GET(ConstantUrl.API_GET_EQUIPMENT)
    Observable<EquipmentResultBean> getEquipment(@Header("X-AUTH-TOKEN") String token);


    @Headers({"Content-Type: application/json","Accept:  application/json"})
    @PUT(ConstantUrl.API_START_TASK+"/{id}")
    Observable<EndTaskResultBean> endTask(@Path("id") String id, @Header("X-AUTH-TOKEN")String token, @Body EndTaskBean bean);

    @Headers({"Content-Type: application/json","Accept:  application/json"})
    @POST(ConstantUrl.API_FEED_BACK)
    Observable<FeedbackResultBean> feedback(@Header("X-AUTH-TOKEN")String token, @Body FeedbackBean bean);

    @Headers({"Content-Type: application/json","Accept:  application/json"})
    @POST(ConstantUrl.API_EQUIPMENT_FAULT)
    Observable<EquipmentFaultResultBean> equipmentFault(@Header("X-AUTH-TOKEN")String token, @Body EquipmentFaultBean bean);
}
