package umc.link.zip.presentation.zip

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class ZipFragmentDirections private constructor() {
  public companion object {
    public fun actionFragmentZipToFragmentOpenZip(): NavDirections =
        ActionOnlyNavDirections(R.id.action_fragmentZip_to_fragmentOpenZip)

    public fun actionFragmentZipToFragmentMakeZip(): NavDirections =
        ActionOnlyNavDirections(R.id.action_fragmentZip_to_fragmentMakeZip)
  }
}
