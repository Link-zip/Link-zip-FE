package umc.link.zip.presentation.mypage

sealed class NicknameState {
    object Empty : NicknameState()
    data class Valid(val message: String, val color: Int) : NicknameState()
    data class Invalid(val message: String, val color: Int) : NicknameState()
}