package umc.link.zip.presentation.home.alarm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import umc.link.zip.domain.model.alarm.Alarm
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val repository: AlarmRepository) : ViewModel() {
    val alarms: StateFlow<List<Alarm>> = repository.alarms
}
