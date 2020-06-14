package wsm.utils;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.Recognition;
import org.ansj.recognition.impl.BookRecognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import wsm.engine.InstrumentConstruction;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zmfan
 */
public class TfIdfUtil implements Serializable {

    public static final long serialVersionUID = 362498820111132311L;
    private final List<String> documents;
    private List<List<String>> documentWords;
    private List<Map<String, Integer>> documentTfList;
    private Map<String, Double> idfMap;
    private List<Map<String, Double>> tfIdfMap;
    private Recognition preFilter;

    public void setOnlyNoun(boolean onlyNoun) {
        this.onlyNoun = onlyNoun;
    }

    private boolean onlyNoun = false;

    public TfIdfUtil(List<String> documents) {
        this.documents = documents;
    }

    public void estimate() {
        this.initPreFilter();
        this.splitAllDocsWords();
        this.calAllTf();
        this.calIdf();
        this.tfIdfMap = this.calTfIdf();
    }

    /**
     * get all documents counts, which used to calculate TF-IDF
     *
     * @return all documents counts
     */
    private int getDocumentsCounts() {
        return this.documents.size();
    }

    /**
     * define stop word ,regex and dictionary before split words
     **/
    private void initPreFilter() {
        //add defined dictionary
        DicLibrary.insert(DicLibrary.DEFAULT, "二〇一九年", "n", DicLibrary.DEFAULT_FREQ);
        DicLibrary.insert(DicLibrary.DEFAULT, "二〇一八年", "n", DicLibrary.DEFAULT_FREQ);
        DicLibrary.insert(DicLibrary.DEFAULT, "二〇一七年", "n", DicLibrary.DEFAULT_FREQ);

        StopRecognition filter = new StopRecognition();
        filter.insertStopWords("原告", "被告", "代理人", "中华人民共和国", " ", "");
        filter.insertStopRegexes("\\d{1}$");
        filter.insertStopRegexes("\\s*|\t|\r|\n");
        filter.insertStopRegexes("[a-zA-Z’!\"#$%&\\'()（），。　\u0001\u0007*+,-÷./:：;；|<=>?@，—。?★、…【】？“”‘’！[\\\\]^_`{|}~·±]+");
        this.preFilter = filter;
    }

    /**
     * split all documents
     */
    private void splitAllDocsWords() {
        Recognition filter = this.preFilter;
        documentWords = new ArrayList<>(getDocumentsCounts());
        for (String document : documents) {
            documentWords.add(docSegmentation(document, filter));
        }
    }

    /**
     * document segmentation
     *
     * @param document document statements
     * @param filter   stop word, regex filter
     * @return term list
     */
    private List<String> docSegmentation(String document, Recognition filter) {
        List<String> wordList = new ArrayList<>();
        // add stop word filter and book recognition
        Result splitWordRes = NlpAnalysis.parse(document).recognition(filter).recognition(new BookRecognition());
        for (Term term : splitWordRes.getTerms()) {
            if (onlyNoun) {
                if (term.getNatureStr().equals("n") ||
                        term.getNatureStr().equals("ns") ||
                        term.getNatureStr().equals("nz")) {
                    wordList.add(term.getName());
                }
            } else {
                wordList.add(term.getName());
            }
        }
        return wordList;
    }

    /**
     * calculate TF
     *
     * @param wordList word list
     * @return word term frequency
     */
    private Map<String, Integer> calTf(List<String> wordList) {
        Map<String, Integer> countMap = new HashMap<>();
        for (String word : wordList) {
            if (countMap.containsKey(word)) {
                countMap.put(word, countMap.get(word) + 1);
            } else {
                countMap.put(word, 1);
            }
        }
        return countMap;
    }

    /**
     * calculate TF for all documents
     */
    private void calAllTf() {
        documentTfList = new ArrayList<>(getDocumentsCounts());
        for (List<String> wordList :
                documentWords) {
            documentTfList.add(calTf(wordList));
        }
    }

