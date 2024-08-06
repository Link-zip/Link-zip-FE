import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import umc.link.zip.domain.network.ApiService

object RetrofitInstance {
    private const val BASE_URL = "https://example.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
