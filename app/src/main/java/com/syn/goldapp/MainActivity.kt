package com.syn.goldapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.syn.goldapp.remote.dataModel.ContentModel
import com.syn.goldapp.remote.dataModel.GoldModel
import com.syn.goldapp.remote.dataModel.timeModel.DateModel
import com.syn.goldapp.remote.gold.GoldApiRepository
import com.syn.goldapp.remote.gold.GoldRequest
import com.syn.goldapp.remote.time.TimeApiRepository
import com.syn.goldapp.remote.time.TimeRequest
import com.syn.goldapp.ui.theme.*
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // نصب Splash Screen سیستمی
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // فعال‌سازی حالت EdgeToEdge
//        enableEdgeToEdge()

        // جدیدترین روش برای تغییر رنگ StatusBar و NavigationBar
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContent {
            GoldAppTheme {
                var showCustomSplashScreen by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    // نمایش اسپلش سفارشی به مدت 1.0 ثانیه
                    delay(1000)
                    showCustomSplashScreen = false
                }
                if (showCustomSplashScreen) {
                    // نمایش اسپلش سفارشی
                    SplashScreen()
                } else {
                    MainAppScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {

    // ----------------------------
    // گرفتن تاریخ (فقط یک بار)
    // ----------------------------
    var persianDate by remember { mutableStateOf("...در حال دریافت تاریخ") }
    LaunchedEffect(Unit) {
        TimeApiRepository.instance.getTime(object : TimeRequest {
            override fun onSuccess(data: DateModel) {
                val d = data.date
                persianDate = "${d.l_value} ${d.j_value} ${d.F_value} ${d.Y_value}"
            }

            override fun onNotSuccess(message: String) {
                persianDate = "خطا در دریافت تاریخ"
            }

            override fun onError(error: Throwable) {
                persianDate = "خطا در دریافت تاریخ"
            }
        })
    }

    // ----------------------------
    // گرفتن کل داده‌های طلا، ارز و کریپتو در یک فراخوانی
    // ----------------------------
    var isLoading by remember { mutableStateOf(true) }       // اگر true باشد، LoadingBox نمایش داده می‌شود
    var goldList by remember { mutableStateOf(emptyList<ContentModel>()) }
    var currencyList by remember { mutableStateOf(emptyList<ContentModel>()) }
    var cryptoList by remember { mutableStateOf(emptyList<ContentModel>()) }
    var lastUpdate by remember { mutableStateOf("") }        // مقدار last_update از JSON

    var dataFetched by remember { mutableStateOf(false) }    // آیا داده‌ها Fetch شده‌اند یا نه

    // فراخوانی سرور
    LaunchedEffect(Unit) {
        GoldApiRepository.instance.getGold(object : GoldRequest {
            override fun onSuccess(data: GoldModel) {
                // Parse تمام داده‌ها از یک بار پاسخ
                goldList = data.data.golds ?: emptyList()
                currencyList = data.data.currencies ?: emptyList()
                cryptoList = data.data.cryptocurrencies ?: emptyList()
                lastUpdate = data.lastUpdate
                dataFetched = true
                isLoading = false
            }

            override fun onNotSuccess(message: String) {
                // می‌توانید پیام خطا ثبت کنید
                dataFetched = true
                isLoading = false
            }

            override fun onError(error: String) {
                // می‌توانید پیام خطا ثبت کنید
                dataFetched = true
                isLoading = false
            }
        })
    }

    // ترتیب تب‌ها: 0 -> ارز دیجیتال، 1 -> ارز، 2 -> طلا (پیش‌فرض)
    var selectedTabIndex by remember { mutableIntStateOf(2) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BackgroundBlack)
        ) {
            // بالای صفحه (تاریخ + آخرین آپدیت)
            TopDateBox(
                persianDate = persianDate,
                lastUpdate = if (dataFetched) lastUpdate else "" // اگر داده Fetch نشده، آخرین آپدیت خالی باشد
            )

            // سکشن تب‌ها
            TabsSection(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { newIndex -> selectedTabIndex = newIndex }
            )

            // اگر در حال بارگیری باشیم، فقط Progress نمایش داده شود
            if (isLoading) {
                LoadingBox()
            } else {
                // بدنه تب‌ها (دیگر فراخوانی جدید نداریم!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(BackgroundBlack)
                ) {
                    when (selectedTabIndex) {
                        0 -> CryptoListScreen(cryptoList)
                        1 -> CurrencyListScreen(currencyList)
                        2 -> GoldListScreen(goldList)
                    }
                }
            }
        }
    }
}

/** باکس بالای صفحه که تاریخ شمسی و lastUpdate را نمایش می‌دهد */
@Composable
fun TopDateBox(persianDate: String, lastUpdate: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_top_main),
            contentDescription = "Background for dates",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // تاریخ (Bold و وسط‌چین)
            Text(
                text = persianDate,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // اگر مقدار lastUpdate خالی نباشد، نمایش می‌دهیم
            if (lastUpdate.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "آخرین بروزرسانی: $lastUpdate",
                    color = TextGrey,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * سه تب: [0 -> ارز دیجیتال], [1 -> ارز], [2 -> طلا]
 * طلا پیش‌فرض انتخابی است (selectedTabIndex=2).
 */
@Composable
fun TabsSection(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(GoldText.copy(alpha = 0.19f))
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),


            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // تب‌ها
            listOf("ارز دیجیتال", "قیمت ارز", "قیمت طلا").forEachIndexed { index, title ->
                Tab(

                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                        .padding(6.dp)
                        .clip(MaterialTheme.shapes.medium)

                    ,
                    text = {
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) GoldText else Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )

                // نمایش Divider بین تب‌ها (به جز بعد از آخری)
                if (index < 2) {
                    VerticalDivider(
                        color = DividerGrey,
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                    )
                }
            }
        }
    }
}


