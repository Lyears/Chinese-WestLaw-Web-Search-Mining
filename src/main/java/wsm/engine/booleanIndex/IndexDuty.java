package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.utils.DiskIOHandler;
import wsm.utils.PostingListOperation;
import wsm.utils.QuerySplitHandler;

import java.io.Serializable;
import java.util.*;


public class IndexDuty extends IndexAbstract implements Serializable {
    public static final long serialVersionUID = 362498820666666666L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private final String[] keyWords = {"duty"};

    public IndexDuty() {
        this.inverseIndex = new HashMap<>();
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update Duty Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){

            // only get the field with field name in keyWords list
            for (String field: this.keyWords){

                // split for other fields
                String fieldValue = courtInfo.get(i).getFieldValueByFieldName(field, courtInfo.get(i));
                if (fieldValue == null || fieldValue.isBlank()){ continue; }
                fieldValue = fieldValue.replace('\u00A0', ' ');
                ArrayList<String> indexKeyList = QuerySplitHandler.indexSplitter(fieldValue);
                for (String s : indexKeyList) {
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
        List<String> splittedKey = QuerySplitHandler.indexSplitter(queryString);
        for (String key: splittedKey) {
            if (feedback.isEmpty()) {
                PostingListOperation.opORPostingLists(feedback, inverseIndex.get(key));
            } else {
                PostingListOperation.opANDPostingLists(feedback, inverseIndex.get(key));
            }
            if (feedback.isEmpty()){
                break;
            }
        }
        return feedback;
    }

    @Override
    public void storeIndexToDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/special_split/duty";
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @return the recovered Index object
     */
    public static IndexDuty recoverIndexFromDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/special_split/duty";
        return (IndexDuty) DiskIOHandler.readObjectFromFile(fileName);
    }

    public static void main(String[] args) {
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        IndexDuty indexDuty = recoverIndexFromDisk(wsmRootDir);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Arrays.asList(
                "人民币，由被告承担，55134元"
        );

        for (String queryString: queryStringList) {
            System.out.printf("Begin to query %s\n", queryString);
            TreeSet<Integer> res = indexDuty.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            int count = 0;
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.printf( "CaseCode %s, duty %s\n",
                        courtInfo.getCaseCode(), courtInfo.getDuty());
                count ++;
                if (count > 50) {
                    break;
                }
            }
        }
    }
}
