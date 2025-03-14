package com.example.myproject.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaxSavingViewModel : ViewModel() {
    private val repository = TaxSavingRepository()
    private val _products = MutableLiveData<List<TaxSavingProduct>>()
    val products: LiveData<List<TaxSavingProduct>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchProducts() {
        _isLoading.value = true
        _error.value = null

        repository.getAllProducts().enqueue(object : Callback<List<TaxSavingProduct>> {
            override fun onResponse(call: Call<List<TaxSavingProduct>>, response: Response<List<TaxSavingProduct>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _products.value = response.body()
                } else {
                    _error.value = "เกิดข้อผิดพลาด: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<TaxSavingProduct>>, t: Throwable) {
                _isLoading.value = false
                _error.value = "เกิดข้อผิดพลาด: ${t.message}"
            }
        })
    }
}