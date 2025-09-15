package com.syn.goldapp.remote.time

import com.syn.goldapp.remote.MainRetrofitService
import com.syn.goldapp.remote.dataModel.timeModel.DateModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimeApiRepository private constructor() {
    companion object {

        private var apiRepository: TimeApiRepository? = null

        val instance: TimeApiRepository
            get() {
                if (apiRepository == null) apiRepository = TimeApiRepository()
                return apiRepository!!
            }

    }

    fun getTime(
        request: TimeRequest
    ) {

        MainRetrofitService.timeApiService.getTime(true).enqueue(

            object : Callback<DateModel> {
                override fun onResponse(call: Call<DateModel?>, response: Response<DateModel?>) {

                    if (response.isSuccessful) {
                        val data = response.body() as DateModel
                        request.onSuccess(data)
                    } else
                        request.onNotSuccess("Not Success!")
                }

                override fun onFailure(call: Call<DateModel?>, t: Throwable) {
                    request.onError(t)
                }

            }

        )

    }

}