package com.agarcia.myapplication.network

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class NetworkUtils {

    val MOVIES_API_BASEURL = "http://wwww.omdbapi.com/"
    val TOKEN_API = "c44a57a2"


    fun buildURL(id: String): URL? {
        val uri = Uri.parse(MOVIES_API_BASEURL)
            .buildUpon()
            .appendQueryParameter("apiker", TOKEN_API)
            .appendQueryParameter("t", id)
            .build()
        var url: URL? = null
        try {
            url = URL(uri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        return url
    }


    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream

            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            return if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}