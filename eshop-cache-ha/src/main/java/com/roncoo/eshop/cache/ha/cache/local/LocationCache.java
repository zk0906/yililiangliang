package com.roncoo.eshop.cache.ha.cache.local;

import java.util.HashMap;
import java.util.Map;

public class LocationCache {
    private static  Map<Long,String> cityMap = new HashMap<Long, String>();
    static{
        cityMap.put(1L,"beijing");
    }

    public static String getCityName(Long cityId){
        return cityMap.get(cityId);
    }

}
