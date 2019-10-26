package duncanscott.org.groovy.utils.enumutil


import spock.lang.Specification

class EnumConverterSpec extends Specification {

    enum TestEnumCaseInsensitive {
        ONE,
        TWO,
        THREE
    }

    enum TestEnumCaseSensitive {
        ONE,
        TWO,
        THREE,
        one
    }

    def "test enum conversion case-sensitive"() {
        setup:
        boolean caseSensitive = false

        when:
        EnumConverter<TestEnumCaseSensitive> converter1 = new EnumConverter(TestEnumCaseSensitive)

        then:
        converter1.toEnum('one') == TestEnumCaseSensitive.one

        when:
        EnumConverter<TestEnumCaseInsensitive> converter2 = new EnumConverter<>(TestEnumCaseInsensitive,caseSensitive)

        then:
        converter2.toEnum('one') == TestEnumCaseInsensitive.ONE

        when:
        EnumConverter<TestEnumCaseSensitive> converter3 = new EnumConverter<>(TestEnumCaseSensitive,caseSensitive)
        converter3.toEnum('one')

        then:
        thrown(RuntimeException)

    }

}
