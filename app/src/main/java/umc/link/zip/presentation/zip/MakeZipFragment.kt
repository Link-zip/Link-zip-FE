package umc.link.zip.presentation.zip

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMakezipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.ZipAdapter
import umc.link.zip.presentation.zip.adapter.ZipViewModel

@AndroidEntryPoint
class MakeZipFragment : BaseFragment<FragmentMakezipBinding>(R.layout.fragment_makezip) {

    private val zipViewModel: ZipViewModel by viewModels()
    private lateinit var selectedColor: String
    override fun initObserver() {}

    override fun initView() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMakezipExzipIc: ImageView = binding.fragmentMakezipExzipIc
        val zipNameEnterTv = binding.zipNameEnterTv
        val fragmentMakezipDeleteIc = binding.fragmentMakezipDeleteIc
        val fragmentMakezipCntTxt = binding.fragmentMakezipCntTxt
        val fragmentZipMakeBtn = binding.fragmentZipMakeBtn
        val ivProfilesetGrayshadow = binding.ivProfilesetGrayshadow
        val ivProfilesetBlueshadow = binding.ivProfilesetBlueshadow

        // Set click listeners for each rectangle and store the selected color
        view.findViewById<View>(R.id.rectangle_1)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_1)
            selectedColor = "yellow"  // 예시로 'yellow'를 설정
        }
        view.findViewById<View>(R.id.rectangle_2)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_2)
            selectedColor = "lightgreen"  // 예시로 'blue'를 설정
        }
        view.findViewById<View>(R.id.rectangle_3)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_3)
            selectedColor = "green"  // 예시로 'red'를 설정
        }
        view.findViewById<View>(R.id.rectangle_4)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_4)
            selectedColor = "lightblue"  // 예시로 'red'를 설정
        }
        view.findViewById<View>(R.id.rectangle_5)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_5)
            selectedColor = "blue"  // 예시로 'red'를 설정
        }
        view.findViewById<View>(R.id.rectangle_6)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_6)
            selectedColor = "darkpurple"  // 예시로 'red'를 설정
        }
        view.findViewById<View>(R.id.rectangle_7)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_7)
            selectedColor = "purple"  // 예시로 'red'를 설정
        }
        // 색상 설정

        // 한글은 2글자, 영어는 1글자로 취급해 주는 함수
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
                binding.fragmentMakezipZipNameTv.text = text.take(5)
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

        // Add item and navigate to FragmentZip on button click
        fragmentZipMakeBtn.setOnClickListener {
            val title = zipNameEnterTv.text.toString()
            if (title.isEmpty()) {
                // Handle the case where title is empty
                Log.d("FragmentMakeZip", "Title is empty")
                return@setOnClickListener
            }

            // Zip 생성 API 호출
            zipViewModel.createZipItem(title, selectedColor)

            // Navigate to FragmentZip
            findNavController().navigate(R.id.action_fragmentMakeZip_to_fragmentZip)
            Log.d("FragmentMakeZip", "Navigated to FragmentZip")
        }
    }
}
