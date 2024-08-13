package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.response.NameCheckResponse
import umc.link.zip.data.service.LoginService
import umc.link.zip.domain.model.login.NameCheckModel
import umc.link.zip.domain.repository.login.LoginRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun nameCheck(nickname: String): NetworkResult<NameCheckModel> {
        return handleApi({loginService.nameCheck(nickname)}) {response: NameCheckResponse ->
            response.toModel()
        }
    }
}