package umc.link.zip.presentation.create

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import kotlin.Int
import umc.link.zip.R

public class CustomlinkCustomFragmentDirections private constructor() {
  private data class ActionCustomlinkCustomFragmentToOpenLinkFragment(
    public val linkId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_customlinkCustomFragment_to_openLinkFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("linkId", this.linkId)
        return result
      }
  }

  public companion object {
    public fun actionCustomlinkCustomFragmentToCustomlinkAlarmFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customlinkCustomFragment_to_customlinkAlarmFragment)

    public fun actionCustomlinkCustomFragmentToCustomlinkMemoFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customlinkCustomFragment_to_customlinkMemoFragment)

    public fun actionCustomlinkCustomFragmentToOpenLinkFragment(linkId: Int): NavDirections =
        ActionCustomlinkCustomFragmentToOpenLinkFragment(linkId)
  }
}
