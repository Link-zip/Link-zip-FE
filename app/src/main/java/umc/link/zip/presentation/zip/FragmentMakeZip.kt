package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        // Get the ImageView that will change drawable
        val fragmentMakezipExzipIc: ImageView = view.findViewById(R.id.fragment_makezip_exzip_ic)

        // Set click listeners for each rectangle
        view.findViewById<View>(R.id.rectangle_1).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_1)
        }

        view.findViewById<View>(R.id.rectangle_2).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_2)
        }

        view.findViewById<View>(R.id.rectangle_3).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_3)
        }

        view.findViewById<View>(R.id.rectangle_4).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_4)
        }

        view.findViewById<View>(R.id.rectangle_5).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_5)
        }

        view.findViewById<View>(R.id.rectangle_6).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_6)
        }

        view.findViewById<View>(R.id.rectangle_7).setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_7)
        }
    }
}
