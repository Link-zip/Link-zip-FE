package umc.link.zip.presentation.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import umc.link.zip.domain.model.create.Link
import umc.link.zip.domain.model.create.Zip
import javax.inject.Inject

@HiltViewModel
class CustomLinkViewModel @Inject constructor() : ViewModel() {

    private val _link = MutableStateFlow<Link>(
            Link(
                    id = "",
                    title = "",
                    url = "",
                    text = "",
                    thumbnail = "",
                    likes = 0,
                    createdAt = "",
                    zip = Zip(id = "", title = "", color = "")
            )
    )


}
