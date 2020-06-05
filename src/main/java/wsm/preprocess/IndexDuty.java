package wsm.preprocess;

import wsm.models.CourtInfo;
import wsm.utils.DiskIOHandler;
import wsm.utils.QuerySplitHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;


public class IndexDuty extends IndexAbstract implements Serializable {
    public static final long serialVersionUID = 362498820666666666L;

    // the posting list for a key (or a term), using TreeSet
    private HashMap<String, TreeSet<Integer>> inverseIndex;
    // the key word for this Index, only the corresponding items will be processed in this index
    private final String[] keyWords = {"duty", "performance", "performedPart", "unperformPart"};

    public IndexDuty(String keyWord) {
        this.inverseIndex = new HashMap<>();
    }

    @Override
    public void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo) {

        if (docId == null || courtInfo == null || docId.size() != courtInfo.size()) {
            System.out.println("Update Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){

            // only get the field with field name in keyWords list
            for (String field: this.keyWords){

                if (field.equals("performance")){
                    // do not split for field "performance"
                    String performance =  courtInfo.get(i).getFieldValueByFieldName(field, courtInfo.get(i));
                    if (performance == null){ continue; }
                    if (!inverseIndex.containsKey(performance)) {
                        inverseIndex.put(performance, new TreeSet<>());
                    }
                    inverseIndex.get(performance).add(docId.get(i));
                } else {
                    // split for other fields
                    String fieldValue = courtInfo.get(i).getFieldValueByFieldName(field, courtInfo.get(i));
                    if (fieldValue == null){ continue; }
                    ArrayList<String> indexKeyList = QuerySplitHandler.indexSplitter(fieldValue);
                    for (String s : indexKeyList) {
                        if (!inverseIndex.containsKey(s)) {
                            inverseIndex.put(s, new TreeSet<>());
                        }
                        inverseIndex.get(s).add(docId.get(i));
                    }
                }

            }

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
}
