

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText
import java.text.SimpleDateFormat
import java.util.*


open class CustomDateUtils {

    companion object {
        @JvmStatic
        fun setCalendar(editText: DatePickerEditText) {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            val year = calendar.get(Calendar.YEAR)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val dateString = day.toString() + "/" + month + "/" + year
            editText.maxDate = getSubStractYear(dateString, 18)
            editText.minDate = "01/01/1905"
        }

        @JvmStatic
        fun getSubStractYear(date: String, age: Int): String {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val myDate = sdf.parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = myDate
                calendar.add(Calendar.YEAR, -age)
                return sdf.format(calendar.time)
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

        }

        @JvmStatic
        fun getCurrentDateToMongoFormat(): String? {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
            val date = Date()
            return dateFormat.format(date)
        }

        @JvmStatic
        fun setDateToMongoFormat(date: String): String? {
//            val inputFormat = SimpleDateFormat("yyyy/MM-dd", Locale.ENGLISH);
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val dateFormat = inputFormat.parse(date)
//           val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            return outputFormat.format(dateFormat)
        }



            @JvmStatic
            fun getCurrentDay(): String {
                val now = Date()
//                var simpleDateformat = SimpleDateFormat("E") // the day of the week abbreviated
//                println(simpleDateformat.format(now))

               var simpleDateformat = SimpleDateFormat("EEEE",Locale.ENGLISH)
                return  simpleDateformat.format(now)

                // the day of the week spelled out completely
//                println(simpleDateformat.format(now))

//                val calendar = Calendar.getInstance()
//                calendar.time = now
//                System.out.println(calendar.get(Calendar.DAY_OF_WEEK)) // the day of the week in numerical format

        }


    }


}
