package wsm.utils;

import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.Lda;
import com.github.chen0040.lda.LdaResult;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import wsm.preprocess.InstrumentConstruction;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LDAHandler {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        StopRecognition filter = new StopRecognition();
        filter.insertStopWords("原告", "被告");

        List<String> docs = InstrumentConstruction.getInstrumentContentList().parallelStream().map(s ->
        {
            Matcher m = p.matcher(s);
            s = m.replaceAll("");
            return NlpAnalysis.parse(s).recognition(filter).toStringWithOutNature(" ");
        }
        ).collect(Collectors.toList());

        KeyWordComputer<NlpAnalysis> kwc = new KeyWordComputer<>(20);
        Collection<Keyword> keywords = kwc.computeArticleTfidf(docs.get(0));
        System.out.println(keywords);

        Lda method = new Lda();
        method.setTopicCount(30);
        method.setMaxVocabularySize(10000);

        LdaResult result = method.fit(docs);
        List<TupleTwo<Integer,Double>> topics = result.documents().get(0).topTopics(5);
        System.out.println("Topic Count: " + result.topicCount());


        for(int topicIndex = 0; topicIndex < result.topicCount(); ++topicIndex){
            String topicSummary = result.topicSummary(topicIndex);
            List<TupleTwo<String, Integer>> topKeyWords = result.topKeyWords(topicIndex, 10);
            List<TupleTwo<Doc, Double>> topStrings = result.topDocuments(topicIndex, 5);

            System.out.println("Topic #" + (topicIndex+1) + ": " + topicSummary);

            for(TupleTwo<String, Integer> entry : topKeyWords){
                String keyword = entry._1();
                int score = entry._2();
                System.out.println("Keyword: " + keyword + "(" + score + ")");
            }

            for(TupleTwo<Doc, Double> entry : topStrings){
                double score = entry._2();
                int docIndex = entry._1().getDocIndex();
                String docContent = entry._1().getContent();
                System.out.println("Doc (" + docIndex + ", " + score + ")): " + docContent);
            }
        }
    }
}
