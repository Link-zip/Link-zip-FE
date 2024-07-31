package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import umc.link.zip.R

class FragmentMakeZip : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_makezip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // iv_openzip_toolbar_back 클릭 리스너 설정
        val backButton: View = view.findViewById(R.id.iv_openzip_toolbar_back)
        backButton.setOnClickListener {
            // FragmentZip으로 돌아감
            findNavController().navigateUp()
        }
    }
}

