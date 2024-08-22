package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class CustomlinkZipFragmentDirections private constructor() {
  public companion object {
    public fun actionCustomlinkZipFragmentToCustomlinkCustomFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customlinkZipFragment_to_customlinkCustomFragment)

    public fun actionCustomlinkZipFragmentToOpenLinkFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customlinkZipFragment_to_openLinkFragment)
  }
}
