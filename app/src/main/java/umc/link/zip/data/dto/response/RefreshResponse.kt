package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.RefreshModel

data class RefreshResponse(
    val accessToken: String,
    val accessTokenExpiresAt: String
) {
    fun toModel() = RefreshModel(
        accessToken = this.accessToken,
        accessTokenExpiresAt = this.accessTokenExpiresAt
    )
}
