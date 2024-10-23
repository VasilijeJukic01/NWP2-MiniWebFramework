package com.nwp.framework.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Helper {

    public static Map<String, String> getParametersFromRoute(String route) {
        String[] routeSplit = route.split("\\?");
        return routeSplit.length == 1 ? new HashMap<>() : getParametersFromString(routeSplit[1]);
    }

    public static Map<String, String> getParametersFromString(String parametersString) {
        return Arrays.stream(parametersString.split("&"))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(keyPair -> keyPair[0], keyPair -> keyPair[1]));
    }
}