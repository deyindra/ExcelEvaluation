package org.idey.excel.expression.function;

/**
 * @author i.dey
 * Utility class to check the valid function name
 *
 */
public final class AllowedFunctionUtil {

    /**
     *
     * @param name function name
     * @return return true functiin name is contains alphabet or any proceeding numeric 0 to 9
     * else return false
     */
    public static boolean isValidFunctionName(final String name) {
        if (name == null) {
            return false;
        }
        String strName = name.trim();
        final int size = strName.length();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            final char c = strName.charAt(i);
            if (Character.isLetter(c) || c == '_') {
                continue;
            } else if (Character.isDigit(c) && i > 0) {
                continue;
            }
            return false;
        }
        return true;
    }
}