    /**
     * calculate IDF
     */
    private void calIdf() {
        int documentCount = getDocumentsCounts();
        idfMap = new HashMap<>();

        Map<String, Integer> wordAppearanceMap = new HashMap<>();
        for (Map<String, Integer> countMap : documentTfList) {
            for (String word : countMap.keySet()) {
                if (wordAppearanceMap.containsKey(word)) {
                    wordAppearanceMap.put(word, wordAppearanceMap.get(word) + 1);
                } else {
                    wordAppearanceMap.put(word, 1);
                }
            }

            for (String word : wordAppearanceMap.keySet()) {
                double idf = Math.log((double) documentCount / (double) (wordAppearanceMap.get(word) + 1));
                idfMap.put(word, idf);
            }
        }
    }

    /**
     * calculate TF-IDF for every words
     *
     * @return TF-IDF list for all documents
     */
    private List<Map<String, Double>> calTfIdf() {
        List<Map<String, Double>> tfIdfRes = new ArrayList<>();
        for (Map<String, Integer> documentTfMap : documentTfList) {
            Map<String, Double> tfIdf = new HashMap<>();
            for (String word : documentTfMap.keySet()) {
                double value = idfMap.get(word) * documentTfMap.get(word);
                tfIdf.put(word, value);
            }
            // normalize tf-idf
            double totalLength = tfIdf.values().parallelStream().mapToDouble(s -> s * s).sum();
            tfIdf.replaceAll((k, v) -> v / Math.sqrt(totalLength));
            tfIdfRes.add(tfIdf);
        }
        return tfIdfRes;
    }

    /**
     * calculate cosine similarity for two vectors
     *
     * @param v1 vector1
     * @param v2 vector2
     * @return cosine similarity
     */
    private double calCosineSimilarity(Map<String, Double> v1, Map<String, Double> v2) {
        double result = 0.0;
        for (String word1 : v1.keySet()) {
            for (String word2 : v2.keySet()) {
                if (word1.equals(word2)) {
                    result += v1.get(word1) * v2.get(word2);
                }
            }
        }
        return result;
    }

    /**
     * get top k relevant documents indices
     *
     * @param queryStr query statement
     * @return indices list for top k
     */
    public List<Integer> getRelevantTopKIndices(String queryStr, int k) {
        Recognition filter = this.preFilter;
        List<String> queryTermList = this.docSegmentation(queryStr, filter);
        //get term frequency for query statement
        Map<String, Integer> countMap = this.calTf(queryTermList);
        double totalLength = countMap.values().parallelStream().mapToDouble(s -> s * s).sum();
        Map<String, Double> queryVector = new HashMap<>(countMap.size());
        // normalization
        for (String word : countMap.keySet()) {
            queryVector.put(word, countMap.get(word) / Math.sqrt(totalLength));
        }
        // initialize a tree map for sorting
        Map<Double, Integer> queryResultMap = new TreeMap<>(Comparator.reverseOrder());
        for (Map<String, Double> m1 : this.tfIdfMap) {
            queryResultMap.put(calCosineSimilarity(m1, queryVector), this.tfIdfMap.indexOf(m1));
        }
        return queryResultMap.values().stream().limit(k).collect(Collectors.toList());
    }

    public void storeIndexToDisk(String fileRootPath) {
        String fileName = fileRootPath + "/tfIdf";
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    public static TfIdfUtil recoverFromDisk(String fileRootPath) {
        String fileName = fileRootPath + "/tfIdf";
        return (TfIdfUtil) DiskIOHandler.readObjectFromFile(fileName);
    }

    /**
     * store the object to disk
     *
     * @param fileRootPath root path
     */
    public static void initialize(String fileRootPath) {
        TfIdfUtil tfIdfUtil = new TfIdfUtil(InstrumentConstruction.getInstrumentContentList());
        tfIdfUtil.estimate();
        tfIdfUtil.storeIndexToDisk(fileRootPath);
    }

    public static void main(String[] args) {
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        initialize(wsmRootDir);
    }
}


