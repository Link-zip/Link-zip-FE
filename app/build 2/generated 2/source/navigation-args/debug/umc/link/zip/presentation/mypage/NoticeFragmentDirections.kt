package umc.link.zip.presentation.mypage

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class NoticeFragmentDirections private constructor() {
  public companion object {
    public fun actionNoticeFragmentToNoticeDetailFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_noticeFragment_to_noticeDetailFragment)
  }
}
