import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.domain.model.ZipLinkItem


class OpenZipViewModel : ViewModel() {

    private val _zipLinks = MutableLiveData<List<ZipLinkItem>>()
    val zipLinks: LiveData<List<ZipLinkItem>> get() = _zipLinks

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _zipLinks.value = listOf(
            ZipLinkItem("테스트입니다1", "url1", "텍스트1", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 1, "2024.7.28"),
            ZipLinkItem("테스트입니다2", "url2", "텍스트2", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 2, "2024.7.29"),
            ZipLinkItem("테스트입니다3", "url3", "텍스트3", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 0, "2024.7.30"),
            ZipLinkItem("테스트입니다4", "url4", "텍스트4", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 4, "2024.7.31"),
            ZipLinkItem("테스트입니다5", "url5", "텍스트5", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 5, "2024.8.01"),
            ZipLinkItem("테스트입니다6", "url6", "텍스트6", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 6, "2024.8.02"),
            ZipLinkItem("테스트입니다7", "url7", "텍스트7", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 0, "2024.8.03"),
            ZipLinkItem("테스트입니다8", "url8", "텍스트8", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 8, "2024.8.04"),
            ZipLinkItem("테스트입니다9", "url9", "텍스트9", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 9, "2024.8.05"),
            ZipLinkItem("테스트입니다10", "url10", "텍스트10", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 10, "2024.8.06")
        )
    }
}
