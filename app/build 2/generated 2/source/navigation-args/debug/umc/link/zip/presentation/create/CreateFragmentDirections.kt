package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class CreateFragmentDirections private constructor() {
  public companion object {
    public fun actionCreateFragmentToHomeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_createFragment_to_homeFragment)

    public fun actionCreateFragmentToCustomlinkZipFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_createFragment_to_customlinkZipFragment)

    public fun actionCreateFragmentToCreateLoadingFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_createFragment_to_createLoadingFragment)
  }
}
