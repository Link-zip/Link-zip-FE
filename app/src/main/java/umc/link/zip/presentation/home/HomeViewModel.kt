package umc.link.zip.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.home.Link
import umc.link.zip.domain.repository.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _recentLinks = MutableLiveData<List<Link>>()
    val recentLinks: LiveData<List<Link>> get() = _recentLinks

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchRecentLinks() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getRecentLinks()
                if (response.isSuccessful) {
                    _recentLinks.value = response.body()
                } else {
                    _error.value = "Failed to fetch links: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}