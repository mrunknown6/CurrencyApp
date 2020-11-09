package com.example.currencyapp.repository

import com.example.currencyapp.util.Constants
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class CurrencyDataListRepository {

    fun fetchCurrencyDataTableRows(): Elements {
        val doc = Jsoup.connect(Constants.RSS_FEED_URL).get()
        val description = doc.getElementsByTag("description")[1].text()

        return Jsoup.parse(description).select("tr")
    }
}