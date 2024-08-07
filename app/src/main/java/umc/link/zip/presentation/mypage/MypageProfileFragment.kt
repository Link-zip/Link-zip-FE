package umc.link.zip.presentation.mypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import jp.wasabeef.blurry.Blurry
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageProfileBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.colorOf
import umc.link.zip.util.extension.takeWhileIndexed

@AndroidEntryPoint
class MypageProfileFragment : BaseFragment<FragmentMypageProfileBinding>(R.layout.fragment_mypage_profile) {
 //   private val zipViewModel: MypageProfileViewModel by viewModels()
    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        // 블러 처리
        applyBlurToImageView(binding.ivMypageProfileUserInfoBoxBg)
        applyBlurToImageView(binding.ivMypageProfileUserNicknmChangeBoxBg)

        binding.ivMypageProfileToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }
        setNickname()
    }


    private fun applyBlurToImageView(view: BlurView) {
        val window = requireActivity().window
        val radius = 16f

        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        view.outlineProvider = ViewOutlineProvider.BOUNDS;
        view.setClipToOutline(true);

        view.setupWith(rootView, context?.let { RenderScriptBlur(it) }) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

    }

    //한글은 2글자, 영어는 1글자로 취급해 주는 함수
    private fun customLength(s: String?): Int {
        if (s == null) return 0
        var length = 0
        for (char in s) {
            length += if (char.toString().matches(Regex("[가-힣]"))) 2 else 1
        }
        return length
    }

    private fun setNickname() {
        binding.etMypageProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

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
                        customLength(
                            s?.toString()?.take(index + 1)
                        ) <= 20
                    }
                    binding.etMypageProfile.setText(truncatedText)
                    binding.etMypageProfile.setSelection(truncatedText?.length ?: 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(binding.etMypageProfile.text.isNotEmpty()) {
                    // 기존 닉네임이라면 ?
                    if (binding.etMypageProfile.text.toString() == "old") {
                        binding.tvMypageProfileNicknmExp.text = "기존의 닉네임이에요!"
                        binding.tvMypageProfileNicknmExp.visibility = View.VISIBLE
                        binding.tvMypageProfileNicknmExp.setTextColor(ContextCompat.getColor(binding.root.context, R.color.disabled_color))
                        //완료 버튼 비활성화
                        binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.bg_mypage_profile_btn_save)
                        //중복 확인 버튼 비활성화
                        binding.viewMypageProfileBtnChkDup.setBackgroundResource(R.drawable.bg_mypage_profile_btn_dup)
                    }else {
                        //중복 확인 로직 + 버튼 활성화
                        binding.viewMypageProfileBtnChkDup.setBackgroundResource(R.drawable.bg_mypage_profile_btn_dup_active)
                        binding.viewMypageProfileBtnChkDup.setOnClickListener {
                            //api 통신 후 받아오기 만약 api data를 data라 하자.
                            var data = "ok"
                            when (data) {
                                "no" -> {
                                    //설명 수정
                                    binding.tvMypageProfileNicknmExp.text = "이미 사용 중인 유저가 있어요!"
                                    binding.tvMypageProfileNicknmExp.visibility = View.VISIBLE
                                    binding.tvMypageProfileNicknmExp.setTextColor(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color.disabled_color
                                        )
                                    )
                                    //완료 버튼 비활성화
                                    binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.bg_mypage_profile_btn_save)
                                    binding.tvMypageProfileSaveBtn.setTextColor(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color.b005773
                                        )
                                    )
                                }

                                "ok" -> {
                                    //설명 수정
                                    binding.tvMypageProfileNicknmExp.text = "환상적인 닉네임이에요!"
                                    binding.tvMypageProfileNicknmExp.visibility = View.VISIBLE
                                    binding.tvMypageProfileNicknmExp.setTextColor(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color.abled_color
                                        )
                                    )
                                    //완료 버튼 활성화
                                    binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
                                    binding.tvMypageProfileSaveBtn.setTextColor(
                                        ContextCompat.getColor(
                                            binding.root.context,
                                            R.color.white
                                        )
                                    )
                                }
                            }
                        }
                    }
                }else {
                    //중복 확인 버튼 비활성화
                    binding.viewMypageProfileBtnChkDup.setBackgroundResource(R.drawable.bg_mypage_profile_btn_dup)
                    //설명 안보이게
                    binding.tvMypageProfileNicknmExp.visibility = View.GONE
                    //완료 버튼 비활성화
                    binding.viewMypageProfileBtnSave.setBackgroundResource(R.drawable.bg_mypage_profile_btn_save)
                    binding.tvMypageProfileSaveBtn.setTextColor(ContextCompat.getColor(binding.root.context, R.color.abled_color))
                }
            }

        })

        // Set maximum length for zipNameEnterTv
        binding.etMypageProfile.filters = arrayOf(android.text.InputFilter.LengthFilter(20))


        // Set up touch listener to hide keyboard
        view?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                binding.etMypageProfile.clearFocus()
            }
            false
        }
    }

}
