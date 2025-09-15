package com.syn.goldapp.remote.gold

import com.syn.goldapp.remote.dataModel.GoldModel

/**
 * GoldRequest یک اینترفیس (Callback) است که نتیجه فراخوانی
 * GoldApiService را مدیریت می‌کند.
 */
interface GoldRequest {

    /**
     * زمانی که پاسخ سرور موفقیت‌آمیز باشد و body معتبری دریافت کنیم،
     * این متد فراخوانی می‌شود و دادهٔ GoldModel را برمی‌گرداند.
     */
    fun onSuccess(data: GoldModel)

    /**
     * زمانی که پاسخ سرور موفق نباشد (مثلاً status code ناموفق باشد)
     * یا پاسخی دریافت شود که نا‌معتبر باشد، این متد را صدا می‌زنیم.
     */
    fun onNotSuccess(message: String)

    /**
     * زمانی که کلاینت (مثلاً به دلیل مشکل اینترنت یا قطعی سرور) به خطا
     * برخورد کند، این متد را فراخوانی می‌کنیم.
     */
    fun onError(error: String)
}
