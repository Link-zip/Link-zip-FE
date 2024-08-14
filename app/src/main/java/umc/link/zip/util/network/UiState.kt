package umc.link.zip.util.network

sealed class UiState {
    data object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val error: Throwable?) : UiState()
}
