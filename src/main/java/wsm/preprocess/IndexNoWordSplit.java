package wsm.preprocess;

import wsm.models.CourtInfo;
import wsm.utils.DiskIOHandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class IndexNoWordSplit extends IndexAbstract implements Serializable {

    public static final long serialVersionUID = 362498820777777777L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private String keyWord;

    public IndexNoWordSplit(String keyWord) {
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
            String noSplitString = courtInfo.get(i).getFieldValueByFieldName(keyWord, courtInfo.get(i));
            if (noSplitString == null){
                continue;
            }
            if (!inverseIndex.containsKey(noSplitString)){
                inverseIndex.put(noSplitString, new TreeSet<>());
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
    public void storeIndexToDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/no_word_split/" + this.keyWord;
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @param keyWord the keyword
     * @return the recovered Index object
     */
    public static IndexNoWordSplit recoverIndexFromDisk(String fileRootPath, String keyWord){
        String fileName = fileRootPath + "/boolean_index/no_word_cut/" + keyWord;
        return (IndexNoWordSplit) DiskIOHandler.readObjectFromFile(fileName);
    }
}
