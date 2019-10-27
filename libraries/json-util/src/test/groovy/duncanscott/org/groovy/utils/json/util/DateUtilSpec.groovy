package duncanscott.org.groovy.utils.json.util

import duncanscott.org.groovy.utils.json.util.DateUtil
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

        when:
        String dateString = DateUtil.dateToString(date1)

        then:
        noExceptionThrown()
    }

}
