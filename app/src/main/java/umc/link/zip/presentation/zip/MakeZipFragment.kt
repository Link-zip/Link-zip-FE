package umc.link.zip.presentation.zip

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMakezipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.ZipViewModel

@AndroidEntryPoint
class MakeZipFragment : BaseFragment<FragmentMakezipBinding>(R.layout.fragment_makezip) {

    private val zipViewModel: ZipViewModel by viewModels()
    private var selectedDrawable: Int = R.drawable.ic_zip_shadow_1
    private val navigator by lazy { findNavController() }

    override fun initObserver() {}
    override fun initView() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // iv_openzip_toolbar_back 클릭 리스너 설정
        val backButton: View = binding.ivOpenzipToolbarBack
        backButton.setOnClickListener {
            navigator.navigateUp()
        }

        // Get the ImageView that will change drawable
        val fragmentMakezipExzipIc: ImageView = binding.fragmentMakezipExzipIc

        // Set click listeners for each rectangle
        view.findViewById<View>(R.id.rectangle_1).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_1
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_2).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_2
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_3).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_3
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_4).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_4
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_5).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_5
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_6).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_6
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }
        view.findViewById<View>(R.id.rectangle_7).setOnClickListener {
            selectedDrawable = R.drawable.ic_zip_shadow_7
            fragmentMakezipExzipIc.setImageResource(selectedDrawable)
        }

        // Get the EditText, TextView, and ImageView
        val zipNameEnterTv: EditText = binding.zipNameEnterTv
        val fragmentMakezipZipNameTv: TextView = binding.fragmentMakezipZipNameTv
        val fragmentMakezipCntTxt: TextView = binding.fragmentMakezipCntTxt
        val fragmentMakezipDeleteIc: ImageView = binding.fragmentMakezipDeleteIc
        val fragmentZipMakeBtn: View = binding.fragmentZipMakeBtn
        val ivProfilesetGrayshadow: ImageView = binding.ivProfilesetGrayshadow
        val ivProfilesetBlueshadow: ImageView = binding.ivProfilesetBlueshadow

        //한글은 2글자, 영어는 1글자로 취급해 주는 함수
        fun customLength(s: String?): Int {
            if (s == null) return 0
            var length = 0
            for (char in s) {
                length += if (char.toString().matches(Regex("[가-힣]"))) 2 else 1
            }
            return length
        }
        fun CharSequence.takeWhileIndexed(predicate: (Int, Char) -> Boolean): String {
            val sb = StringBuilder()
            for ((index, element) in this.withIndex()) {
                if (!predicate(index, element)) break
                sb.append(element)
            }
            return sb.toString()
        }

        // Add TextWatcher to EditText
        zipNameEnterTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show delete icon when text is entered
                fragmentMakezipDeleteIc.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                // Use customLength function to get the correct character count
                val length = customLength(s?.toString())
                val characterCount = "$length/30"
                val spannableString = SpannableString(characterCount)
                if (length >= 1) {
                    val color = ContextCompat.getColor(requireContext(), R.color.color1191ad)
                    spannableString.setSpan(
                        ForegroundColorSpan(color),
                        0,
                        length.toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                fragmentMakezipCntTxt.text = spannableString

                // Check if the length exceeds 30 characters and cut it off if necessary
                if (length > 30) {
                    val truncatedText = s?.toString()?.takeWhileIndexed { index, _ -> customLength(s?.toString()?.take(index + 1)) <= 30 }
                    zipNameEnterTv.setText(truncatedText)
                    zipNameEnterTv.setSelection(truncatedText?.length ?: 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Update the TextView with up to the first 5 characters
                val text = s.toString()
                fragmentMakezipZipNameTv.text = text.take(5)
            }
        })

        // Set maximum length for zipNameEnterTv
        zipNameEnterTv.filters = arrayOf(android.text.InputFilter.LengthFilter(30))

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
                        fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
                    }
                    ivProfilesetGrayshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.VISIBLE else View.GONE
                    ivProfilesetBlueshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }
        })
        //여기까지가 텍스트 입력 및 키보드 내리기, 삭제 버튼 보였다가 지우기, 등등입니다.
        // Add item and navigate to FragmentZip on button click
        fragmentZipMakeBtn.setOnClickListener {
            val title = zipNameEnterTv.text.toString()
            if (title.isEmpty()) {
                // Handle the case where title is empty
                Log.d("FragmentMakeZip", "Title is empty")
                return@setOnClickListener
            }

            // Get userId from SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("user_id", "") ?: ""
            if (userId.isEmpty()) {
                // Handle the case where userId is not available
                Log.d("FragmentMakeZip", "User ID is empty")
                return@setOnClickListener
            }

            // Add item to ViewModel
            val zipItem = ZipItem(
                id = userId + System.currentTimeMillis().toString(),
                user_id = userId,
                title = title,
                color = selectedDrawable
            )
            zipViewModel.addZipItem(zipItem)
            Log.d("FragmentMakeZip", "Added ZipItem to ViewModel: $zipItem")

            // Navigate to FragmentZip
            findNavController().navigate(R.id.action_fragmentMakeZip_to_fragmentZip)
            Log.d("FragmentMakeZip", "Navigated to FragmentZip")
        }
    }
}
