package duncanscott.org.groovy.utils.json.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class DateUtilSpec extends Specification {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    private Date ldToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant())
    }

    void "test compareDateLists"() {
        setup:
        LocalDate baseDate = LocalDate.now()
        Date date1 = ldToDate(baseDate)
        Date date2 = ldToDate(baseDate.plusDays(1))
        ArrayNode dateList1 = DateUtil.dateToJsonArray(date1)
        ArrayNode dateList2 = DateUtil.dateToJsonArray(date2)
        ObjectNode jsonObject1 = MAPPER.createObjectNode()
        jsonObject1.set('date-1', dateList1)
        jsonObject1.set('date-2', dateList2)
        String jsonText = MAPPER.writeValueAsString(jsonObject1)
        ObjectNode jsonObject2 = (ObjectNode) MAPPER.readTree(jsonText)

        List<Integer> list1FromObject1 = dateList1.collect { it.asInt() }
        List<Integer> list2FromObject1 = dateList2.collect { it.asInt() }

        List<Integer> list1FromObject2 = (jsonObject2.get('date-1') as ArrayNode).collect { it.asInt() }
        List<Integer> list2FromObject2 = (jsonObject2.get('date-2') as ArrayNode).collect { it.asInt() }

        expect:
        -1 == DateUtil.compareDateLists(list1FromObject1, list2FromObject1)
        1 == DateUtil.compareDateLists(list2FromObject1, list1FromObject1)
        0 == DateUtil.compareDateLists(list1FromObject1, list1FromObject1)

        -1 == DateUtil.compareDateLists(list1FromObject2, list2FromObject2)
        1 == DateUtil.compareDateLists(list2FromObject2, list1FromObject2)
        0 == DateUtil.compareDateLists(list1FromObject2, list1FromObject2)

        DateUtil.parseDateList(list1FromObject2) == date1
        DateUtil.parseDateList(list2FromObject1) == date2
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
