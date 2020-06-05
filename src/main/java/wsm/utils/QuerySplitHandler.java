package wsm.utils;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.IndexAnalysis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuerySplitHandler {

    /**
     * the string splitter for normal query string
     * @param queryString the requested string
     * @return a List for terms
     */
    public static ArrayList<String> indexSplitter(String queryString) {
        // filter stop words
        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("o", "e", "c", "u", "w", "p");
        // split words
        Result res = IndexAnalysis.parse(queryString).recognition(filter);
        // feedback
        ArrayList<String> feedback = new ArrayList<>();
        String term;
        for (int i = 0; i < res.size(); i++){
            term = res.getTerms().get(i).
                    getRealName().replace('\u00A0', ' ').trim();
            if (term.length() == 0){
                continue;
            }
            feedback.add(term.trim());
        }
        return feedback;
    }

    /**
     * the index key builder for LocalDate
     * @param date the requested LocalDate
     * @return a List for terms
     */
    public static ArrayList<String> dateSplitter(LocalDate date) {

        // cut Date string
        String[] dateList = date.toString().split("-");
        ArrayList<String> feedback = new ArrayList<>(Arrays.asList(dateList));
        feedback.set(1, feedback.get(1)+"m");
        feedback.set(2, feedback.get(2)+"d");

        return feedback;
    }

    /**
     * case code parser
     * @param caseCode the caseCode string, e.g. "（1997）崇执字第308号"
     * @return a List for caseCode index keys
     */
    public static ArrayList<String> caseCodeSplitter(String caseCode) {

        // feedback
        ArrayList<String> feedback = new ArrayList<>();

        // filter some Chinese characters
        String filteredString = caseCode.replace('\u00A0', ' ').
                replace('\uFF08', '(').replace('\uFF09',')').trim();

        int midStartIndex, midFinishIndex;
        // find year
        String yearPattern = "([\\d]+)";
        Pattern regex = Pattern.compile(yearPattern);
        Matcher match = regex.matcher(filteredString);
        if (match.find()){
            feedback.add(match.group(0).trim());
            midStartIndex = match.end() + 1;
        } else {
            System.out.printf("Cannot Find year in caseCode %s.\n", caseCode);
            return null;
        }
        // find case sequence
        String seqPattern = "[第,恢]*[\\d]+号";
        regex = Pattern.compile(seqPattern);
        match = regex.matcher(filteredString);
        if (match.find()){
            midFinishIndex = match.start();
        } else {
            System.out.printf("Cannot Find sequence in caseCode %s.\n", caseCode);
            return null;
        }
        feedback.add(filteredString.substring(midStartIndex, midFinishIndex).trim());
        feedback.add(match.group(0).trim());
        return feedback;
    }


    public static void main(String[] args) {
        String normalQuery = "哈哈嘻嘻和呼呼 ， 在上海虹桥机场南路钓鱼。 ";
        ArrayList<String> wordList1 = indexSplitter(normalQuery);
        System.out.println(wordList1);

        LocalDate date = LocalDate.now();
        ArrayList<String> dateList1 = dateSplitter(date);
        System.out.println(dateList1);

        String caseCodeQuery = "（2020）沪72执恢3号";
        ArrayList<String> caseCodeList1 = caseCodeSplitter(caseCodeQuery);
        System.out.println(caseCodeList1);
    }

}
