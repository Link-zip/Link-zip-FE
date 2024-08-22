package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class OpenLinkFragmentDirections private constructor() {
  public companion object {
    public fun actionOpenLinkFragmentToCustomLinkCustomFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_openLinkFragment_to_customLinkCustomFragment)
  }
}
