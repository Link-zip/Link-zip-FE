package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.RefreshModel

data class RefreshResponse(
    val accessToken: String,
    val accessTokenExpires: String
) {
    fun toModel() = RefreshModel(
        accessToken = this.accessToken,
        accessTokenExpires = this.accessTokenExpires
    )
}
