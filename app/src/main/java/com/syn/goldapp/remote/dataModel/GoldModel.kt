package com.syn.goldapp.remote.dataModel

import com.google.gson.annotations.SerializedName

/**
 * دیتاکلاسی GoldModel مطابق با پاسخ JSON سرور که ساختار زیر را دارد:
 *
 * {
 *   "success": 1,
 *   "message": "...",
 *   "last_update": "...",
 *   "source": "...",
 *   "data": {
 *       "golds": [...],
 *       "currencies": [...],
 *       "cryptocurrencies": [...]
 *   }
 * }
 */
data class GoldModel(
    val success: Int,
    val message: String,

    // در خروجی JSON، last_update وجود دارد و اینجا با SerializedName هماهنگ می‌کنیم
    @SerializedName("last_update")
    val lastUpdate: String,

    val source: String,

    // داخل فیلد data، لیست‌های مختلفی مثل golds، currencies و cryptocurrencies وجود دارد
    val data: AllData
)

/**
 * AllData کلاس نگهدارنده برای فیلد data در JSON فوق. سه لیست داخل آن وجود دارد:
 * golds، currencies و cryptocurrencies
 */
data class AllData(

    // چون در JSON اسمش "golds" است
    @SerializedName("golds")
    val golds: List<ContentModel>?,

    // چون در JSON اسمش "currencies" است
    @SerializedName("currencies")
    val currencies: List<ContentModel>?,

    // اگر قصد استفاده از ارزهای دیجیتال هم داشتید، این را نگهدارید
    // در JSON اسمش "cryptocurrencies" است و ساختارش مانند ContentModel است
    @SerializedName("cryptocurrencies")
    val cryptocurrencies: List<ContentModel>?
)

/**
 * ContentModel کلاس پایه برای آیتم‌های طلا، ارز و کریپتو.
 * در JSON ساختاری شبیه زیر داریم:
 * {
 *   "name": "dollar",
 *   "label": "دلار",
 *   "price": 888800,
 *   "symbol": "$"
 * }
 *
 * در صورت نیاز می‌توانید فیلد symbol را هم اضافه کنید.
 */
data class ContentModel(
    val name: String,
    val label: String,
    val price: Int
    // اگر می‌خواهید فیلد symbol در currencies را ذخیره کنید، اضافه کنید:
    // val symbol: String?
)
