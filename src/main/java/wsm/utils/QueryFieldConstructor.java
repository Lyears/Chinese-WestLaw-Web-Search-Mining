package wsm.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryFieldConstructor {

    public static List<String> parseDateString(String query) {

        // check full date
        List<String> formatList = Arrays.asList("yyyy-MM-dd",
                "yyyy年MM月dd日");
        for (String format: formatList) {
            try {
                return QuerySplitHandler.dateSplitter(
                        LocalDate.parse(query, DateTimeFormatter.ofPattern(format)));
            } catch (Exception ignored) { }
        }

        // check year, month and date by
        // find case sequence
        List<String> feedback = new ArrayList<>();
        String yearPattern = "[\\d]{4}[年,y]";
        Pattern regex = Pattern.compile(yearPattern);
        Matcher match = regex.matcher(query);
        if (match.find()){
            feedback.add(query.substring(match.start(), match.end()-1));
        }

        List<String> dayMonthPattern = Arrays.asList("[\\d]{1,2}[月,m]", "[\\d]{1,2}[日,d]");
        for (String pattern: dayMonthPattern) {
            regex = Pattern.compile(pattern);
            match = regex.matcher(query);
            if (match.find()) {
                String founded = match.group(0).
                        replace("月", "m").replace("日", "d");
                if (founded.length() == 2) {
                    founded = "0" + founded;
                }
                feedback.add(founded);
            }
        }

        return feedback;
    }

    public static void main(String[] args) {
        String testString = "2000年11月6日";
        System.out.println(parseDateString(testString));
    }

}
