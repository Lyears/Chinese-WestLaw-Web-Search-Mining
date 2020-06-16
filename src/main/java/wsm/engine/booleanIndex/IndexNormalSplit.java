package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.models.PeopleInfoZxgk;
import wsm.utils.DiskIOHandler;
import wsm.utils.FuzzyQueryHandler;
import wsm.utils.PostingListOperation;
import wsm.utils.QuerySplitHandler;

import java.io.Serializable;
import java.util.*;

public class IndexNormalSplit extends IndexAbstract implements Serializable {
    public static final long serialVersionUID = 362498820888888888L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private String keyWord;

    public IndexNormalSplit(String keyWord) {
        this.inverseIndex = new HashMap<>();
        this.keyWord = keyWord;
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update Normal-Split Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){
            // only get the field with field name equal to keyword
            String stringToSplit = courtInfo.get(i).getFieldValueByFieldName(keyWord, courtInfo.get(i));
            if (stringToSplit == null || stringToSplit.length() <= 1){
                // ignore null keys or too short strings
                continue;
            } else if (stringToSplit.trim().length() <= 40 && stringToSplit.trim().length() >= 2){
                if (keyWord.equals("iname")) {
                    ArrayList<String> inameSplitList = QuerySplitHandler.inameSplitter(stringToSplit);
                    for (String sp: inameSplitList) {
                        if (!inverseIndex.containsKey(sp)){
                            inverseIndex.put(sp, new TreeSet<>());
                        }
                        inverseIndex.get(sp).add(docId.get(i));
                    }
                } else {
                    // for too short items, also use the whole string as key
                    if (!inverseIndex.containsKey(stringToSplit)){
                        inverseIndex.put(stringToSplit, new TreeSet<>());
                    }
                    inverseIndex.get(stringToSplit).add(docId.get(i));
                }
            }
            // split the query String and use splited words as index
            ArrayList<String> indexKeyList = QuerySplitHandler.indexSplitter(stringToSplit);
            for (String s : indexKeyList) {
                if (s.length() <= 1) {continue;}
                if (!inverseIndex.containsKey(s)) {
                    inverseIndex.put(s, new TreeSet<>());
                }
                inverseIndex.get(s).add(docId.get(i));
            }
            // process the sublist qysler in CourtInfo
            if (courtInfo.get(i).getPeopleInfo() == null){
                continue;
            }
            for (PeopleInfoZxgk peopleInfoZxgk : courtInfo.get(i).getPeopleInfo()){
                stringToSplit = peopleInfoZxgk.getFieldValueByFieldName(keyWord, peopleInfoZxgk);
                if (stringToSplit == null || stringToSplit.length() <= 1){
                    continue;
                } else if (stringToSplit.trim().length() <= 40 && stringToSplit.trim().length() >= 2){
                    if (!inverseIndex.containsKey(stringToSplit)){
                        inverseIndex.put(stringToSplit, new TreeSet<>());
                    }
                    inverseIndex.get(stringToSplit).add(docId.get(i));
                }
                ArrayList<String> indexKeyListHierarchy = QuerySplitHandler.indexSplitter(stringToSplit);
                for (String s : indexKeyListHierarchy) {
                    if (s.length() <= 1) {continue;}
                    if (!inverseIndex.containsKey(s)) {
                        inverseIndex.put(s, new TreeSet<>());
                    }
                    inverseIndex.get(s).add(docId.get(i));
                }
            }
        }
    }

    @Override
    public TreeSet<Integer> queryFromRequestString(String queryString) {

        TreeSet<Integer> feedback = new TreeSet<>();
        // query whole string
        if (queryString.trim().length() <= 30 && queryString.trim().length() >= 2) {
            PostingListOperation.opORPostingLists(feedback, inverseIndex.get(queryString));
        }
        if (!feedback.isEmpty()) {
            return feedback;
        }

        // if there is no exact matching, find most similar keys in key set
        List<String> similarKeys = FuzzyQueryHandler.findMostSimilarKeys(
                queryString, IndexConsts.fuzzyThreshold, inverseIndex.keySet());

        // OR all the intermediate result
        for (String key: similarKeys) {
            PostingListOperation.opORPostingLists(feedback, inverseIndex.get(key));
        }

        // query spitted terms, AND all results
        List<String> splitedKeys = QuerySplitHandler.indexSplitter(queryString);
        TreeSet<Integer> feedbackSplit = new TreeSet<>();
        for (String key : splitedKeys) {
            if (feedbackSplit.isEmpty()) {
                PostingListOperation.opORPostingLists(feedbackSplit, inverseIndex.get(key));
            } else {
                PostingListOperation.opANDPostingLists(feedbackSplit, inverseIndex.get(key));
            }
            if (feedbackSplit.isEmpty()) {
                break;
            }
        }
        PostingListOperation.opORPostingLists(feedback, feedbackSplit);
        return feedback;
    }

    @Override
    public void storeIndexToDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/normal_split/" + this.keyWord;
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @param keyWord the keyword
     * @return the recovered Index object
     */
    public static IndexNormalSplit recoverIndexFromDisk(String fileRootPath, String keyWord){
        String fileName = fileRootPath + "/boolean_index/normal_split/" + keyWord;
        return (IndexNormalSplit) DiskIOHandler.readObjectFromFile(fileName);
    }

    public static void main(String[] args) {
        String testKey = "iname";
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        IndexNormalSplit indexNoWordSplit = recoverIndexFromDisk(wsmRootDir, testKey);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Arrays.asList(
                "上海明仑贸易有限公司", "李明", "上海市森海园艺发展有限公司h", "卢秀连"
        );

        for (String queryString: queryStringList) {
            System.out.printf("Begin to query %s\n", queryString);
            TreeSet<Integer> res = indexNoWordSplit.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            int count = 0;
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.println(courtInfo.toString());
                count ++;
                if (count > 50) {
                    break;
                }
            }
        }
    }
}
