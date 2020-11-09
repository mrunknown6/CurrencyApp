package com.example.currencyapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.currencyapp.CurrencyDataListApplication
import com.example.currencyapp.repository.CurrencyDataListRepository
import com.example.currencyapp.models.CurrencyData
import com.example.currencyapp.util.Constants
import com.example.currencyapp.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.select.Elements

class CurrencyDataListViewModel(
    private val app: Application,
    private val repository: CurrencyDataListRepository
) : AndroidViewModel(app) {

    val currencyDataList = MutableLiveData<Resource<MutableList<CurrencyData>>>()

    fun fetchCurrencyDataList() {
        if (hasInternetConnection()) {
            CoroutineScope(Dispatchers.IO).launch {
                currencyDataList.postValue(Resource.Loading())
                val rows = repository.fetchCurrencyDataTableRows()
                currencyDataList.postValue(Resource.Success(analyseRows(rows)))
            }
        } else {
            currencyDataList.postValue(Resource.Error("No internet connection."))
        }
    }

    private fun analyseRows(rows: Elements): MutableList<CurrencyData> {
        val temp = mutableListOf<CurrencyData>()

        for (row in rows) {
            val cells = row.select("td")
            val currencyData = CurrencyData()

            for (cellIdx in 0 until cells.size) {
                when (cellIdx) {
                    0 -> currencyData.abbr = cells[cellIdx].text()
                    1 -> currencyData.title = cells[cellIdx].text()
                    2 -> currencyData.toGel = cells[cellIdx].text().toDouble()
                    3 -> {
                        Glide.with(app.applicationContext)
                            .asBitmap()
                            .load(cells[cellIdx].select("img").attr("src"))
                            .into(
                                Constants.RSS_FEED_CHANGE_BITMAP_WIDTH,
                                Constants.RSS_FEED_CHANGE_BITMAP_HEIGHT
                            )
                            .get().also {
                                currencyData.changeBitmap = it
                            }
                    }
                    4 -> currencyData.change = cells[cellIdx].text().toDouble()
                }
            }

            temp.add(currencyData)
        }

        return temp
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<CurrencyDataListApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}