package duncanscott.org.groovy.utils.json.util

import groovy.util.logging.Slf4j
import org.json.simple.JSONArray

import java.text.SimpleDateFormat

/**
 * Created by dscott on 10/13/2014.
 */
@Slf4j
class DateUtil {

    static final String UTC = 'UTC'
    static final TimeZone greenwichMeanTime = TimeZone.getTimeZone(UTC);

    //aim for Javascript ISO date format:
    // e.g. "1993-07-28T21:39:07.000Z"       javascript Date.prototype.toISOString()
    //yyyy-MMM-dd HH:mm:ss.SSS (old format)
    static final String dateFormatText = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"  //Quoted "Z" to indicate UTC, no timezone offset
    static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatText)
    static {
        dateFormat.setTimeZone(greenwichMeanTime)
    }

    static final List<Integer> calendarFields = [
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH,
            Calendar.HOUR_OF_DAY,
            Calendar.MINUTE,
            Calendar.SECOND,
            Calendar.MILLISECOND
        ]

    static Calendar getUtcCalendar() {
        Calendar c = Calendar.getInstance()
        c.setTimeZone(greenwichMeanTime)
        c
    }

    static String dateToString(Date date) {
        dateFormat.format(date)
    }

    static Date stringToDate(String dateString) {
        dateFormat.parse(dateString)
    }

    static int compareDateLists(List date1, List date2) {
        if (!date1 || !date2) {
            log.error 'NULL date passed to compareDateLists'
            if (date1) {
                return -1
            }
            if (date2) {
                return 1
            }
            return 0
        }
        int result = 0
        int i = 0
        int maxIndex = date1.size()
        if (date2.size() < maxIndex) {
            maxIndex = date2.size()
        }
        while (!result && i < maxIndex) {
            Integer v1 = date1[i] as Integer
            Integer v2 = date2[i] as Integer
            result = v1 <=> v2
            i++
        }
        return result
    }

    static JSONArray dateToJsonArray(Date date = new Date()) {
        JSONArray json = null
        if (date) {
            json = new JSONArray()
            Calendar c = utcCalendar
            c.setTime(date)
            calendarFields.each { Integer calendarField ->
                json << c.get(calendarField)
            }
        }
        return json
    }

    static Date parseDateList(List timeValues) {
        Calendar c = utcCalendar
        if (timeValues) {
            Integer index = 0
            Integer maxIndex = [timeValues.size(),calendarFields.size()].min()
            while (index < maxIndex) {
                Integer timeValue = timeValues[index] as Integer
                Integer calendarField = calendarFields[index]
                c.set(calendarField, timeValue)
                ++index
            }
            return c.getTime()
        }
        return null
    }
}
