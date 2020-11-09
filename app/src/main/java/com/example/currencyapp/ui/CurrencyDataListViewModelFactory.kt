package com.example.currencyapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyapp.repository.CurrencyDataListRepository

class CurrencyDataListViewModelFactory(
    private val app: Application,
    private val repositoryData: CurrencyDataListRepository
): ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyDataListViewModel(
            app,
            repositoryData
        ) as T
    }
}