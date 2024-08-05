package umc.link.zip.presentation.zip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMakezipBinding
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class FragmentZip : BaseFragment<FragmentZipBinding>(R.layout.fragment_zip) {

    private val zipViewModel: ZipViewModel by viewModels()
    private lateinit var zipAdapter: ZipAdapter
    override fun initObserver() {

    }

    override fun initView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val recyclerView: RecyclerView = binding.fragmentZipRecyclerview
        zipAdapter = ZipAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = zipAdapter

        // ViewModel의 zipItems를 관찰하여 RecyclerView 업데이트
        zipViewModel.zipItems.observe(viewLifecycleOwner, { zipItems ->
            zipAdapter.submitList(zipItems)
            Log.d("FragmentZip", "zipItems updated: $zipItems")
        })

        // Zip 만들기 버튼 설정
        val makeZipButton: Button = binding.fragmentZipMakeBtn
        makeZipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
            Log.d("FragmentZip", "Navigated to FragmentMakeZip")
        }
    }

    override fun onResume() {
        super.onResume()
        // RecyclerView 설정
        val recyclerView: RecyclerView = binding.fragmentZipRecyclerview
        zipAdapter = ZipAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = zipAdapter

        // ViewModel의 zipItems를 관찰하여 RecyclerView 업데이트
        zipViewModel.zipItems.observe(viewLifecycleOwner, { zipItems ->
            zipAdapter.submitList(zipItems)
            Log.d("FragmentZip", "zipItems updated: $zipItems")
        })

        // Zip 만들기 버튼 설정
        val makeZipButton: Button = binding.fragmentZipMakeBtn
        makeZipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
            Log.d("FragmentZip", "Navigated to FragmentMakeZip")
        }
    }
}
