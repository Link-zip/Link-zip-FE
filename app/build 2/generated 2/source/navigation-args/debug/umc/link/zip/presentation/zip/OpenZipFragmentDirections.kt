package umc.link.zip.presentation.zip

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class OpenZipFragmentDirections private constructor() {
  public companion object {
    public fun actionFragmentOpenZipToFragmentZip(): NavDirections =
        ActionOnlyNavDirections(R.id.action_fragmentOpenZip_to_fragmentZip)
  }
}
