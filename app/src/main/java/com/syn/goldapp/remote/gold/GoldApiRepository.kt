package com.syn.goldapp.remote.gold

import com.syn.goldapp.remote.MainRetrofitService
import com.syn.goldapp.remote.dataModel.GoldModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * GoldApiRepository وظیفه مدیریت فراخوانی متدهای GoldApiService
 * و دریافت پاسخ را بر عهده دارد. این کلاس به صورت Singleton پیاده‌سازی شده
 * تا در سراسر برنامه تنها یک نمونه از آن وجود داشته باشد.
 */
class GoldApiRepository private constructor() {

    companion object {
        // نگهدارنده Singleton
        private var apiRepository: GoldApiRepository? = null

        // متد کمکی برای دسترسی به Singleton
        val instance: GoldApiRepository
            get() {
                if (apiRepository == null) {
                    apiRepository = GoldApiRepository()
                }
                return apiRepository!!
            }
    }

    /**
     * متد getGold درخواست مربوط به قیمت طلا و ارز را صدا می‌زند.
     * @param request کال‌بکی که نتیجه درخواست را مدیریت می‌کند.
     */
    fun getGold(request: GoldRequest) {

        // فراخوانی متد getGold در GoldApiService
        MainRetrofitService.goldApiService.getGold().enqueue(object : Callback<GoldModel> {
            override fun onResponse(call: Call<GoldModel>, response: Response<GoldModel>) {
                // چک می‌کنیم که موفق بوده باشد و body تهی نباشد
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    request.onSuccess(data)
                } else {
                    request.onNotSuccess("Not Success!")
                }
            }

            override fun onFailure(call: Call<GoldModel>, t: Throwable) {
                // در صورت بروز خطای شبکه یا هر خطای دیگری
                request.onError("Error:  ${t.message}")
            }

        })

    }
}
