package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.response.LoginResponse
import umc.link.zip.data.service.LoginService
import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.domain.repository.LoginRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun login(request: LoginRequest): NetworkResult<LoginModel> {
        return handleApi({loginService.login(request)}) { response: BaseResponse<LoginResponse> ->
            response.data.toModel()
        }
    }

}