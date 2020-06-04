package wsm.preprocess;

import wsm.models.CourtInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class IndexNoWordCut extends IndexAbstract{

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private String keyWord;

    public IndexNoWordCut(String keyWord) {
        this.inverseIndex = new HashMap<>();
        this.keyWord = keyWord;
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){
            // only get the field with field name equal to keyword
            String noSplitString = courtInfo.get(i).getFieldValueByFieldName(keyWord, courtInfo);
            if (noSplitString == null){
                continue;
            }
            inverseIndex.get(noSplitString).add(docId.get(i));
        }
    }

    @Override
    public TreeSet<Integer> queryFromRequestString(String queryString) {

        if (inverseIndex.containsKey(queryString)) {
            return inverseIndex.get(queryString);
        }
        return null;
    }

    @Override
    public Integer storeIndexToDisk(String fileName) {
        return null;
    }

    @Override
    public Integer recoverIndexFromDisk(String fileName) {
        return null;
    }
}
