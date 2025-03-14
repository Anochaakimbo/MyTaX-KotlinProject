package com.example.myproject.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TaxSavingProduct(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("product_id") val productId: Int,
    @Expose @SerializedName("icon_name") val iconName: String,
    @Expose @SerializedName("title") val title: String,
    @Expose @SerializedName("subtitle") val subtitle: String,
    @Expose @SerializedName("rate") val rate: String,
    @Expose @SerializedName("url") val url: String
)
