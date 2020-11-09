package com.example.currencyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyapp.*
import com.example.currencyapp.adapters.CurrencyDataListAdapter
import com.example.currencyapp.repository.CurrencyDataListRepository
import com.example.currencyapp.util.Resource
import kotlinx.android.synthetic.main.activity_currency_data_list.*

class CurrencyDataListActivity : AppCompatActivity() {

    private var adapter: CurrencyDataListAdapter? = null
    private var viewModel: CurrencyDataListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_data_list)

        setupArchitecturalComponents()
        setCurrencyDataListObserver()
        setSwipeRefreshLayoutListener()

        viewModel?.fetchCurrencyDataList()
    }

    private fun setupArchitecturalComponents() {
        val repository =
            CurrencyDataListRepository()
        val viewModelFactory =
            CurrencyDataListViewModelFactory(
                application,
                repository
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrencyDataListViewModel::class.java)
    }

    private fun setCurrencyDataListObserver() {
        viewModel?.currencyDataList?.observe(this, Observer {
            when (it) {
                is Resource.Loading -> srlContainer.isRefreshing = true
                is Resource.Success -> {
                    srlContainer.isRefreshing = false
                    rvCurrencyDataList.layoutManager = LinearLayoutManager(this)
                    adapter =
                        CurrencyDataListAdapter(it.data!!)
                    rvCurrencyDataList.adapter = adapter
                }
                is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setSwipeRefreshLayoutListener() {
        srlContainer.setOnRefreshListener {
            viewModel?.fetchCurrencyDataList()
        }
    }

}