package com.ecloud.apps.watermeterreader.core.network.retrofit

import com.ecloud.apps.watermeterreader.core.datastore.WmrPreferencesDataSource
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.dto.GetBranchesResponse
import com.ecloud.apps.watermeterreader.core.network.dto.GetConsumptionsResponse
import com.ecloud.apps.watermeterreader.core.network.dto.GetProjectsResponse
import com.ecloud.apps.watermeterreader.core.network.dto.LoginDto
import com.ecloud.apps.watermeterreader.core.network.dto.LoginResponse
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkConsumption
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkProject
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Retrofit API declaration for Ebt Network API
 */
private interface RetrofitEbtNetworkApi {
    @Headers("No-Authentication: true")
    @GET("ajaxlogin.php")
    suspend fun login(
        @Query("userid") userid: String,
        @Query("password") password: String
    ): LoginResponse

    @GET("udp.php")
    suspend fun getProjects(
        @Query("objectcode") objectCode: String = "u_ajaxMobileGetWaterReading",
        @Query("type") type: String = "WaterReadings"
    ): GetProjectsResponse

    @GET("udp.php")
    suspend fun getConsumptions(
        @Query("docno") docno: String,
        @Query("objectcode") objectCode: String = "u_ajaxMobileGetWaterReading",
        @Query("type") type: String = "WaterReadingItems",
    ): GetConsumptionsResponse

    @Headers("No-Authentication: true")
    @HEAD("udp.php")
    suspend fun checkProjects(
        @Query("objectcode") objectCode: String = "u_ajaxMobileGetWaterReading",
        @Query("type") type: String = "WaterReadings"
    ): Response<Void>

    @GET("udp.php")
    suspend fun getBranches(
        @Query("objectcode") objectCode: String = "u_ajaxMobileGetWaterReading",
        @Query("type") type: String = "Branches",
    ): GetBranchesResponse
}

/**
 * [Retrofit] backend [WmrNetworkDataSource]
 */
@Singleton
class RetrofitWmrNetwork @Inject constructor(
    private val wmrPreferencesDataSource: WmrPreferencesDataSource,
    networkJson: Json
) : WmrNetworkDataSource {

    private val hostSelectionInterceptor = Interceptor { chain ->
        var request = chain.request()

        val baseUrl =
            runBlocking { wmrPreferencesDataSource.userDataStream.first().selectedUrl }

        val url = baseUrl.toHttpUrlOrNull()

        url?.let { httpUrl ->
            request = request.newBuilder()
                .url(
                    request.url.toString().replace(
                        "http://localhost",
                        "${httpUrl.scheme}://" +
                                "${httpUrl.host}:" +
                                "${httpUrl.port}${httpUrl.encodedPath}"
                    )
                )
                .build()
        }

        chain.proceed(request)
    }

    private val authInterceptor = Interceptor { chain ->
        var request = chain.request()

        if (request.header("No-Authentication") == null) {

            val user = runBlocking { wmrPreferencesDataSource.userDataStream.first() }

            val url = request.url.newBuilder()
                .addQueryParameter("contenttype", "json")
                .addQueryParameter("branch", user.branchCode)
                .addQueryParameter("company", user.companyCode)
                .addQueryParameter("userid", user.id)
                .build()

            request = request.newBuilder()
                .url(url)
                .build()
        }

        chain.proceed(request)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val networkApi = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(hostSelectionInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor { chain ->
                    var request = chain.request()

                    val url = request.url.newBuilder()
                        .addQueryParameter("contenttype", "json")
                        .build()

                    request = request.newBuilder().url(url).build()

                    chain.proceed(request)
                }
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BASIC)
                    }
                )
                .build()
        )
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitEbtNetworkApi::class.java)

    override suspend fun getProjects(): List<NetworkProject> =
        networkApi.getProjects().projects

    override suspend fun getConsumptions(projectCode: String): List<NetworkConsumption> =
        networkApi.getConsumptions(projectCode).consumptions

    override suspend fun login(userid: String, password: String): LoginDto =
        networkApi.login(userid, password).login.first()

    override suspend fun checkProjects(): Boolean {
        val response = networkApi.checkProjects()
        return if (response.isSuccessful) {
            isContentTypeJson(response)
        } else false
    }

    override suspend fun getBranches(): List<NetworkBranch> =
        networkApi.getBranches().branches

    private fun isContentTypeJson(response: Response<*>) =
        response.headers()["Content-Type"] == "application/json"
}
