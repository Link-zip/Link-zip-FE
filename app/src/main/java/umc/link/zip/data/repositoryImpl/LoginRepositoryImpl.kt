package umc.link.zip.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.response.JwtResponse
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.data.dto.response.LoginResponse
import umc.link.zip.data.dto.response.NameCheckResponse
import umc.link.zip.data.dto.response.SignupResponse
import umc.link.zip.data.service.LoginService
import umc.link.zip.domain.model.login.JwtModel
import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.domain.model.login.NameCheckModel
import umc.link.zip.domain.model.login.SignupModel
import umc.link.zip.domain.repository.LoginRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {

    override suspend fun login(request: LoginRequest): NetworkResult<LoginModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.login(request).awaitResponse()
                handleApi({ response }) { baseResponse: BaseResponse<LoginResponse> ->
                    baseResponse.result.toModel()
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun nameCheck(nickname: String): NetworkResult<NameCheckModel> {
        return handleApi({loginService.nameCheck(nickname)}) {response: BaseResponse<NameCheckResponse> ->
            response.result.toModel()
        }
    }

    override suspend fun signup(request: SignupRequest): NetworkResult<SignupModel> {
        return handleApi({loginService.signup(request)}) {response: BaseResponse<SignupResponse> ->
            response.result.toModel()
        }
    }


    override suspend fun checkJwt(): NetworkResult<JwtModel> {
        return withContext(Dispatchers.IO) {
            try {
                val res = loginService.checkJwt().awaitResponse()
                handleApi({ res }) { response: BaseResponse<JwtResponse> ->
                    response.result.toModel()
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

}