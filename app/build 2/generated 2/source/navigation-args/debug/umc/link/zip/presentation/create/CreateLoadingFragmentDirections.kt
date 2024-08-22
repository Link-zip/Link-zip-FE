package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class CreateLoadingFragmentDirections private constructor() {
  public companion object {
    public fun actionCreateLoadingFragmentToCustomTextZipFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_createLoadingFragment_to_customTextZipFragment)
  }
}
