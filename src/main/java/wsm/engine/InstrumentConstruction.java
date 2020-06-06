package wsm.engine;

import wsm.models.CourtInfo;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfoLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class InstrumentConstruction {
    // the root dir for wsm dataset (the folder of unzipping *.zip)
    private static final String wsmRootDir = "/home/jlzheng/src/java/wsm-dataset/resources";
    // version1: test function for building the no word cut Index
    public static void main(String[] args) {

        // System.out.println(System.getenv("WSM_ROOT_DIR"));

        // construct an index for id -> doc
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;
        ArrayList<Integer> docNumList = new ArrayList<>();
        IndexIdToDoc indexIdToDoc = InstrumentConstruction.constructInstrumentMapFromDisk(
                wsmRootDir, docNumList);

        // update the index from all CourtInfos
        // maximum update entry number
        ArrayList<String> contentList = new ArrayList<>();
        for (int docId = docIdOffset.get(1); docId < docIdOffset.get(1) + docNumList.get(1); docId++){

            CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                    indexIdToDoc.getDocFileNameFromID(docId), docId, docIdOffset);
            contentList.add(courtInfo.getContent());
        }

        System.out.println(contentList.size());
        System.out.println(contentList.get(0));
        System.out.println(contentList.get(contentList.size()-1));
    }

    public static List<String> getInstrumentContentList(){

        ArrayList<Integer> docNumList = new ArrayList<>();
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;
        IndexIdToDoc indexIdToDoc = InstrumentConstruction.constructInstrumentMapFromDisk(
                wsmRootDir, docNumList);

        // update the index from all CourtInfos
        // maximum update entry number
        List<String> contentList = new ArrayList<>();
        for (int docId = docIdOffset.get(1); docId < docIdOffset.get(1) + docNumList.get(1); docId++){

            CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                    indexIdToDoc.getDocFileNameFromID(docId), docId, docIdOffset);
            contentList.add(courtInfo.getContent());
        }
        return contentList;
    }

    public static IndexIdToDoc constructInstrumentMapFromDisk(String wsmRootDir,
                                                             List<Integer> docNumList) {
        // read document list from three sub dirs
        String instrumentSubdir = wsmRootDir + "/home/data/law/hshfy_wenshu";

        // the doc to file hash map
        IndexIdToDoc indexIdToDoc = new IndexIdToDoc();
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;
        // construct docId -> fileName index
        int instrumentDocNum = indexIdToDoc.updateMapFromDataset(instrumentSubdir, docIdOffset.get(1));

        // return the number of processed documents
        if (docNumList == null) {
            docNumList = new ArrayList<>();
        }
        List<Integer> list = Arrays.asList(0, instrumentDocNum, 0);
        docNumList.addAll(new ArrayList<>(list));

        return indexIdToDoc;
    }
}
