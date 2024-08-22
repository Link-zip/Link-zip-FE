package umc.link.zip.presentation.zip

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class MakeZipFragmentDirections private constructor() {
  public companion object {
    public fun actionFragmentMakeZipToFragmentZip(): NavDirections =
        ActionOnlyNavDirections(R.id.action_fragmentMakeZip_to_fragmentZip)
  }
}