/** ProgressBar و متن فارسی وسط‌چین در مرکز صفحه */
@Composable
fun LoadingBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = GoldText)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "...در حال دریافت اطلاعات",
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** پیغام «داده‌ای یافت نشد» با متن وسط‌چین */
@Composable
fun NoDataBox(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** ---------------------------
 *  نمایش تب ارز دیجیتال
 *  --------------------------- */
@Composable
fun CryptoListScreen(cryptoList: List<ContentModel>) {
    // اگر لیست خالی است، پیام «هیچ داده‌ای» بده
    if (cryptoList.isEmpty()) {
        NoDataBox("!هیچ داده‌ای برای ارز دیجیتال موجود نیست\n.لطفا اینترنت خود را بررسی کنید")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp) // <-- اضافه کردن این خط
        ) {
            items(cryptoList) { item ->
                // اگر بیت‌کوین باشد، همان عدد سرور را نمایش بده
                if (item.name == "bitcoin") {
                    BitcoinItemRow(item.label, item.price)
                } else {
                    // مابقی ارزها (تتر و ...) تقسیم بر 10 می‌شوند
                    PriceItemRow(item.label, item.name, item.price)
                }
            }
        }
    }
}

/** ---------------------------
 *  نمایش تب ارز
 *  --------------------------- */
@Composable
fun CurrencyListScreen(currencyList: List<ContentModel>) {
    if (currencyList.isEmpty()) {
        NoDataBox("!هیچ داده‌ای برای ارز موجود نیست\n.لطفا اینترنت خود را بررسی کنید")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp) // <-- اضافه کردن این خط
        ) {
            items(currencyList) { item ->
                PriceItemRow(item.label, item.name, item.price)
            }
        }
    }
}

/** ---------------------------
 *  نمایش تب طلا
 *  --------------------------- */
@Composable
fun GoldListScreen(goldList: List<ContentModel>) {
    if (goldList.isEmpty()) {
        NoDataBox("!هیچ داده‌ای برای طلا موجود نیست\n.لطفا اینترنت خود را بررسی کنید")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp) // <-- اضافه کردن این خط
        ) {
            items(goldList) { item ->
                PriceItemRow(item.label, item.name, item.price)
            }
        }
    }
}

/** ---------------------------
 *  آیتم بیت‌کوین
 *  --------------------------- */
@Composable
fun BitcoinItemRow(label: String, bitcoinPrice: Int) {
    val finalPriceString = rawNumberToSeparatedString(bitcoinPrice) // سه‌رقم‌سه‌رقم جدا کردن
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // سمت چپ: [آیکون دلار + مبلغ]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_dlr),
                contentDescription = "دلار",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(GoldText)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = finalPriceString,
                color = GoldText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        // سمت راست: [label] + آیکون بیت‌کوین
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Right
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(GoldText.copy(alpha = 0.19f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bitcoin),
                    contentDescription = "bitcoin",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/** ---------------------------
 *  آیتم‌های غیر بیت‌کوین
 *  --------------------------- */
@Composable
fun PriceItemRow(label: String, name: String, price: Int) {
    val formattedPrice = price.toTomanString() // ریال -> تومان
    val iconRes = getIconResourceByName(name)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // سمت چپ: [آیکون تومان + مبلغ]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_tmn),
                contentDescription = "تومان",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(GoldText)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formattedPrice,
                color = GoldText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        // سمت راست: [label] + آیکون
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Right
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(GoldText.copy(alpha = 0.19f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/** انتخاب آیکون سمت راست بر اساس name (به‌جز بیت‌کوین که جدا هندل می‌کنیم) */
fun getIconResourceByName(name: String): Int {
    return when (name) {
        // طلا
        "gold_18" -> R.drawable.ic_18
        "gold_24" -> R.drawable.ic_24
        "coin", "half_coin", "quarter_coin" -> R.drawable.ic_gold
        // ارز
        "dollar" -> R.drawable.ic_dollar
        "derham" -> R.drawable.ic_derham
        "pound" -> R.drawable.ic_pond
        "euro" -> R.drawable.ic_euro
        // ارز دیجیتال (به جز بیت‌کوین که جدا هندل شده)
        "tether" -> R.drawable.ic_tether
        else -> R.drawable.ic_gold
    }
}

/** تبدیل عدد ریال به تومان با تقسیم بر 10 و سه‌رقم سه‌رقم کردن. */
fun Int.toTomanString(): String {
    val tomans = this / 10
    val nf: NumberFormat = DecimalFormat("#,###")
    return nf.format(tomans)
}

/** جدا کردن عدد به صورت سه‌رقم سه‌رقم، بدون تقسیم بر 10. (برای بیت‌کوین) */
fun rawNumberToSeparatedString(raw: Int): String {
    val nf: NumberFormat = DecimalFormat("#,###")
    return nf.format(raw)
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, locale = "fa")
@Composable
fun MainAppScreenPreview() {
    GoldAppTheme {
        MainAppScreen()
    }
}
