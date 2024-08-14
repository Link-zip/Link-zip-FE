package umc.link.zip.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
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

    override suspend fun login(): NetworkResult<LoginModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.login().awaitResponse()
                handleApi({ response }) { baseResponse: BaseResponse<LoginResponse> ->
                    Log.d("login", "Repository 정상 작동")
                    baseResponse.result.toModel()
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    /*override suspend fun login(): NetworkResult<LoginModel> {
        return handleApi({loginService.login().execute()}) { response: BaseResponse<LoginResponse> ->
            Log.d("login", "Repository 정상 작동")
            response.result.toModel()
        }
    }*/

}