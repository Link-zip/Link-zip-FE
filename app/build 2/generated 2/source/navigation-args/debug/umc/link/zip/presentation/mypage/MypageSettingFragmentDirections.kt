package umc.link.zip.presentation.mypage

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class MypageSettingFragmentDirections private constructor() {
  public companion object {
    public fun actionMypageSettingFragmentToNoticeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mypageSettingFragment_to_noticeFragment)
  }
}
