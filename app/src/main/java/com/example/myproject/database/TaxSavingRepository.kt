package com.example.myproject.database

import com.example.myproject.api.TaxSavingAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaxSavingRepository {
    private val api: TaxSavingAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // เปลี่ยนเป็น URL จริง
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TaxSavingAPI::class.java)
    }

    fun getAllProducts() = api.getAllProducts()
}