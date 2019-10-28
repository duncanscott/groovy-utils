package duncanscott.org.groovy.utils.json.util

import groovy.util.logging.Slf4j
import org.json.simple.JSONArray

import java.text.SimpleDateFormat

/**
 * Created by dscott on 10/13/2014.
 */
@Slf4j
class DateUtil {

    static final String dateFormatText = 'yyyy-MMM-dd HH:mm:ss.SSS'
    static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatText)

    static final List<Integer> calendarFields = [
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH,
            Calendar.HOUR_OF_DAY,
            Calendar.MINUTE,
            Calendar.SECOND,
            Calendar.MILLISECOND
        ]

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
            Calendar c = Calendar.getInstance()
            c.setTime(date)
            calendarFields.each { Integer calendarField ->
                json << c.get(calendarField)
            }
        }
        return json
    }

    static Date parseDateList(List timeValues) {
        Calendar c = Calendar.getInstance()
        if (timeValues) {
            timeValues.eachWithIndex { timeValue, Integer index ->
                Integer timeValueInteger = timeValue as Integer
                if (index < calendarFields.size()) {
                    Integer calendarField = calendarFields[index]
                    c.set(calendarField, timeValueInteger)
                }
            }
            return c.getTime()
        }
        return null
    }
}
