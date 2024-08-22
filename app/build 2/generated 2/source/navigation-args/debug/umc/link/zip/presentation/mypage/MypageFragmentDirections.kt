package umc.link.zip.presentation.mypage

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class MypageFragmentDirections private constructor() {
  public companion object {
    public fun actionMypageFragmentToMypageSettingFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mypageFragment_to_mypageSettingFragment)

    public fun actionMypageFragmentToMypageProfileFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mypageFragment_to_mypageProfileFragment)
  }
}
