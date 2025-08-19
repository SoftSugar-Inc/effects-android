package softsugar.senseme.com.effects.utils;

import java.util.HashMap;
import java.util.Map;

public class SortingOrder {
    public static Map<String, Integer> createSortingOrder(String[] arr) {
        Map<String, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            orderMap.put(arr[i], i);
        }
        return orderMap;
    }
}
