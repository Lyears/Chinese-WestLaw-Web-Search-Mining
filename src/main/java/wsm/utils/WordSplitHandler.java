package wsm.utils;

import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class WordSplitHandler {
    public static void main(String[] args) {
        String s = "（2017）沪0104民初19331号";
        System.out.println(IndexAnalysis.parse(s));
    }
}
