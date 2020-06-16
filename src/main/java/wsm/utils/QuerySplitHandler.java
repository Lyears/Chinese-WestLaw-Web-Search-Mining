package wsm.utils;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.IndexAnalysis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        int midStartIndex = -1, midFinishIndex = -1;
        // find year
        String yearPattern1 = "\\([\\d]*\\)";
        Pattern regex = Pattern.compile(yearPattern1);
        Matcher match = regex.matcher(filteredString);
        String yearPattern2 = "[\\d]*年";
        Pattern regex2 = Pattern.compile(yearPattern2);
        Matcher match2 = regex2.matcher(filteredString);
        if (match.find()){
            feedback.add(filteredString.substring(match.start()+1, match.end()-1).trim());
            midStartIndex = match.end();
        } else if (match2.find()){
            feedback.add(filteredString.substring(match2.start(), match2.end()-1).trim());
            midStartIndex = match2.end();
        } else {
            System.out.printf("Cannot Find year in caseCode %s.\n", caseCode);
            feedback.add("");
        }
        // find case sequence
        String seqPattern = "[第]*[\\d]+[补,-]*号";
        regex = Pattern.compile(seqPattern);
        match = regex.matcher(filteredString);
        if (match.find()){
            midFinishIndex = match.start();
        } else {
            System.out.printf("Cannot Find sequence in caseCode %s.\n", caseCode);
        }
        // process the situation that either year or sequence cannot be matched
        if (midStartIndex != -1 && midFinishIndex != -1){
            feedback.add(filteredString.substring(midStartIndex, midFinishIndex).trim());
            feedback.add(match.group(0).trim());
        } else {
            feedback.add("");
            if (match.find()){
                feedback.add(match.group(0).trim());
            } else {
                feedback.add("");
            }
            System.out.printf("Error happens for getting caseCode midInfo, caseCode %s.\n", caseCode);
        }
        if (!feedback.get(2).isBlank() && feedback.get(2).charAt(0) == '第'){
            feedback.set(2, feedback.get(2).substring(1));
        }

        // filter blanks
        ArrayList<String> feedbackFilter = new ArrayList<String>();
        for (int i = 0; i < feedback.size(); i++) {
            if (!feedback.get(i).isBlank()) {
                feedbackFilter.add(feedback.get(i));
            }
        }
        return feedbackFilter;
    }

    /**
     * split an iname string
     * @param iname the iname string
     * @return an Array for iname keys
     */
    public static ArrayList<String> inameSplitter(String iname) {
        ArrayList<String> feedback = new ArrayList<>();

        String inameRepPattern = "法定代表人：.*";
        Pattern inameRepRegex = Pattern.compile(inameRepPattern);
        Matcher inameRepMatch = inameRepRegex.matcher(iname);
        if (inameRepMatch.find()) {
            String foundCorp = iname.substring(0, inameRepMatch.start());
            feedback.add(foundCorp.replace('（',' ').replace('(',' ').trim());
            feedback.add(iname.substring(inameRepMatch.start()+6, inameRepMatch.end())
                    .replace('）',' ').replace(')',' ').trim());
        } else {
            feedback.add(iname);
        }
        return feedback;
    }

    public static void main(String[] args) {

        List<String> normalQueryList = Arrays.asList("哈哈嘻嘻和呼呼 ， 在上海虹桥机场南路钓鱼。 ",
                "范志明；；；范志毅等", "浦东新区人民法院");
        for (String normalQuery: normalQueryList) {
            ArrayList<String> wordList = indexSplitter(normalQuery);
            System.out.println(wordList);
        }

        LocalDate date = LocalDate.now();
        ArrayList<String> dateList1 = dateSplitter(date);
        System.out.println(dateList1);

        List<String> caseCodes = Arrays.asList(" （2020）沪72执恢3号",
                "（）沪72执恢  第782178-号",
                "2013年昌执字第00603号",
                "？？2017？？？？0422？177？？");
        for (String caseCode: caseCodes) {
            ArrayList<String> caseCodeList = caseCodeSplitter(caseCode);
            System.out.println(caseCodeList);
        }

        List<String> inames = Arrays.asList("上海爱尔爱司动力机械有限公司（法定代表人：陈平）",
                "陈平", "上海爱尔爱司动力机械有限公司");
        for (String iname: inames) {
            ArrayList<String> inameList = inameSplitter(iname);
            System.out.println(inameList);
        }

    }

}
