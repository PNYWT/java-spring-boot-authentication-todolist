package com.authentication.authentication.exception.validation;

import com.authentication.authentication.exception.components.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Comparator;
import java.util.Set;

public class RequestValidator {

    public static void validateAllowedParams(HttpServletRequest request, Set<String> allowedParams) {
        Set<String> sentParams = request.getParameterMap().keySet();
        for (String param : sentParams) {
            if (!allowedParams.contains(param)) {
                String suggestion = getClosestMatch(param, allowedParams);
                throw new InvalidParameterException(param, suggestion);
            }
        }
    }

    private static String getClosestMatch(String input, Set<String> allowed) {
        return allowed.stream()
                .min(Comparator.comparingInt(p -> levenshteinDistance(input, p)))
                .orElse(null);
    }

    // Levenshtein distance algorithm (basic version)
    private static int levenshteinDistance(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;

        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}

