package com.walmart.ticketservice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GsonBuilder to convert the java Objects to Json
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
public class GsonUtils {

    static Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    public static String convertToJson(Object obj) {

        return gson.toJson(obj);
    }
}
