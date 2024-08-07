package umc.link.zip.presentation.home.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class AlarmRepository @Inject constructor(){
    fun getAlarms(): LiveData<List<Alarm>> {
        val alarms = MutableLiveData<List<Alarm>>()

        // 더미 데이터
        alarms.value = listOf(
                Alarm(
                        1,
                        0,
                        "original",
                        "2024-03-20T08:00:00Z",
                        "\"마이크로/나노 인플루언서 마케팅 전략\" 리마인드 알림이 도착했어요!",
                        "트랜드 파악!"),
                Alarm(
                        2,
                        1,
                        "original",
                        "2024-03-20T12:00:00Z",
                        "어쩌고 저쩌고",
                        "어쩌고 메모"),
                Alarm(3,
                        0,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"),
                Alarm(4,
                        1,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"),
                Alarm(5,
                        0,
                        "original",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모"),
                Alarm(6,
                        0,
                        "reminder",
                        "2024-03-20T18:00:00Z",
                        "제목제목",
                        "메모메모")
        )

        return alarms
    }
}
