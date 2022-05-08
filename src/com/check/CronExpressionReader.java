package com.check;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CronExpressionReader {
    public static void main(String[] args) {
        String minuteRegex = "^(\\*|[1-5]?[0-9](-[1-5]?[0-9])?)(\\/[1-9][0-9]*)?(,(\\*|[1-5]?[0-9](-[1-5]?[0-9])?)"
                + "(\\/[1-9][0-9]*)?)*$";
        String hourRegex = "^(\\*|(1?[0-9]|2[0-3])(-(1?[0-9]|2[0-3]))?)(\\/[1-9][0-9]*)?(,(\\*|(1?[0-9]|2[0-3])(-"
                + "(1?[0-9]|2[0-3]))?)(\\/[1-9][0-9]*)?)*$";
        String monthDayRegex = "^(\\*|([1-9]|[1-2][0-9]?|3[0-1])(-([1-9]|[1-2][0-9]?|3[0-1]))?)(\\/[1-9][0-9]*)?(,(\\*|"
                + "([1-9]|[1-2][0-9]?|3[0-1])(-([1-9]|[1-2][0-9]?|3[0-1]))?)(\\/[1-9][0-9]*)?)*$";
        String monthRegex = "^(\\*|([1-9]|1[0-2]?)(-([1-9]|1[0-2]?))?)(\\/[1-9][0-9]*)?(,(\\*|([1-9]|1[0-2]?)(-([1-9]|1[0-2]?))?)(\\/[1-9][0-9]*)?)*$";
        String weekdayRegex = "^(\\*|[0-6](-[0-6])?)(\\/[1-9][0-9]*)?(,(\\*|[0-6](-[0-6])?)(\\/[1-9][0-9]*)?)*$";

        Pattern minutePattern = Pattern.compile(minuteRegex);
        Pattern hourPattern = Pattern.compile(hourRegex);
        Pattern monthDayPattern = Pattern.compile(monthDayRegex);
        Pattern monthPattern = Pattern.compile(monthRegex);
        Pattern weekDayPattern = Pattern.compile(weekdayRegex);
        Scanner scanner = new Scanner(System.in);
        String[] tokens = new String[6];
        for (int i = 0; i < 6; i++) {
            tokens[i] = scanner.next(); // ignores any tokens after 6th
        }

        if (minutePattern.matcher(tokens[0]).matches() && hourPattern.matcher(tokens[1]).matches()
                && monthDayPattern.matcher(tokens[2]).matches() && monthPattern.matcher(tokens[3]).matches()
                && weekDayPattern.matcher(tokens[4]).matches()) {

            System.out.println("minute\t"+ getMinutes(tokens[0]));
            System.out.println("hour\t"+getHours(tokens[1]));
            System.out.println("day of month\t"+getDayOfMonth(tokens[2]));
            System.out.println("month\t"+getMonths(tokens[3]));
            System.out.println("day of week\t"+getDayOfWeek(tokens[4]));
            System.out.println("command\t"+tokens[5]);


        } else {
            throw new IllegalArgumentException("Invalid cron expression");
        }
    }

    private static String getMinutes(String token) {
        return getDiscreteValuesAsSpaceSeparatedString(token, 0,59);
    }

    private static String getHours(String token) {
        return getDiscreteValuesAsSpaceSeparatedString(token, 0, 23);
    }

    private static String getMonths(String token) {
        return getDiscreteValuesAsSpaceSeparatedString(token, 1, 12);
    }

    private static String getDayOfMonth(String token) {
        return getDiscreteValuesAsSpaceSeparatedString(token, 1, 31);
    }

    private static String getDayOfWeek(String token) {
        return getDiscreteValuesAsSpaceSeparatedString(token, 0, 6);
    }

    private static String getDiscreteValuesAsSpaceSeparatedString(String token, int minValue,  int maxValue) {
        if (token.contains(",")) {
            return token.replace(",", " ");
        } else if (token.contains("-")) {
            String[] bounds = token.split("-");
            return IntStream.rangeClosed(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1])).mapToObj(String::valueOf)
                    .collect(Collectors.joining(" "));
        } else if (token.contains("/")) {
            String[] bounds = token.split("/");
            if (bounds[0].equals("*")) {
                StringBuilder sb = new StringBuilder();
                for (int i = minValue; i <= maxValue; i = i + Integer.parseInt(bounds[1])) {
                    sb.append(i + " ");
                }
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = Integer.parseInt(bounds[0]); i <= maxValue; i = i + Integer.parseInt(bounds[1])) {
                    sb.append(i + " ");
                }
                return sb.toString();
            }
        } else if (token.equals("*")){
            return IntStream.rangeClosed(minValue, maxValue).mapToObj(String::valueOf).collect(Collectors.joining(" "));
        }
        return token;
    }
}
