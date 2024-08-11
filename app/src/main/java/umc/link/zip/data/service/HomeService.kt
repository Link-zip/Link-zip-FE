package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.GET
import umc.link.zip.domain.model.home.Link

interface HomeService {
    @GET("/list/recent")
    suspend fun getRecentList(): Response<List<Link>>
}