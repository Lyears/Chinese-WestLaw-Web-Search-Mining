package wsm.preprocess;

import wsm.models.CourtInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;


public class InstrumentConstruction {

    // version1: test function for building the no word cut Index
    public static void main(String[] args) {

        // the root dir for wsm dataset (the folder of unzipping *.zip)
        String wsmRootDir = "/home/jlzheng/src/java/wsm-dataset/resources";
        // System.out.println(System.getenv("WSM_ROOT_DIR"));

        // construct an index for id -> doc
        List<Integer> docIdOffsetList = Arrays.asList(0, 10000000, 20000000);
        ArrayList<Integer> docIdOffset = new ArrayList<>();
        docIdOffset.addAll(docIdOffsetList);
        ArrayList<Integer> docNumList = new ArrayList<>();
        IndexIdToDoc indexIdToDoc = wsm.preprocess.IndexConstruction.constructCourtInfoMapFromDisk(
                wsmRootDir, docIdOffset, docNumList);

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


    public static IndexIdToDoc constructInstrumentMapFromDisk(String wsmRootDir,
                                                             ArrayList<Integer> docIdOffset,
                                                             ArrayList<Integer> docNumList) {
        // read document list from three sub dirs
        String instrumentSubdir = wsmRootDir + "/home/data/law/hshfy_wenshu";

        // the doc to file hash map
        IndexIdToDoc indexIdToDoc = new IndexIdToDoc();
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
