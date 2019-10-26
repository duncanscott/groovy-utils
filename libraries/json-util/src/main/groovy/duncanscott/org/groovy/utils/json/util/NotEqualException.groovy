package duncanscott.org.groovy.utils.json.util

/**
 * Created by dscott on 4/14/2015.
 */
class NotEqualException extends RuntimeException {

    final Object o1
    final Object o2

    NotEqualException(o1, o2) {
        this.o1 = o1
        this.o2 = o2
    }

    String toString() {
        StringBuffer b = new StringBuffer("JSON elements not equal:")
        b << "\n#### Element 1 ####################################\n"
        b << JsonUtil.toString(o1)
        b << "\n#### Element 2 ####################################\n"
        b << JsonUtil.toString(o2)
        b << "\n###################################################\n"
        b.toString()
    }

}