package com.syn.goldapp.remote

import com.syn.goldapp.remote.gold.GoldApiService
import com.syn.goldapp.remote.time.TimeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * MainRetrofitService یک شیء Singleton است که وظیفه ساخت و نگهداری
 * نمونه‌های Retrofit را برای کل برنامه بر عهده دارد. هر سرویس (ApiService)
 * که لازم داریم، از طریق همین کلاس ساخته می‌شود.
 */
object MainRetrofitService {

    // آدرس Base URL سرور (مشترک برای time و gold)
    private const val BASE_URL = "https://tools.daneshjooyar.com/api/v1/"

    // ساخت نمونه Retrofit:
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // مبدل JSON به Kotlin object
        .build()

    /**
     * سرویس مختص ارتباط با متدهای مربوط به زمان (TimeApiService)
     * در اینجا ساخته و کش می‌شود.
     */
    val timeApiService: TimeApiService = retrofit.create(TimeApiService::class.java)

    /**
     * سرویس مختص ارتباط با متدهای مربوط به قیمت طلا/ارز (GoldApiService)
     * در اینجا ساخته و کش می‌شود.
     */
    val goldApiService: GoldApiService = retrofit.create(GoldApiService::class.java)
}
