package com.example.timezoneaware.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DateUtils {

    companion object {

        // dates from server look like this: "2019-07-23T03:28:01.406944Z"
        fun convertServerStringDateToLong(sd: String): Long {
            var stringDate = sd.removeRange(sd.indexOf("T") until sd.length)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val time = sdf.parse(stringDate).time
                return time
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        fun convertLongToStringDate(longDate: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val date = sdf.format(Date(longDate))
                return date
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        //from date range
        fun convertLongToStringTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault())
            return format.format(date)
        }

        fun currentDate(): String {
            val c = Calendar.getInstance()
            val day = c[Calendar.DAY_OF_MONTH]
            val month = c[Calendar.MONTH]
            val year = c[Calendar.YEAR]
            val date = day.toString() + "-" + (month + 1) + "-" + year

            return date.toString()
        }


        fun getCalculatedDate(days: Int): String {
            val c = Calendar.getInstance()

            c.add(Calendar.DAY_OF_YEAR, days)
            val day = c[Calendar.DAY_OF_MONTH]
            val month = c[Calendar.MONTH]
            val year = c[Calendar.YEAR]
            val date = day.toString() + "/" + (month + 1) + "/" + year

            return date.toString()
        }

        fun getTimezoneGMT(timezone:String): String {
            println(timezone)
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy" + " " + " hh:mm:ss aa")

            val localTime = Date()
            // Printing the local time
            System.out.println("local Time:" + dateFormat.format(localTime));
            // convert the localtime to GMT

            //  function will helps to get the GMT Timezone
            // using the getTimeZOne() method
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            System.out.println("Time as per city timezone : "
                    + dateFormat.format(localTime));
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            // Printing the GMT time to
            // illustrate changes in GMT time
            System.out.println("Time IN Gmt : "
                    + dateFormat.format(localTime));

            val timezone: TimeZone = TimeZone.getTimeZone(timezone)
            System.out.println("GMT Diff "
                    + timezone.getDisplayName(false,TimeZone.SHORT));

            return dateFormat.format(localTime)
        }

    }

}