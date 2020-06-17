package wsm.engine.booleanIndex;

import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BooleanIndexConstruction {

    // version1: test function for building the no word cut Index
    public static void main(String[] args) {

        // the root dir for wsm dataset (the folder of unzipping *.zip)
//        String wsmRootDir = "/home/jlzheng/src/java/wsm-dataset/resources";
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            System.out.println("Please first set environment variable WSM_ROOT_DIR");
            return;
        }

        // construct an index for id -> doc
        ArrayList<Integer> docNumList = new ArrayList<>();
        IndexIdToDoc indexIdToDoc = BooleanIndexConstruction.constructCourtInfoMapFromDisk(
                wsmRootDir, docNumList);

        // construct a Boolean index collection (for all fields)
        BooleanIndexCollection booleanIndexCollection = createIndexCollectionFromDocs(indexIdToDoc, docNumList);

        // store all indexes
        booleanIndexCollection.storeIndexToDisk(wsmRootDir);
        indexIdToDoc.storeIndexToDisk(wsmRootDir);

    }

    public static BooleanIndexCollection createIndexCollectionFromDocs(
            IndexIdToDoc indexIdToDoc, List<Integer> docNumList) {

        // feedback
        BooleanIndexCollection feedback = new BooleanIndexCollection();

        // update the Boolean Index Collection from all CourtInfos
        // maximum update entry number
        int maximumEntryUpdateNumber = 2500;
        ArrayList<Integer> docIdList = new ArrayList<>();
        ArrayList<CourtInfo> courtInfos = new ArrayList<>();
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;

        for (int i = 0; i < docIdOffset.size(); i++) {
            for (int docId = docIdOffset.get(i); docId < docIdOffset.get(i) + docNumList.get(i); docId++) {
//            for (int docId = docIdOffset.get(i); docId < docIdOffset.get(i) + 3000; docId++){

                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, docIdOffset);
                if (courtInfo != null) {
                    courtInfos.add(courtInfo);
                    docIdList.add(docId);
                }

                if (docIdList.size() == maximumEntryUpdateNumber ||
                        docId == docIdOffset.get(i) + docNumList.get(i) - 1) {
                    System.out.printf("Update index, current docId %d\n", docId);
                    feedback.updateFromCourtInfo(docIdList, courtInfos);
                    docIdList.clear();
                    courtInfos.clear();
                }
            }
        }
        return feedback;
    }


    /**
     * construct a docId -> fileName index from wsmRootDir, and also create a list for number of docs
     *
     * @param wsmRootDir the wsm root dir
     * @param docNumList also an implicit return parameter, the number of documents for all three dataset
     * @return the constructed index, from docId -> fileName
     */
    public static IndexIdToDoc constructCourtInfoMapFromDisk(String wsmRootDir,
                                                             List<Integer> docNumList) {
        // read document list from three sub dirs
        String hshfySubDir = wsmRootDir + "/home/data/law/hshfy/info";
        String instrumentSubdir = wsmRootDir + "/home/data/law/hshfy_wenshu";
        String zxgkSubDir = wsmRootDir + "/home/data/law/zxgk";
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;

        // the doc to file hash map
        IndexIdToDoc indexIdToDoc = new IndexIdToDoc();
        // construct docId -> fileName index
        int hshfyDocNum = indexIdToDoc.updateMapFromDataset(hshfySubDir, docIdOffset.get(0));
        int instrumentDocNum = indexIdToDoc.updateMapFromDataset(instrumentSubdir, docIdOffset.get(1));
        int zxgkDocNum = indexIdToDoc.updateMapFromDataset(zxgkSubDir, docIdOffset.get(2));

        // return the number of processed documents
        if (docNumList == null) {
            docNumList = new ArrayList<>();
        }
        List<Integer> list = Arrays.asList(hshfyDocNum, instrumentDocNum, zxgkDocNum);
        docNumList.addAll(new ArrayList<>(list));

        return indexIdToDoc;
    }
}
