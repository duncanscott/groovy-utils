package duncanscott.org.groovy.utils.json.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

@Slf4j
class DateUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    static final String UTC = 'UTC'
    static final TimeZone greenwichMeanTime = TimeZone.getTimeZone(UTC)

    //aim for Javascript ISO date format:
    // e.g. "1993-07-28T21:39:07.000Z"       javascript Date.prototype.toISOString()
    //yyyy-MMM-dd HH:mm:ss.SSS (old format)
    static final String dateFormatText = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    //Quoted "Z" to indicate UTC, no timezone offset
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

    static String nowToString() {
        dateToString(new Date())
    }

    static ArrayNode nowToJsonArray() {
        dateToJsonArray(new Date())
    }

    static String dateToString(Date date) {
        date ? dateFormat.format(date) : null
    }

    static Date stringToDate(String dateString) {
        dateString ? dateFormat.parse(dateString) : null
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
            def val1 = date1[i]
            def val2 = date2[i]
            Integer v1 = (val1 instanceof Number) ? ((Number)val1).intValue() : (val1.toString() as Integer)
            Integer v2 = (val2 instanceof Number) ? ((Number)val2).intValue() : (val2.toString() as Integer)
            result = v1 <=> v2
            i++
        }
        return result
    }

    static ArrayNode dateToJsonArray(Date date = new Date()) {
        ArrayNode json = null
        if (date) {
            json = MAPPER.createArrayNode()
            Calendar c = utcCalendar
            c.setTime(date)
            calendarFields.each { Integer calendarField ->
                json.add(c.get(calendarField))
            }
        }
        return json
    }

    static Date parseDateList(List timeValues) {
        Calendar c = utcCalendar
        if (timeValues) {
            Integer index = 0
            Integer maxIndex = [timeValues.size(), calendarFields.size()].min()
            while (index < maxIndex) {
                def timeValue = timeValues[index]
                Integer intValue = (timeValue instanceof Number) ? ((Number)timeValue).intValue() : (timeValue.toString() as Integer)
                Integer calendarField = calendarFields[index]
                c.set(calendarField, intValue)
                ++index
            }
            return c.getTime()
        }
        return null
    }
}
