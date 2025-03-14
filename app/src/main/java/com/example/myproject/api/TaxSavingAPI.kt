package com.example.myproject.api

import com.example.myproject.database.AllTaxDeductionClass
import com.example.myproject.database.TaxSavingProduct
import retrofit2.Call
import retrofit2.http.GET

interface TaxSavingAPI {
    @GET("/products")
    fun getAllProducts(): Call<List<TaxSavingProduct>>
}