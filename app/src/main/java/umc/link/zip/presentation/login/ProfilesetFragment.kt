package umc.link.zip.presentation.login

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.databinding.FragmentProfilesetBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.colorOf
import umc.link.zip.util.extension.drawableOf
import umc.link.zip.util.network.NetworkResult

@RequiresApi(Build.VERSION_CODES.P)
@AndroidEntryPoint
class ProfilesetFragment : BaseFragment<FragmentProfilesetBinding>(R.layout.fragment_profileset){
    private var isChecked : Boolean = false
    private val viewModel : ProfilesetViewModel by viewModels()

    override fun initObserver() {
        setNameCheckObserver()
        setFinishObserver()
    }

    override fun initView() {
        setClickListener()
        setEditText()
        binding.btnProfilesetNamecheck.isClickable = false
        binding.btnProfilesetDelete.visibility = View.GONE
    }

    private fun setFinishObserver() {
        viewModel.signupResult.observe(this) { result ->
            when (result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    val fragment = ProfilesetCompletedFragment().apply {
                        arguments = Bundle().apply {
                            putString("nickname", binding.etProfilesetNickname.text.toString())
                        }
                    }
                    parentFragmentManager.commit {
                        replace(R.id.fragment_view_login, fragment)
                        addToBackStack(null)
                    }
                }
            }
        }
    }

    private fun setNameCheckObserver() {
        viewModel.nameCheckResult.observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    if(result.data.isSuccess) {
                        binding.btnProfilesetFinish.setOnClickListener(finishBtnClickListener)
                        binding.btnProfilesetFinish.background = drawableOf(R.drawable.shape_rect_8_1191ad_fill)
                        binding.ivProfilesetGrayshadow.visibility = View.GONE
                        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
                        binding.tvProfilesetResult.text = "환상적인 닉네임이에요!"
                        binding.tvProfilesetResult.setTextColor(colorOf(R.color.abled_color))
                        binding.etProfilesetNickname.background = drawableOf(R.drawable.shape_profileset_edittext_able)
                    } else {
                        binding.tvProfilesetResult.text = "이미 사용 중인 유저가 있어요!"
                        binding.tvProfilesetResult.setTextColor(colorOf(R.color.disabled_color))
                        binding.etProfilesetNickname.background = drawableOf(R.drawable.shape_profileset_edittext_disable)
                    }
                    binding.tvProfilesetResult.visibility = View.VISIBLE
                    binding.viewProfilesetMg8.visibility = View.VISIBLE
                    isChecked = true
                }

                is NetworkResult.Error -> {
                    // Handle error
                }

                is NetworkResult.Fail -> {
                    // Handle fail
                }
            }
        }
    }

    private fun setEditText() {
        binding.etProfilesetNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnProfilesetDelete.visibility =
                    if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                binding.btnProfilesetNamecheck.isClickable = !s.isNullOrEmpty()

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

                if(isChecked) {
                    isChecked = false
                    binding.btnProfilesetFinish.setOnClickListener(null)
                    binding.btnProfilesetFinish.background = drawableOf(R.drawable.shape_rect_8_666666_fill)
                    binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
                    binding.ivProfilesetBlueshadow.visibility = View.GONE
                    binding.tvProfilesetResult.visibility = View.GONE
                    binding.viewProfilesetMg8.visibility = View.GONE
                    binding.etProfilesetNickname.background = drawableOf(R.drawable.shape_profileset_edittext_default)
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etProfilesetNickname.filters = arrayOf(InputFilter.LengthFilter(30))

        view?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                binding.etProfilesetNickname.clearFocus()
            }
            false
        }
    }

    private val finishBtnClickListener = View.OnClickListener {
        val nickname = binding.etProfilesetNickname.text.toString()
        viewModel.signup(SignupRequest(
            nickname=nickname,
            key=""
        ))
    }

    private fun setClickListener() {
        binding.ivProfilesetToolbarBack.setOnClickListener {
            (activity as LoginActivity).enableLoginBtn()
            parentFragmentManager.popBackStack()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as LoginActivity).enableLoginBtn()
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


        binding.btnProfilesetDelete.setOnClickListener {
            binding.etProfilesetNickname.text.clear()
        }

        binding.btnProfilesetNamecheck.setOnClickListener {
            val nickname = binding.etProfilesetNickname.text.toString()
            viewModel.nameCheck(nickname)
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