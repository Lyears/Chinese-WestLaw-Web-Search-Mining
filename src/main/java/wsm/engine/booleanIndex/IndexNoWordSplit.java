package wsm.engine.booleanIndex;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.models.PeopleInfoZxgk;
import wsm.utils.DiskIOHandler;
import wsm.utils.FuzzyQueryHandler;
import wsm.utils.PostingListOperation;

import java.io.Serializable;
import java.util.*;

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
            System.out.println("Update No-Word-Split Index from CourtInfo fails.");
            return;
        }

        // update index for every courtInfo
        for (int i = 0; i < docId.size(); i++){
            // only get the field with field name equal to keyword
            String noSplitString = courtInfo.get(i).getFieldValueByFieldName(keyWord, courtInfo.get(i));
            if (noSplitString == null || noSplitString.isBlank()){
                continue;
            }
            if (!inverseIndex.containsKey(noSplitString)){
                inverseIndex.put(noSplitString, new TreeSet<>());
            }
            inverseIndex.get(noSplitString).add(docId.get(i));
            // process the sublist qysler in CourtInfo
            if (courtInfo.get(i).getPeopleInfo() == null){
                continue;
            }
            for (PeopleInfoZxgk peopleInfoZxgk : courtInfo.get(i).getPeopleInfo()){
                noSplitString = peopleInfoZxgk.getFieldValueByFieldName(keyWord, peopleInfoZxgk);
                if (noSplitString == null || noSplitString.isBlank()){
                    continue;
                }
                if (!inverseIndex.containsKey(noSplitString)){
                    inverseIndex.put(noSplitString, new TreeSet<>());
                }
                inverseIndex.get(noSplitString).add(docId.get(i));
            }
        }
    }

    @Override
    public TreeSet<Integer> queryFromRequestString(String queryString) {

        TreeSet<Integer> feedback = new TreeSet<>();
        // if the query string directly occurs in inverse index
        if (inverseIndex.containsKey(queryString)){
            feedback = (TreeSet<Integer>) inverseIndex.get(queryString).clone();
            return feedback;
        }

        // if there is no exact matching, find most similar keys in key set
        List<String> similarKeys = FuzzyQueryHandler.findMostSimilarKeys(
                queryString, IndexConsts.fuzzyThreshold, inverseIndex.keySet());

        // OR all the intermediate result
        for (String key: similarKeys) {
            PostingListOperation.opORPostingLists(feedback, inverseIndex.get(key));
        }
        return feedback;
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
        String fileName = fileRootPath + "/boolean_index/no_word_split/" + keyWord;
        return (IndexNoWordSplit) DiskIOHandler.readObjectFromFile(fileName);
    }

    public static void main(String[] args) {
        String testKey = "courtPhone";
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }
        IndexNoWordSplit indexNoWordSplit = recoverIndexFromDisk(wsmRootDir, testKey);
        IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

        List<String> queryStringList = Arrays.asList("38794519", "38794444", "38794518");

        for (String queryString: queryStringList) {
            System.out.printf("Begin to query %s\n", queryString);
            TreeSet<Integer> res = indexNoWordSplit.queryFromRequestString(queryString);
            if (res == null) {
                System.out.printf("Query Fails for %s.\n", queryString);
                continue;
            }
            int count = 0;
            for (Integer docId: res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                System.out.println(courtInfo.toString());
                count ++;
                if (count > 50) {
                    break;
                }
            }
        }

    }
}
