

import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.regex.Pattern

open class FormattingUtils {
    companion object {
        @JvmStatic
        fun formatMoney(amount: Double): String? {
            val df = DecimalFormat("##.##",
                    DecimalFormatSymbols.getInstance(Locale.ENGLISH))
            return df.format(amount)

        }

        @JvmStatic
        fun formatPhoneNumber(phone: String): String {
//            phone += phone.substring(1)
            return "233".plus(phone.substring(1))
        }

        fun validateEmail(email: String): Boolean {
            return !Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email.trim { it <= ' ' }).matches()
        }


        fun isNotEnoughDigit(textView: EditText): Boolean {
            if (textView.text.toString().trim().length < 10)
//                textInputLayout.error = errorMessage
                return false

            return true


        }
    }
}