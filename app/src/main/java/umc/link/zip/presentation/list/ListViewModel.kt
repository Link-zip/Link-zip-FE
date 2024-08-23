package umc.link.zip.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.home.HomeAlertExistsModel
import umc.link.zip.domain.repository.HomeRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _alertExists = MutableLiveData<NetworkResult<HomeAlertExistsModel>>()
    val alertExists: LiveData<NetworkResult<HomeAlertExistsModel>> get() = _alertExists


    fun getAlertExists() {
        viewModelScope.launch {
            _alertExists.value = homeRepository.getAlertExists()
        }
    }

}