import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMakezipBinding
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.domain.model.List.ZipLink
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.list.adapter.OpenZipItemAdapter

@AndroidEntryPoint
class FragmentOpenZip : BaseFragment<FragmentOpenzipBinding>(R.layout.fragment_openzip) {
    override fun initObserver() {}
    override fun initView() {}

    private var adapter: OpenZipItemAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val toolbarBackBtn = binding.ivOpenzipToolbarBack
        toolbarBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOpenZip_to_fragmentZip)
            Log.d("FragmentOpenZip", "Navigated to FragmentZip")
        }
    }

    private fun setupRecyclerView() {
        adapter = OpenZipItemAdapter(loadInitialData())
        binding.fragmentOpenzipItemLinkRv.adapter = adapter
    }

    private fun loadInitialData(): List<ZipLink> {
        return listOf(
            ZipLink("1", "테스트입니다1", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 1, "2024.7.28"),
            ZipLink("2", "테스트입니다2", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 1, "2024.7.28")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}
