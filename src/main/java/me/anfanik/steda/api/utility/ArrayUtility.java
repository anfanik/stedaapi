package me.anfanik.steda.api.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtility {

    public <T> T[] skipFirst(T[] array, T[] target, int skip) {
        System.arraycopy(array, skip, target, 0, array.length -1);
        return target;
    }

}
