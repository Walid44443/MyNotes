package codes.walid4444.notes.Helper

import android.graphics.Color
import androidx.annotation.ColorInt


class Utility {
    companion object {
        @ColorInt
        fun getContrastColor(@ColorInt color: Int): Int { // Counting the perceptive luminance - human eye favors green color...
            val a =
                1 - (0.299 * Color.red(color) + 0.587 * Color.green(
                    color
                ) + 0.114 * Color.blue(color)) / 255
            val d: Int
            d = if (a < 0.5) {
                0 // bright colors - black font
            } else {
                255 // dark colors - white font
            }
            return Color.rgb(d, d, d)
        }
    }
}
