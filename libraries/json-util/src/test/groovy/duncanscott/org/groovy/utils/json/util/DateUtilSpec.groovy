package duncanscott.org.groovy.utils.json.util

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import spock.lang.Specification

import java.time.LocalDate

class DateUtilSpec extends Specification {

    void "test compareDateLists"() {
        setup:
        LocalDate baseDate = LocalDate.now()
        Date date1 = baseDate.toDate()
        Date date2 = baseDate.plusDays(1).toDate()
        JSONArray dateList1 = DateUtil.dateToJsonArray(date1)
        JSONArray dateList2 = DateUtil.dateToJsonArray(date2)
        JSONObject jsonObject1 = new JSONObject()
        jsonObject1['date-1'] = dateList1
        jsonObject1['date-2'] = dateList2
        String jsonText = jsonObject1.toString()
        JSONObject jsonObject2 = (JSONObject) new JSONParser().parse(jsonText)

        expect:
        -1 == DateUtil.compareDateLists(dateList1, dateList2)
        1 == DateUtil.compareDateLists(dateList2, dateList1)
        0 == DateUtil.compareDateLists(dateList1, dateList1)

        -1 == DateUtil.compareDateLists(jsonObject2['date-1'] as List, jsonObject2['date-2'] as List)
        1 == DateUtil.compareDateLists(jsonObject2['date-2'] as List, jsonObject2['date-1'] as List)
        0 == DateUtil.compareDateLists(jsonObject2['date-1'] as List, jsonObject2['date-1'] as List)

        DateUtil.parseDateList(jsonObject2['date-1'] as List) == date1
        DateUtil.parseDateList(jsonObject1['date-2'] as List) == date2
    }

    void "test dateToString stringToDate"() {
        setup:
        String dateString1 = '1993-07-28T21:39:07.543Z'
        String dateString3 = '1993-07-28T21:39:07.544Z'

        when:
        Date date1 = DateUtil.stringToDate(dateString1)

        then:
        noExceptionThrown()
        date1

        when:
        String dateString2 = DateUtil.dateToString(date1)

        then:
        noExceptionThrown()
        dateString2 == dateString1

        when:
        Date date2 = DateUtil.stringToDate(dateString2)

        then:
        date2 == date1

        when:
        Date date3 = DateUtil.stringToDate(dateString3)

        then:
        date3 > date1
        date3 != date2

    }


}
