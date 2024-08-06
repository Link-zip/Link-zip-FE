package umc.link.zip.presentation.home.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val repository: AlarmRepository) : ViewModel() {
    val alarms: LiveData<List<Alarm>> = repository.getAlarms()
}
