package umc.link.zip.presentation.login

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentProfilesetBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.drawableOf

@AndroidEntryPoint
class ProfilesetFragment : BaseFragment<FragmentProfilesetBinding>(R.layout.fragment_profileset){
    override fun initObserver() {

    }

    override fun initView() {
        setClickListener()
        setEditText()
    }

    private fun setEditText() {
        binding.etProfilesetNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnProfilesetDelete.visibility =
                    if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                val length = customLength(s?.toString())
                val characterCount = "$length/20"
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
                binding.tvProfilesetLetter.text = spannableString

                if (length > 20) {
                    val truncatedText = s?.toString()?.takeWhileIndexed { index, _ -> customLength(s?.toString()?.take(index + 1)) <= 20 }
                    binding.etProfilesetNickname.setText(truncatedText)
                    binding.etProfilesetNickname.setSelection(truncatedText?.length ?: 0)
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etProfilesetNickname.filters = arrayOf(android.text.InputFilter.LengthFilter(30))

        view?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                binding.etProfilesetNickname.clearFocus()
            }
            false
        }

    }

    private fun setClickListener() {
        binding.btnProfilesetFinish.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_view_login, ProfilesetCompletedFragment())
                .commit()
        }

        binding.ivProfilesetToolbarBack.setOnClickListener {
            requireActivity().supportFragmentManager.findFragmentByTag("profileset")?.let {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
        }

        binding.btnProfilesetDelete.setOnClickListener {
            binding.etProfilesetNickname.text.clear()
        }

        binding.btnProfilesetNamecheck.setOnClickListener {
            binding.btnProfilesetFinish.background = drawableOf(R.drawable.shape_rect_8_1191ad_fill)
            binding.ivProfilesetGrayshadow.visibility = View.GONE
            binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        }
    }

    private fun CharSequence.takeWhileIndexed(predicate: (Int, Char) -> Boolean): String {
        val sb = StringBuilder()
        for ((index, element) in this.withIndex()) {
            if (!predicate(index, element)) break
            sb.append(element)
        }
        return sb.toString()
    }

    fun customLength(s: String?): Int {
        if (s == null) return 0
        var length = 0
        for (char in s) {
            length += if (char.toString().matches(Regex("[가-힣]"))) 2 else 1
        }
        return length
    }

}