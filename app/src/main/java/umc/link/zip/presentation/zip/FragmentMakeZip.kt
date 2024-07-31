package com.example.app

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        // Get the EditText, TextView, and ImageView
        val zipNameEnterTv: EditText = view.findViewById(R.id.zip_name_enter_tv)
        val fragmentMakezipZipNameTv: TextView = view.findViewById(R.id.fragment_makezip_zip_name_tv)
        val fragmentMakezipCntTxt: TextView = view.findViewById(R.id.fragment_makezip_cnt_txt)
        val fragmentMakezipDeleteIc: ImageView = view.findViewById(R.id.fragment_makezip_delete_ic)
        val fragmentZipMakeBtn: View = view.findViewById(R.id.fragment_zip_make_btn)
        val ivProfilesetGrayshadow: ImageView = view.findViewById(R.id.iv_profileset_grayshadow)
        val ivProfilesetBlueshadow: ImageView = view.findViewById(R.id.iv_profileset_blueshadow)

        // Add TextWatcher to EditText
        zipNameEnterTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show delete icon when text is entered
                fragmentMakezipDeleteIc.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                // Update character count and change text color if length is 1 or more
                val characterCount = "${s?.length}/15"
                val spannableString = SpannableString(characterCount)
                if (s?.length ?: 0 >= 1) {
                    val color = ContextCompat.getColor(requireContext(), R.color.color1191ad)
                    spannableString.setSpan(
                        ForegroundColorSpan(color),
                        0,
                        s?.length.toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                fragmentMakezipCntTxt.text = spannableString
            }

            override fun afterTextChanged(s: Editable?) {
                // Update the TextView with up to the first 5 characters
                val text = s.toString()
                fragmentMakezipZipNameTv.text = text.take(5)
            }
        })

        // Delete text when delete icon is clicked
        fragmentMakezipDeleteIc.setOnClickListener {
            zipNameEnterTv.text.clear()
        }

        // Set up touch listener to hide keyboard
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                zipNameEnterTv.clearFocus()
            }
            false
        }

        // Hide button when keyboard is shown
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                view.getWindowVisibleDisplayFrame(r)
                val screenHeight = view.rootView.height

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                val keypadHeight = screenHeight - r.bottom

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is opened
                    fragmentZipMakeBtn.visibility = View.GONE
                } else {
                    // Keyboard is closed
                    fragmentZipMakeBtn.visibility = View.VISIBLE
                    if (zipNameEnterTv.text.isNullOrEmpty()) {
                        fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill) // Original drawable
                    } else {
                        fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
                    }
                    ivProfilesetGrayshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.VISIBLE else View.GONE
                    ivProfilesetBlueshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }
        })
    }
}
