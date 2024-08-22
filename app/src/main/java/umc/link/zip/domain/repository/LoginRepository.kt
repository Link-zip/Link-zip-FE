package umc.link.zip.domain.repository

import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.request.RefreshRequest
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.domain.model.login.JwtModel
import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.domain.model.login.NameCheckModel
import umc.link.zip.domain.model.login.RefreshModel
import umc.link.zip.domain.model.login.TokenModel
import umc.link.zip.util.network.NetworkResult

interface LoginRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginModel>

    suspend fun checkJwt(): NetworkResult<JwtModel>

    suspend fun nameCheck(nickname: String): NetworkResult<NameCheckModel>

    suspend fun signup(request: SignupRequest): NetworkResult<TokenModel>

   /* suspend fun refresh(request: RefreshRequest): NetworkResult<RefreshModel>*/
}
