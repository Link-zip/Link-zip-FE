package umc.link.zip.data.repositoryImpl

import retrofit2.Response
import umc.link.zip.data.service.HomeService
import umc.link.zip.domain.model.home.Link
import umc.link.zip.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeService: HomeService) : HomeRepository {
    override suspend fun getRecentLinks(): Response<List<Link>> {
        return homeService.getRecentList()
    }
}