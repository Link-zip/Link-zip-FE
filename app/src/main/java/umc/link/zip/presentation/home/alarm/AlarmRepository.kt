import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import umc.link.zip.domain.model.alarm.Alarm
import javax.inject.Inject

class AlarmRepository @Inject constructor() {
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> get() = _alarms

    init {
        // 더미 데이터 설정
        _alarms.value = listOf(
                Alarm(
                        1,
                        0,
                        "original",
                        "2024-03-20T08:00:00Z",
                        "\"마이크로/나노 인플루언서 마케팅 전략\" 리마인드 알림이 도착했어요!",
                        "트렌드 파악!"
                ),
                Alarm(
                        2,
                        1,
                        "original",
                        "2024-03-20T12:00:00Z",
                        "어쩌고 저쩌고",
                        "어쩌고 메모"
                ),
                Alarm(
                        3,
                        0,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"
                ),
                Alarm(
                        4,
                        1,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"
                ),
                Alarm(
                        5,
                        0,
                        "original",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"
                ),
                Alarm(
                        6,
                        0,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"
                )
        )
    }

    fun updateAlarmStatus(alarmId: Int) {
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
