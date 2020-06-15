package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.exception.QueryFormatException;
import wsm.models.CourtInfo;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.models.CourtInfoLoader;
import wsm.utils.BooleanQueryParser;
import wsm.utils.PostingListOperation;

import java.util.*;

public class BooleanIndexCollection extends IndexAbstract{

    private HashMap<String, IndexAbstract> indexCollection;

    public BooleanIndexCollection() {

        indexCollection = new HashMap<>();

        // create all init Boolean Indexes
        for (String noSplitKey: IndexConsts.noSplitKeys){
            indexCollection.put(noSplitKey, new IndexNoWordSplit(noSplitKey));
        }
        for (String normalSplitKey: IndexConsts.normalSplitKeys){
            indexCollection.put(normalSplitKey, new IndexNormalSplit(normalSplitKey));
        }
        for (String localDateKey: IndexConsts.localDateKeys) {
            indexCollection.put(localDateKey, new IndexLocalDate(localDateKey));
        }
        indexCollection.put("duty", new IndexDuty());
        indexCollection.put("caseCode", new IndexCaseCode());
    }

    public IndexAbstract getIndexFromKey(String key) {
        return indexCollection.get(key);
    }

    public void SetIndexFromKey(String key, IndexAbstract indexAbstract){
        indexCollection.put(key, indexAbstract);
    }

    /**
     * update the index from courtInfo
     * @param docId the docID list
     * @param courtInfo the courtInfo object
     */
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo){
        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update Overall Boolean Index from CourtInfo fails.");
            return;
        }
        for (String key: indexCollection.keySet()) {
            indexCollection.get(key).updateFromCourtInfo(docId, courtInfo);
        }
    }

    /**
     * process a request query string with index
     * return a posting List from a query string
     * @param queryString the String for querying
     * @return the posting list
     */
    public TreeSet<Integer> queryFromRequestString(String queryString) {

        List<String> rearEqn = BooleanQueryParser.convertMidEqnToRearEqn(queryString);
        Stack<String> queryStack = new Stack<>();
        HashMap<String, TreeSet<Integer>> tmpMap = new HashMap<String, TreeSet<Integer>>();
        TreeSet<Integer> feedback = null;
        for (String str : rearEqn) {
            if (str.length() == 0) {
                throw new QueryFormatException(2, "query instance format error");
            } else if (str.equals("|") || str.equals("&") || str.equals("^") || str.equals("\\") ) {
                // fetch two op vals
                TreeSet<Integer> feedbackVar2, feedbackVar1;

                // first op2
                if (queryStack.empty()){
                    throw new QueryFormatException(2, "query instance format error");
                }
                String var2 = queryStack.pop();
                if (tmpMap.containsKey(var2)) {
                    feedbackVar2 = (TreeSet<Integer>) tmpMap.get(var2).clone();
                } else {
                    List<String> var2KV = BooleanQueryParser.splitValueAndKey(var2);
                    feedbackVar2 = queryWithKeyValue(var2KV.get(1), var2KV.get(0));
                }

                // then op1
                if (queryStack.empty()){
                    throw new QueryFormatException(2, "query instance format error");
                }
                String var1 = queryStack.pop();
                if (tmpMap.containsKey(var1)) {
                    feedbackVar1 = (TreeSet<Integer>) tmpMap.get(var1).clone();
                } else {
                    List<String> var1KV = BooleanQueryParser.splitValueAndKey(var1);
                    feedbackVar1 = queryWithKeyValue(var1KV.get(1), var1KV.get(0));
                }
                String newKey = "";
                if (str.equals("|")) {
                    PostingListOperation.opORPostingLists(feedbackVar1, feedbackVar2);
                    newKey = "c||" + var1 + var2;
                } else if (str.equals("&")) {
                    PostingListOperation.opANDPostingLists(feedbackVar1, feedbackVar2);
                    newKey = "c&&" + var1 + var2;
                } else if (str.equals("^")) {
                    PostingListOperation.opSYMDIFPostingLists(feedbackVar1, feedbackVar2);
                    newKey = "c^^" + var1 + var2;
                } else {
                    // the case "\\", SUB operation
                    PostingListOperation.opSUBPostingLists(feedbackVar1, feedbackVar2);
                    newKey = "c\\\\" + var1 + var2;
                }
                queryStack.push(newKey);
                tmpMap.put(newKey, feedbackVar1);
                feedback = feedbackVar1;
            } else {
                queryStack.push(str);
            }
        }
        if (feedback ==  null) {
            throw new QueryFormatException(5, "Unknown failure for query string parsing");
        }
        return feedback;
    }

    /**
     * query from a specific index
     * @param key key indicates the index
     * @param value value indicates the query string for a specific index key
     * @return the doc id list
     */
    public TreeSet<Integer> queryWithKeyValue(String key, String value) {
        if (key.equals("all")) {
            TreeSet<Integer> feedback = new TreeSet<>();
            for (Map.Entry<String, IndexAbstract> entry : indexCollection.entrySet()) {
                PostingListOperation.opORPostingLists(feedback,
                        entry.getValue().queryFromRequestString(value));
            }
            return feedback;
        } else if (!indexCollection.containsKey(key)) {
            throw new QueryFormatException(4, "Index Key does not exist in Database");
        } else {
            return indexCollection.get(key).queryFromRequestString(value);
        }
    }


    /**
     * store the index into an index file
     * @param fileRootPath the index file path
     */
    public void storeIndexToDisk(String fileRootPath){
        for (IndexAbstract indexAbstract: indexCollection.values()) {
            indexAbstract.storeIndexToDisk(fileRootPath);
        }
    }

    public static BooleanIndexCollection recoverIndexFromDisk(String fileRootPath){
        BooleanIndexCollection booleanIndexCollection = new BooleanIndexCollection();

        // recover all generated Boolean Indexes
        for (String noSplitKey: IndexConsts.noSplitKeys){
            booleanIndexCollection.SetIndexFromKey(noSplitKey,
                    IndexNoWordSplit.recoverIndexFromDisk(fileRootPath, noSplitKey));
        }
        for (String normalSplitKey: IndexConsts.normalSplitKeys){
            booleanIndexCollection.SetIndexFromKey(normalSplitKey,
                    IndexNormalSplit.recoverIndexFromDisk(fileRootPath, normalSplitKey));
        }
        for (String localDateKey: IndexConsts.localDateKeys) {
            booleanIndexCollection.SetIndexFromKey(localDateKey,
                    IndexLocalDate.recoverIndexFromDisk(fileRootPath, localDateKey));
        }
        booleanIndexCollection.SetIndexFromKey("duty",
                IndexDuty.recoverIndexFromDisk(fileRootPath));
        booleanIndexCollection.SetIndexFromKey("caseCode",
                IndexCaseCode.recoverIndexFromDisk(fileRootPath));
        return booleanIndexCollection;
    }

    public static void main(String[] args) {
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        BooleanIndexCollection booleanIndexCollection = BooleanIndexCollection.recoverIndexFromDisk(wsmRootDir);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Collections.singletonList("浦东新区人民法院{执行法院}");

        for (String queryString: queryStringList) {
            TreeSet<Integer> res = booleanIndexCollection.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.println(courtInfo.toString());
            }
        }

    }

}
