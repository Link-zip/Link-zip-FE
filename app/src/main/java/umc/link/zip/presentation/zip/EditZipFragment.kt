package umc.link.zip.presentation.zip

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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.databinding.FragmentEditzipBinding
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.OpenZipEditViewModel
import umc.link.zip.util.network.NetworkResult

@AndroidEntryPoint
class EditZipFragment : BaseFragment<FragmentEditzipBinding>(R.layout.fragment_editzip) {

    private val _editResponse = MutableStateFlow<NetworkResult<ZipEditModel>?>(null)
    val editResponse: MutableStateFlow<NetworkResult<ZipEditModel>?> get() = _editResponse

    private val editZipViewModel: OpenZipEditViewModel by viewModels()
    private lateinit var selectedColor: String
    private var textentered : Boolean = false

    private val zipId: Int by lazy {
        arguments?.getInt("zipId") ?: throw IllegalStateException("zipId is required")
    }

    override fun initObserver() {
    }

    override fun initView() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMakezipExzipIc: ImageView = binding.fragmentMakezipExzipIc
        val zipNameEnterTv = binding.zipNameEnterTv
        val fragmentMakezipDeleteIc = binding.fragmentMakezipDeleteIc
        val fragmentMakezipCntTxt = binding.fragmentMakezipCntTxt
        val fragmentMakezipMakeBtn = binding.fragmentMakezipMakeBtn
        val ivProfilesetGrayshadow = binding.ivProfilesetGrayshadow
        val ivProfilesetBlueshadow = binding.ivProfilesetBlueshadow

        selectedColor = "yellow"

        // Set click listeners for each rectangle and store the selected color
        view.findViewById<View>(R.id.rectangle_1)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_1)
            selectedColor = "yellow"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_2)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_2)
            selectedColor = "lightgreen"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_3)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_3)
            selectedColor = "green"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_4)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_4)
            selectedColor = "lightblue"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_5)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_5)
            selectedColor = "blue"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_6)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_6)
            selectedColor = "darkpurple"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
        view.findViewById<View>(R.id.rectangle_7)?.setOnClickListener {
            fragmentMakezipExzipIc.setImageResource(R.drawable.ic_zip_shadow_7)
            selectedColor = "purple"
            Log.d("MakeZipFragment", "selectedColor = ${selectedColor}")
        }
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show delete icon when text is entered
                fragmentMakezipDeleteIc.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                // Use customLength function to get the correct character count
                val length = customLength(s?.toString())
                val characterCount = "$length/30"
                val spannableString = SpannableString(characterCount)
                if (length >= 1) {
                    textentered = true
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
                    fragmentMakezipMakeBtn.visibility = View.GONE
                } else {
                    // Keyboard is closed
                    fragmentMakezipMakeBtn.visibility = View.VISIBLE
                    if (zipNameEnterTv.text.isNullOrEmpty()) {
                        fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill) // Original drawable
                    } else {
                        fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
                    }
                    ivProfilesetGrayshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.VISIBLE else View.GONE
                    ivProfilesetBlueshadow.visibility = if (zipNameEnterTv.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }
        })

        val toolbarBackkBtn = binding.ivEditzipToolbarBack
        toolbarBackkBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentEditZip_to_fragmentOpenZip)
            Log.d("MakeZipFragment", "Navigated to FragmentOpenZip")
        }

        // Add item and navigate to FragmentZip on button click
        fragmentMakezipMakeBtn.setOnClickListener {
            val title = zipNameEnterTv.text.toString().trim() // trim()을 사용하여 공백을 제거하고 검사

            if (title.isEmpty()) {
                // title이 비어있을 경우 로그 출력하고 더 이상 진행하지 않음
                Log.d("EditZipFragment", "title is empty")
                Toast.makeText(context, "Title is empty.", Toast.LENGTH_SHORT).show()
            } else {
                // title이 비어있지 않은 경우 정상적으로 처리
                val request = ZipEditRequest(zipId, title, selectedColor)
                editZipViewModel.editZip(request)

                findNavController().navigate(R.id.action_fragmentEditZip_to_fragmentZip)
                Log.d("EditZipFragment", "selectedColor = $selectedColor, title = $title")
                Log.d("EditZipFragment", "Navigated to FragmentZip")
            }

            // createResponse 상태를 관찰하여 결과를 처리
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    editZipViewModel.editResponse.collectLatest { response ->
                        Log.d("ZipCreateViewModel_MakeZip", "API Response: $response")
                    }
                }
            }
        }

    }

}
