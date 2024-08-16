package umc.link.zip.presentation.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import umc.link.zip.domain.repository.MypageRepository
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val repository: MypageRepository // 후에 api 연결 시 사용
) : ViewModel() {

}