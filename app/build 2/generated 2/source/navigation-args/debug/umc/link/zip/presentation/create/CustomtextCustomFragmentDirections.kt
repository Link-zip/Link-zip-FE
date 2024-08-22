package umc.link.zip.presentation.create

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import kotlin.Int
import umc.link.zip.R

public class CustomtextCustomFragmentDirections private constructor() {
  private data class ActionCustomtextCustomFragmentToOpenTextFragment(
    public val linkId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_customtextCustomFragment_to_openTextFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("linkId", this.linkId)
        return result
      }
  }

  public companion object {
    public fun actionCustomtextCustomFragmentToCustomtextAlarmFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customtextCustomFragment_to_customtextAlarmFragment)

    public fun actionCustomtextCustomFragmentToCustomtextMemoFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customtextCustomFragment_to_customtextMemoFragment)

    public fun actionCustomtextCustomFragmentToOpenTextFragment(linkId: Int): NavDirections =
        ActionCustomtextCustomFragmentToOpenTextFragment(linkId)
  }
}
