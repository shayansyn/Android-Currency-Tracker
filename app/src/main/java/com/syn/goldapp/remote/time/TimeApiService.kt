package com.syn.goldapp.remote.time

import com.syn.goldapp.remote.dataModel.timeModel.DateModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeApiService {

    @GET("date/now/")
    fun getTime(
        @Query("short") short: Boolean
    ) : Call<DateModel>
}