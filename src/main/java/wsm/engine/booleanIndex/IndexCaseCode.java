package wsm.engine.booleanIndex;

import wsm.models.CourtInfo;
import wsm.models.PeopleInfoZxgk;
import wsm.utils.DiskIOHandler;
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

        List<String> spittedStringList = QuerySplitHandler.caseCodeSplitter(queryString);
        TreeSet<Integer> feedback = new TreeSet<>();

        for (String key: spittedStringList) {
            if (feedback.isEmpty()) {
                PostingListOperation.opORPostingLists(feedback, inverseIndex.get(key));
            } else {
                PostingListOperation.opANDPostingLists(feedback, inverseIndex.get(key));
            }
            if (feedback.isEmpty()){
                break;
            }
        }

        PostingListOperation.opORPostingLists(feedback, inverseIndex.get(queryString));
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
}
