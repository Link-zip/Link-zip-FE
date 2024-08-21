package umc.link.zip.util.network

sealed interface UiState<out T> {
    object Empty : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val error: Throwable?) : UiState<Nothing>
}
