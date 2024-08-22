package umc.link.zip.presentation.mypage

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class MypageProfileFragmentDirections private constructor() {
  public companion object {
    public fun actionMypageProfileFragmentToMypageWithdrawalFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mypageProfileFragment_to_mypageWithdrawalFragment)
  }
}
