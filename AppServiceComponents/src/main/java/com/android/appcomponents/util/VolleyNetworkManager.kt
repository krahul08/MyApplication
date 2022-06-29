package com.android.appcomponents.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.appcomponents.network.interfaces.VolleyListener
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.reflect.Method
import java.net.URLEncoder


class VolleyNetworkManager constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: VolleyNetworkManager? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyNetworkManager(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    private fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    /**
    this function will return params by calling headers
     */


//    Add param for application/json, xml
    @Throws(AuthFailureError::class)
    fun getHeaders(access_token: String): Map<String, String> {
        val params: MutableMap<String, String> = HashMap()
        params["Content-Type"] = "application/json; charset=UTF-8"
        params["token"] = access_token
        return params
    }

    /**
    this function will call GET method without params

    @param
    @return Str
     */

    fun createGetWithoutParams(
        base_url: String,
//params need to be change
        sub_url: String,
        listener: VolleyListener<String>
    ) {
        val request =
            JsonArrayRequest(Request.Method.GET, base_url + sub_url, null, { response ->
                try {
                    listener.getResult(response.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                listener.getResult(error.localizedMessage)
            })
        addToRequestQueue(request)
    }

    /**
    this function will call DELETE method without params
     */
    private fun deleteRequest(
        base_url: String,
        sub_url: String, listener: VolleyListener<String>
    ) {
        val request = StringRequest(
            Request.Method.DELETE, base_url + sub_url,
            { response -> // response
                listener.getResult(response.toString())
            }
        ) { error ->
            listener.getResult(error.localizedMessage)
        }
        addToRequestQueue(request)
    }


    /**
    this function will call IMAGE REQUEST method
     */

    fun getImage(
        imageUrl: String,
        sub_url: String, listener: VolleyListener<String?>
    ) {
        val imageRequest = ImageRequest(
            imageUrl + sub_url,
            { bitmap ->
                listener.getResult(bitmap.toString())
            },
            0, // max width
            0, // max height
            Bitmap.Config.ARGB_8888, // decode config
            { error -> // error listener
                listener.getResult(error.localizedMessage)

            }
        )
        addToRequestQueue(imageRequest)
    }

    /**
    this function will call POST method with params
    and return string
     */
    fun getPostRequest(
        base_url: String,
        sub_url: String,
        param1: Any?,
        listener: VolleyListener<String?>
    ) {
        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["param1"] = param1
        val request = JsonObjectRequest(
            Request.Method.POST, base_url + sub_url, JSONObject(jsonParams),
            { response ->
                Log.d(TAG, "somePostRequest Response : $response")
                listener.getResult(response.toString())
            }
        ) { error ->
            if (null != error.networkResponse) {
                Log.d(
                    TAG,
                    "Error Response code: " + error.networkResponse.statusCode
                )
                listener.getResult(error.localizedMessage)
            }
        }
        addToRequestQueue(request)
    }


    /**
    this function will call GET method with params
    and return string
     */
    fun createGetWithParams(
        base_url: String,
        sub_url: String, params: Map<String, Any>
    ): String {
        var url: String? = base_url + sub_url
        val builder = StringBuilder()
        for (key in params.keys) {
            var value = params[key]
            if (value != null) {
                try {
                    val href = "date?July 8, 2019"
                    value = URLEncoder.encode(href, "utf-8")
                    if (builder.isNotEmpty()) builder.append("&")
                    builder.append(key).append("=").append(value)
                } catch (e: UnsupportedEncodingException) {
                }
            }
        }
        return "?" + builder.toString().let { url += it; url }
    }


    /**
    this function will call PUT method with paramsS
     */
    private fun sendPutRequest(
        base_url: String,
        sub_url: String,
        token: String,
        val1: String,
        val2: String,
        val3: String,
        listener: VolleyListener<String?>

    ) {
        /*params will come dynamic using constant */
        val jsonObject = JSONObject()
        try {
            jsonObject.put("token", token)
            jsonObject.put("val1", val1)
            jsonObject.put("val2", val2)
            jsonObject.put("val3", val3)

            val putRequest: JsonObjectRequest =
                object : JsonObjectRequest(
                    Method.PUT, base_url + sub_url, jsonObject,
                    Response.Listener { response ->
                        // response
                        listener.getResult(response.toString())

                    },
                    Response.ErrorListener { error ->
                        // error
                        Log.i("error: ", "$error")
                    }
                ) {
                    override fun getBody(): ByteArray {
                        Log.i("json", jsonObject.toString())
                        return jsonObject.toString().toByteArray(charset("UTF-8"))
                    }

                }
            addToRequestQueue(putRequest)
        } catch (e: JSONException) {
            // handle exception
            Log.i("json_error: ", "$e")
        }
    }
}



