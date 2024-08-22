package umc.link.zip.data

import android.content.Context
import android.content.SharedPreferences
import umc.link.zip.domain.model.login.TokenModel

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID_KEY = "user_id"
        private const val USER_ID_KEY_EXPIRES = "user_id_expires"
        private const val USER_ID_KEY_REFRESH = "user_id_refresh"
        private const val USER_ID_KEY_REFRESH_EXPIRES = "user_id_refresh_expires"
    }

    fun saveAccessToken(accessToken: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, accessToken)
            apply()
        }
    }

    fun saveAccessTokenExpires(accessTokenExpires: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY_EXPIRES, accessTokenExpires)
            apply()
        }
    }

    fun saveRefreshToken(refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY_REFRESH, refreshToken)
            apply()
        }
    }

    fun saveRefreshTokenExpires(refreshTokenExpires: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY_REFRESH_EXPIRES, refreshTokenExpires)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun getUserIdExpires(): String? {
        return sharedPreferences.getString(USER_ID_KEY_EXPIRES, null)
    }

    fun getUserIdRefresh(): String? {
        return sharedPreferences.getString(USER_ID_KEY_REFRESH, null)
    }

    fun deleteUserId() {
        with(sharedPreferences.edit()) {
            remove(USER_ID_KEY)
            remove(USER_ID_KEY_EXPIRES)
            remove(USER_ID_KEY_REFRESH)
            remove(USER_ID_KEY_REFRESH_EXPIRES)
            apply()
        }
    }
}
