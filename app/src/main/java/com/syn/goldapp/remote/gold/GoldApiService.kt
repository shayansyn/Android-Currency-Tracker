package com.syn.goldapp.remote.gold

import com.syn.goldapp.remote.dataModel.GoldModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * GoldApiService اینترفیس ارتباط با سرور برای دریافت قیمت طلا، ارز و...
 * است. در حال حاضر تنها متد getGold از endpoint مربوط به "/currencies/" فراخوانی می‌کند.
 */
interface GoldApiService {

    /**
     * متد GET برای درخواست به سرور در مسیر `currencies/`.
     * پاسخ سرور در قالب یک GoldModel دریافت می‌شود.
     */
    @GET("currencies/")
    fun getGold(): Call<GoldModel>
}
