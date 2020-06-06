package wsm.engine.booleanIndex;

import wsm.models.CourtInfo;
import wsm.engine.auxiliaryIndex.IndexConsts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

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
    public TreeSet<Integer> queryFromRequestString(String queryString){
        return null;
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

}
