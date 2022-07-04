package com.android.appcomponents.network.interfaces

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface NetworkAPI {

    @GET("{url}")
    suspend fun getRequest(
        @Path(value = "url") path: String,
        @QueryMap hashMap: Map<String, String>?
    ): ResponseBody

    @FormUrlEncoded
    @POST("{url}")
    @JvmSuppressWildcards
    suspend fun postRequest(
        @Path(value = "url", encoded = true) path: String,
        @HeaderMap headerMap: Map<String, String>,
        @FieldMap hashMap: Map<String, Any>
    ): ResponseBody

    @PUT("{url}")
    suspend fun putRequest(
        @Path(value = "url") path: String,
        @Body requestBody: RequestBody
    ): ResponseBody

    @DELETE("{url}/{docId}")
    suspend fun deleteDocuments(
        @Path(value = "url") path: String,
        @Path(value = "docId") docId: String
    ): ResponseBody

    @Multipart
    @Headers("Content-Type:multipart/form-data")
    @POST("{url}")
    suspend fun postRequestWithUploadImage(
        @Path(value = "url", encoded = true) path: String,
        @HeaderMap headerMap: Map<String, String>,
        @PartMap partMap: Map<String, RequestBody>
    ): ResponseBody


    @Headers("Content-Type: application/json")
    @POST("{url}")
    suspend fun postRequestForRaw(
        @Path(value = "url", encoded = true) path: String,
        @Body requestBody: RequestBody
    ): ResponseBody
}