package umc.link.zip.util.extension

import android.content.Context

class StatusExtension {
    companion object {
        fun getStatusBarHeight(requireContext: Context): Int {
            val resourceId = requireContext.resources.getIdentifier("status_bar_height", "dimen", "android")

            return if (resourceId > 0) {
                requireContext.resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    }
}