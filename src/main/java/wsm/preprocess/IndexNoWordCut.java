package wsm.preprocess;

import wsm.models.CourtInfo;

import java.util.HashMap;
import java.util.LinkedList;
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
    public void updateFromCourtInfo(CourtInfo courtInfo) {

        // only get the field with field name equal to keyword
        String noSplitString = courtInfo.getFieldValueByFieldName(keyWord, courtInfo);
        return;

    }

    @Override
    public LinkedList<Integer> queryFromRequestString(String queryString) {
        return null;
    }

    @Override
    public Integer StoreIndexToDisk(String fileName) {
        return null;
    }

    @Override
    public Integer recoverIndexFromDisk(String fileName) {
        return null;
    }
}
