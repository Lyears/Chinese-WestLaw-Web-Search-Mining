package wsm.utils;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.ArrayList;

public class ContentSplitHandler {

    // public static ArrayList<String>

    /**
     * the string splitter for normal query string
     * @param queryString the requested string
     * @return a List for terms
     */
    public static String NlpSplitter(String queryString) {
        // filter stop words
        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("o", "e", "c", "u", "w", "p");
        // split words
        Result res = NlpAnalysis.parse(queryString).recognition(filter);
        // feedback
        StringBuilder feedback = new StringBuilder();
        String term;
        for (int i = 0; i < res.size(); i++){
            term = res.getTerms().get(i).
                    getRealName().replace('\u00A0', ' ').trim();
            if (term.length() == 0){
                continue;
            }
            feedback.append(term.trim());
        }
        return feedback.toString();
    }
}
