package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.utils.DiskIOHandler;
import wsm.utils.PostingListOperation;
import wsm.utils.QueryFieldConstructor;
import wsm.utils.QuerySplitHandler;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class IndexLocalDate extends IndexAbstract implements Serializable {
    public static final long serialVersionUID = 362498820999999999L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private String keyWord;

    public IndexLocalDate(String keyWord) {
        this.inverseIndex = new HashMap<>();
        this.keyWord = keyWord;
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update LocalDate Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){
            // only get the field with field name equal to keyword
            String stringToSplit = courtInfo.get(i).getFieldValueByFieldName(keyWord, courtInfo.get(i));
            if (stringToSplit == null){
                continue;
            }
            ArrayList<String> indexKeyList = QuerySplitHandler.dateSplitter(
                    LocalDate.parse(stringToSplit, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            for (String s : indexKeyList) {
                if (!inverseIndex.containsKey(s)) {
                    inverseIndex.put(s, new TreeSet<>());
                }
                inverseIndex.get(s).add(docId.get(i));
            }
        }
    }

    @Override
    public TreeSet<Integer> queryFromRequestString(String queryString) {

        List<String> dateSplited = QueryFieldConstructor.parseDateString(queryString);
        TreeSet<Integer> feedback = new TreeSet<>();
        for (String key : dateSplited) {
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
        String fileName = fileRootPath + "/boolean_index/local_dates/" + this.keyWord;
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @param keyWord the keyword
     * @return the recovered Index object
     */
    public static IndexLocalDate recoverIndexFromDisk(String fileRootPath, String keyWord){
        String fileName = fileRootPath + "/boolean_index/local_dates/" + keyWord;
        return (IndexLocalDate) DiskIOHandler.readObjectFromFile(fileName);
    }

    public static void main(String[] args) {
        String testKey = "caseDue";
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        IndexLocalDate indexLocalDate = recoverIndexFromDisk(wsmRootDir, testKey);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Arrays.asList(
                "2018年07月", "2019年8日", "2019-09-01"
        );

        for (String queryString: queryStringList) {
            System.out.printf("Begin to query %s\n", queryString);
            TreeSet<Integer> res = indexLocalDate.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            int count = 0;
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.printf( "CaseCode %s, caseDue %s.\n", courtInfo.getCaseCode(), courtInfo.getCaseDue());
                count ++;
                if (count > 50) {
                    break;
                }
            }
        }
    }
}
