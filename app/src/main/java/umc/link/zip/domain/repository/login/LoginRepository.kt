package umc.link.zip.domain.repository.login

import umc.link.zip.domain.model.login.NameCheckModel
import umc.link.zip.util.network.NetworkResult

interface LoginRepository {
    suspend fun nameCheck(nickname: String): NetworkResult<NameCheckModel>
}