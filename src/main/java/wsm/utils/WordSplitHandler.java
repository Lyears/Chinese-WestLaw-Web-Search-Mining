package wsm.utils;

import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.ArrayList;

public class WordSplitHandler {

    // public static ArrayList<String>

    public static void main(String[] args) {
        String s = "（2017）沪0104民初19331号";
        System.out.println(IndexAnalysis.parse(s));
    }
}
