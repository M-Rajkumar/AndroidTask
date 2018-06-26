package com.android.task.androidtask

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by hai on 6/26/2018.
 */
interface APIInterface {

    @GET("api/users?")
    fun GetDetails(@Query("page") pCount: String): Call<ResponseBody>
}