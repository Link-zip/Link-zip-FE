import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

object ColorHelper {

    /**
     * 주어진 테마 속성(attrRes)에서 색상 값을 추출합니다.
     *
     * @param context Context, 액티비티나 프래그먼트의 컨텍스트
     * @param attrRes Int, 추출하려는 속성의 리소스 ID
     * @return Int, 색상 값
     */
    @ColorInt
    fun resolveColorAttribute(context: Context, @AttrRes attrRes: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        val found = theme.resolveAttribute(attrRes, typedValue, true)
        return if (found) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            throw IllegalArgumentException("Attribute not found")
        }
    }
}
