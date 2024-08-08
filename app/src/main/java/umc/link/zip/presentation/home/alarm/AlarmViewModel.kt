package umc.link.zip.presentation.home.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.alarm.Alarm
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor() : ViewModel() {
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> get() = _alarms

    fun updateAlarmStatus(alarmId: Int) {
        viewModelScope.launch {
            val updatedAlarms = _alarms.value.map {
                if (it.id == alarmId) {
                    it.copy(alarm_status = 1) // 상태를 1로 변경
                } else {
                    it
                }
            }
            _alarms.value = updatedAlarms
        }
    }

    fun setAlarms(alarms: List<Alarm>) {
        _alarms.value = alarms
    }
}
