package umc.link.zip.presentation.home.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.alert.Alert
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor() : ViewModel() {
    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> get() = _alerts

    fun updateAlertStatus(alertId: Int) {
        viewModelScope.launch {
            val updatedAlerts = _alerts.value.map {
                if (it.id == alertId) {
                    it.copy(alert_status = 1) // 상태를 1로 변경
                } else {
                    it
                }
            }
            _alerts.value = updatedAlerts
        }
    }

    fun setAlerts(alerts: List<Alert>) {
        _alerts.value = alerts
    }
}
