package com.example.scanner_proof_of_concept

import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.Moshi

suspend fun fetchProductInfo(upc: String): ProductInfo? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://your-flask-server/warehouse/api/v1/products/product?upc=$upc")
        .get()
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) return null
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ProductInfo::class.java)
        return adapter.fromJson(response.body!!.string())
    }
}