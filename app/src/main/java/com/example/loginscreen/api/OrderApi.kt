package com.example.loginscreen.api

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.models.Order
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class OrderApi {

    companion object {
        internal fun getAll(context: Context) : Array<Order>
        {
            val orderList = arrayListOf<Order>()
            val userid = context.getSharedPreferences("UrbanCloset",AppCompatActivity.MODE_PRIVATE).getInt("UserID",0)
            val url = URL("${Productapi.API_URL}/getorder?userid=${userid}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val ordersJson = responseJson.getJSONArray("Orders")

                var i = 0
                while (i < ordersJson.length()) {
                    val orderJson = ordersJson.getJSONObject(i)
                    var ordernumber = i
                    ordernumber = ordernumber+1
                    val orderdata = Order(
                        orderJson.getInt("orderid"),
                        orderJson.getInt("TotalPrice"),
                        orderJson.getString("StatusName"),
                        orderJson.getString("Date"),
                        ordernumber
                    )
                    orderList.add(orderdata)
                    i++
                }
            }
            return orderList.toTypedArray()
        }
    }

}