package umc.link.zip.presentation.mypage

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.databinding.FragmentMypageProfileBinding
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.KeyboardUtil
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener
import umc.link.zip.util.extension.takeWhileIndexed
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class MypageProfileFragment : BaseFragment<FragmentMypageProfileBinding>(R.layout.fragment_mypage_profile) {
    private val viewModel: MypageProfileViewModel by viewModels()
    private val navigator by lazy { findNavController() }

    override fun initObserver() {
        repeatOnStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.nicknameState.collect { state ->
                    when (state) {
                        UiState.Empty -> hideNickNameInfo()
                        is UiState.Error ->  // 에러 상태 처리
                            Log.e("CheckNicknm", "Error fetching data", state.error)
                        UiState.Loading -> Log.d("CheckNicknm", "Loading data")
                        is UiState.Success ->
                        {
                            val data = state.data as CheckNicknmModel
                            if(data.availablity){
                                setNickNameInfo("환상적인 닉네임이에요!", R.color.b005773)
                                enableSaveButton()
                            }else{
                                setNickNameInfo("이미 사용 중인 유저가 있어요!", R.color.disabled_color)
                                disableSaveButton()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        // 블러 처리
        applyBlurToImageView(binding.ivMypageProfileUserInfoBoxBg)
        applyBlurToImageView(binding.ivMypageProfileUserNicknmChangeBoxBg)

        binding.ivMypageProfileToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }
        setNickname()
        setupClickListeners()

        KeyboardUtil.registerKeyboardVisibilityListener(binding.clMypageProfile, binding.nsvMypageProfile, binding.cvMypageProfileUserInfoBoxBg, binding.cvMypageProfileUserInfoBoxBg)
    }

    private fun applyBlurToImageView(view: BlurView) {
        val window = requireActivity().window
        val radius = 16f

        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        view.outlineProvider = ViewOutlineProvider.BOUNDS
        view.setClipToOutline(true)

        view.setupWith(rootView, context?.let { RenderScriptBlur(it) }) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)
    }

    private fun setNickname() {
        binding.etMypageProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = customLength(s?.toString())
                val characterCount = "$length/20"
                val spannableString = SpannableString(characterCount)
                if (length >= 1) {
                    val color = ContextCompat.getColor(requireContext(), R.color.abled_color)
                    spannableString.setSpan(
                        ForegroundColorSpan(color),
                        0,
                        length.toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                binding.tvMypageProfileNicknmLetterCount.text = spannableString

                if (length > 20) {
                    val truncatedText = s?.toString()?.takeWhileIndexed { index, _ ->
                        customLength(s.toString().take(index + 1)) <= 20
                    }
                    binding.etMypageProfile.setText(truncatedText)
                    binding.etMypageProfile.setSelection(truncatedText?.length ?: 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                hideNickNameInfo()
                if (binding.etMypageProfile.text.isNotEmpty()) {
                    val newNickname = binding.etMypageProfile.text.toString()
                    var nowNickname = binding.tvMypageProfileNickname.text.toString()
                    nowNickname = "뉴비" // 예시
                    if (newNickname == nowNickname) {
                        setNickNameInfo("기존의 닉네임이에요!", R.color.disabled_color)
                        disableSaveButton()
                        disableCheckDuplicateButton()
                    } else {
                        enableCheckDuplicateButton()
                    }
                } else {
                    disableCheckDuplicateButton()
                    hideNickNameInfo()
                    disableSaveButton()
                }
            }
        })

        binding.etMypageProfile.filters = arrayOf(android.text.InputFilter.LengthFilter(20))

        view?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                binding.etMypageProfile.clearFocus()
            }
            false
        }
    }

    private fun fnCheckNicknmApi(nicknm:String){
        val request = CheckNicknmRequest(nicknm) //sort, filter
        viewModel.checkNickname(request)
    }

    private fun setupClickListeners() {
        //중복 확인 로직
        binding.viewMypageProfileBtnChkDup.setOnSingleClickListener {
            val newNickname = binding.etMypageProfile.text.toString()
            var nowNickname = binding.tvMypageProfileNickname.text.toString()
            nowNickname = "old" // 예시

            if (newNickname.isNotEmpty() && nowNickname != newNickname) {
                fnCheckNicknmApi(newNickname)
            }
        }
        // 완료 버튼 로직 추가 필요
        binding.viewMypageProfileBtnSave.setOnClickListener{

        }

        //회원 탈퇴
        binding.tvMypageWithdrawal.setOnSingleClickListener {
            navigator.navigate(R.id.action_mypageProfileFragment_to_mypageWithdrawalFragment)
        }

    }

    private fun customLength(s: String?): Int {
        if (s == null) return 0
        var length = 0
        for (char in s) {
            length += if (char.toString().matches(Regex("[가-힣]"))) 2 else 1
        }
        return length
    }

    private fun setNickNameInfo(text: String, textColorResId: Int) {
        binding.tvMypageProfileNicknmExp.text = text
        binding.tvMypageProfileNicknmExp.visibility = View.VISIBLE
        binding.tvMypageProfileNicknmExp.setTextColor(ContextCompat.getColor(binding.root.context, textColorResId))
    }

    private fun enableCheckDuplicateButton() {
        binding.viewMypageProfileBtnChkDup.setBackgroundResource(R.drawable.bg_mypage_profile_btn_dup_active)
        binding.viewMypageProfileBtnChkDup.isClickable = true
    }

    private fun disableCheckDuplicateButton() {
        binding.viewMypageProfileBtnChkDup.setBackgroundResource(R.drawable.bg_mypage_profile_btn_dup)
        binding.viewMypageProfileBtnChkDup.isClickable = false
    }

    private fun hideNickNameInfo() {
        binding.tvMypageProfileNicknmExp.visibility = View.GONE
    }

    private fun disableSaveButton() {
        binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.bg_mypage_profile_btn_save)
        binding.tvMypageProfileSaveBtn.setTextColor(ContextCompat.getColor(binding.root.context, R.color.b005773))
    }

    private fun enableSaveButton() {
        binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
        binding.tvMypageProfileSaveBtn.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
    }

}
