package umc.link.zip.presentation.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentProfilesetcompletedBinding
import umc.link.zip.presentation.MainActivity
import umc.link.zip.presentation.base.BaseFragment

@RequiresApi(Build.VERSION_CODES.P)
@AndroidEntryPoint
class ProfilesetCompletedFragment : BaseFragment<FragmentProfilesetcompletedBinding>(R.layout.fragment_profilesetcompleted){
    override fun initObserver() {

    }


    override fun initView() {
        setClickListener()
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        setTextView()
    }

    private fun setClickListener() {
        binding.btnProfilesetcompletedStart.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setTextView() {
        val nickname = arguments?.getString("nickname") ?: "익명"
        val spannableString = SpannableString("$nickname 님, Link.zip에\n오신 걸 환영해요!")

        val myTypeface = Typeface.create(
            ResourcesCompat.getFont(requireContext(), R.font.pretendard_semibold),
            Typeface.BOLD
        )
        spannableString.setSpan(
            TypefaceSpan(myTypeface),
            0,
            nickname.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvProfilesetcompletedWelcome.text = spannableString
    }
}