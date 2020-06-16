package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.utils.DiskIOHandler;
import wsm.utils.FuzzyQueryHandler;
import wsm.utils.PostingListOperation;
import wsm.utils.QuerySplitHandler;

import java.io.Serializable;
import java.util.*;


public class IndexCaseCode extends IndexAbstract implements Serializable {
    public static final long serialVersionUID = 362498820111111111L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;

    public IndexCaseCode() {
        this.inverseIndex = new HashMap<>();
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update CaseCode Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        List<String> caseCodeKeyList = Collections.singletonList("caseCode");
        for (String key: caseCodeKeyList){
            for (int i = 0; i < docId.size(); i++){
                // only get the field with field name equal to keyword
                String stringToSplit = courtInfo.get(i).getFieldValueByFieldName(key, courtInfo.get(i));
                if (stringToSplit == null){
                    continue;
                } else {
                    if (!inverseIndex.containsKey(stringToSplit)) {
                        inverseIndex.put(stringToSplit, new TreeSet<>());
                    }
                    inverseIndex.get(stringToSplit).add(docId.get(i));
                }
                ArrayList<String> indexKeyList = QuerySplitHandler.caseCodeSplitter(stringToSplit);
                for (String s : indexKeyList) {
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
                queryString, IndexConsts.fuzzyThresholdCaseCode, inverseIndex.keySet());

        // OR all the intermediate result
        for (String key: similarKeys) {
            PostingListOperation.opORPostingLists(feedback, inverseIndex.get(key));
        }

        List<String> spittedStringList = QuerySplitHandler.caseCodeSplitter(queryString);
        TreeSet<Integer> feedbackSplit = new TreeSet<>();

        for (String key: spittedStringList) {
            if (feedbackSplit.isEmpty()) {
                PostingListOperation.opORPostingLists(feedbackSplit, inverseIndex.get(key));
            } else {
                PostingListOperation.opANDPostingLists(feedbackSplit, inverseIndex.get(key));
            }
            if (feedbackSplit.isEmpty()){
                break;
            }
        }

        PostingListOperation.opORPostingLists(feedback, feedbackSplit);

        return feedback;
    }

    @Override
    public void storeIndexToDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/special_split/caseCode";
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @return the recovered Index object
     */
    public static IndexCaseCode recoverIndexFromDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/special_split/caseCode";
        return (IndexCaseCode) DiskIOHandler.readObjectFromFile(fileName);
    }

    public static void main(String[] args) {
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        IndexCaseCode indexCaseCode = recoverIndexFromDisk(wsmRootDir);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Arrays.asList(
                "2013年", "（2018）沪0115执12291号", "12291号"
        );

        for (String queryString: queryStringList) {
            System.out.printf("Begin to query %s\n", queryString);
            TreeSet<Integer> res = indexCaseCode.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            int count = 0;
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.printf( "CaseCode %s\n", courtInfo.getCaseCode());
                count ++;
                if (count > 50) {
                    break;
                }
            }
        }
    }
}
