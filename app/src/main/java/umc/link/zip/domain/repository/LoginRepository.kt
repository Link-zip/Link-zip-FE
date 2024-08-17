package umc.link.zip.domain.repository

import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.domain.model.login.JwtModel
import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.util.network.NetworkResult

interface LoginRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginModel>

    suspend fun checkJwt(): NetworkResult<JwtModel>
}