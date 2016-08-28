package org.idey.excel.expression.util;

import java.util.Collection;

/**
 * @author i.dey
 * Generic class for check for assertion and throw {@link IllegalArgumentException} in case assertion fails.
 */
public class AssertUtil {

    /**
     * check if thr object is null or not
     * @param obj check if obj is null
     * @throws IllegalArgumentException in case obj is null
     */
    public static <T>  void checkNull(T obj){
        if(obj == null){
            throw new IllegalArgumentException("Object is null");
        }
    }

    /**
     * check if array is null or empty or if individual element is null or not
     * @param obj check if array is null or empty or if individual element is null or not
     * @throws IllegalArgumentException if condition is not met
     */
    public static <T>  void checkNull(T[] obj){
        if(obj == null || obj.length == 0){
            throw new IllegalArgumentException("Object is null");
        }
        for(T val:obj){
            checkNull(val);
        }
    }

    /**
     * check if Collection is null or empty or if individual element is null or not
     * @param obj check if Collection is null or empty or if individual element is null or not
     * @throws IllegalArgumentException if condition is not met
     */
    public static <T>  void checkNull(Collection<T> obj){
        if(obj == null || obj.size() == 0){
            throw new IllegalArgumentException("Object is null");
        }
        obj.forEach(AssertUtil::checkNull);
    }


    /**
     * check if array is null or length is equal to supplied lenght
     * @param obj Array to be passed
     * @param length expected array length
     * @throws IllegalArgumentException if condition is not met
     */
    public static <T>  void checkNull(T[] obj, final int length){
        if(length<0){
            throw new IllegalArgumentException(String.format("Invalid length specified %d", length));
        }
        if(obj == null || obj.length != length){
            throw new IllegalArgumentException(String.format("Either Array is" +
                    " null or lenght of array is not %d", length));
        }
        for(T val:obj){
            checkNull(val);
        }
    }


    /**
     * check if array is null or length is equal to supplied lenght
     * @param obj {@link Collection} to be passed
     * @param length expected {@link Collection#size()}
     * @throws IllegalArgumentException if condition is not met
     */
    public static <T>  void checkNull(Collection<T> obj, final int length){
        if(length<0){
            throw new IllegalArgumentException(String.format("Invalid length specified %d", length));
        }
        if(obj == null || obj.size() != length){
            throw new IllegalArgumentException(String.format("Either Array is" +
                    " null or lenght of array is not %d", length));
        }
        obj.forEach(AssertUtil::checkNull);
    }
}
