package wsm.preprocess;

import com.alibaba.fastjson.JSON;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoHshfy;
import wsm.models.CourtInfoZxgk;
import wsm.models.CourtInstrumentHshfy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;


public class IndexConstruction {

    // version1: test function for building the no word cut Index
    public static void main(String[] args) {

        // the root dir for wsm dataset (the folder of unzipping *.zip)
        String wsmRootDir = "/home/zmfan/Data/WSM";
        // System.out.println(System.getenv("WSM_ROOT_DIR"));

        // construct an index for id -> doc
        List<Integer> docIdOffsetList = Arrays.asList(0, 10000000, 20000000);
        ArrayList<Integer> docIdOffset = new ArrayList<>();
        docIdOffset.addAll(docIdOffsetList);
        ArrayList<Integer> docNumList = new ArrayList<>();
        IndexIdToDoc indexIdToDoc = IndexConstruction.constructCourtInfoMapFromDisk(
                wsmRootDir, docIdOffset, docNumList);

        // a test NoWordCutIndex
        String keyWord1 = "caseCode";
        IndexNoWordCut indexNoWordCut = new IndexNoWordCut(keyWord1);

        // update the index from all CourtInfos
        // maximum update entry number
        int maximumEntryUpdateNumber = 1000;
        ArrayList<Integer> docIdList = new ArrayList<>();
        ArrayList<CourtInfo> courtInfos = new ArrayList<>();
        for (int i = 0; i < docIdOffset.size()-2; i++){
            for (int docId = docIdOffset.get(i); docId < docIdOffset.get(i) + docNumList.get(i); docId++){

                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, docIdOffset);
                if (courtInfo != null){
                    courtInfos.add(courtInfo);
                    docIdList.add(docId);
                }

                if (docIdList.size() == maximumEntryUpdateNumber ||
                        docId == docIdOffset.get(i) + docNumList.get(i) - 1){
                    System.out.printf("Update index, current docId %d\n", docId);
                    indexNoWordCut.updateFromCourtInfo(docIdList, courtInfos);
                    docIdList.clear();
                    courtInfos.clear();
                }
            }
        }

        // test a query
        TreeSet<Integer> res1 = indexNoWordCut.queryFromRequestString("（1997）崇执字第308号");
        indexNoWordCut.storeIndexToDisk(wsmRootDir);
        System.out.println(res1);
        CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                indexIdToDoc.getDocFileNameFromID(res1.first()), res1.first(), docIdOffset);

    }


    public static IndexIdToDoc constructCourtInfoMapFromDisk(String wsmRootDir,
                                                             ArrayList<Integer> docIdOffset,
                                                             ArrayList<Integer> docNumList) {
        // read document list from three sub dirs
        String hshfySubDir = wsmRootDir + "/home/data/law/hshfy/info";
        String instrumentSubdir = wsmRootDir + "/home/data/law/hshfy_wenshu";
        String zxgkSubDir = wsmRootDir + "/home/data/law/zxgk";

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
