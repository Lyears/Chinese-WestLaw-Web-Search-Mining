package wsm.preprocess;

import wsm.models.CourtInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BooleanIndexCollection extends IndexAbstract{

    private ArrayList<IndexAbstract> indexCollection;

    public BooleanIndexCollection() {

        // create all init Boolean Indexes
        for (String noSplitKey: IndexConsts.noSplitKeys){
            indexCollection.add(new IndexNoWordSplit(noSplitKey));
        }
        for (String normalSplitKey: IndexConsts.normalSplitKeys){
            indexCollection.add(new IndexNormalSplit(normalSplitKey));
        }
        for (String localDateKey: IndexConsts.localDateKeys) {
            indexCollection.add(new IndexLocalDate(localDateKey));
        }
        indexCollection.add(new IndexDuty());

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
        for (IndexAbstract indexAbstract: this.indexCollection) {
            indexAbstract.updateFromCourtInfo(docId, courtInfo);
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
        for (IndexAbstract indexAbstract: this.indexCollection) {
            indexAbstract.storeIndexToDisk(fileRootPath);
        }
    }

}
