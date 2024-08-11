package umc.link.zip.domain.repository

import retrofit2.Response
import umc.link.zip.domain.model.home.Link

interface HomeRepository {
    suspend fun getRecentLinks(): Response<List<Link>>
}