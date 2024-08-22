package umc.link.zip.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID_KEY = "user_id"
    }

    fun saveUserId(userId: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, userId)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun deleteUserId() {
        with(sharedPreferences.edit()) {
            remove(USER_ID_KEY)
            apply()
        }
    }
}
