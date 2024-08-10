package umc.link.zip.presentation.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import umc.link.zip.domain.model.create.Link
import javax.inject.Inject

@HiltViewModel
class CustomLinkViewModel @Inject constructor() : ViewModel() {

        private val _link = MutableStateFlow<Link>(
                Link(
                        zipId = 0,           // 초기값으로 0 사용
                        title = "",
                        text = "",
                        url = "",
                        memo = "",
                        alertDate = ""
                )
        )
}
