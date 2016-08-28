package org.idey.excel.expression.operator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author i.dey
 * define set of allowed char symbol can be used as operator
 */
public final class AllowedCharsetUtil {
    /**
     * The set of allowed operator chars
     */
    private static final Set<Character> ALLOWED_OPERATOR_CHARS = new HashSet<>(
                Arrays.asList( '+', '-', '*', '/', '%', '^', '!',
                               '#','§', '$', '&', ';', ':', '~', '<', '>',
                               '|', '='));

    /**
     * Check if a character is an allowed operator char
     * @param ch the char to check
     * @return true if the char is allowed an an operator symbol, false otherwise
     */
    public static boolean isAllowedOperatorChar(char ch) {
        return ALLOWED_OPERATOR_CHARS.contains(ch);
    }

}
