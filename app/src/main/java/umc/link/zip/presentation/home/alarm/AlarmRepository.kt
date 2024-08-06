package umc.link.zip.presentation.home.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class AlarmRepository @Inject constructor(){
    fun getAlarms(): LiveData<List<Alarm>> {
        val alarms = MutableLiveData<List<Alarm>>()
        // 더미 데이터 추가
        alarms.value = listOf(
                Alarm(1, 0, "original", "2024-03-20T08:00:00Z", "아침 알람", "아침 알람 내용입니다."),
                Alarm(2, 1, "reminder", "2024-03-20T12:00:00Z", "점심 알람", "점심 알람 내용입니다."),
                Alarm(3, 0, "original", "2024-03-20T18:00:00Z", "저녁 알람", "저녁 알람 내용입니다.")
        )
        return alarms
    }
}
