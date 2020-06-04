package wsm.preprocess;

import com.alibaba.fastjson.JSON;
import wsm.models.CourtInfoHshfy;
import wsm.models.CourtInfoZxgk;
import wsm.models.CourtInstrumentHshfy;

import java.io.IOException;
import java.util.HashMap;

public class IndexConstruction {

    // version1: test function for building the no word cut Index
    public static void main(String[] args) {

        // the root dir for wsm dataset (the folder of unzipping *.zip)
        String wsmRootDir = "/home/jlzheng/src/java/wsm-dataset/resources";
        System.out.println(System.getenv("WSM_ROOT_DIR"));

        // read document list from three sub dirs
        String hshfySubDir = wsmRootDir + "/home/data/law/hshfy/info";
        String instrumentSubdir = wsmRootDir + "/home/data/law/hshfy_wenshu";
        String zxgkSubDir = wsmRootDir + "/home/data/law/zxgk";
        // the doc to file hash map
        IndexIdToDoc indexIdToDoc = new IndexIdToDoc();
        // construct docId -> fileName index
        indexIdToDoc.updateMapFromDataset(hshfySubDir, 0);
        indexIdToDoc.updateMapFromDataset(instrumentSubdir, 10000000);
        indexIdToDoc.updateMapFromDataset(zxgkSubDir, 20000000);

        CourtInfoHshfy courtInfoHshfy =
                CourtInfoLoader.loadCourtInfoHshfyFromDoc(indexIdToDoc.getDocFileNameFromID(1));
        CourtInstrumentHshfy instrumentInfo =
                CourtInfoLoader.loadCourtInstrumentFromDoc(indexIdToDoc.getDocFileNameFromID(10000001));
        CourtInfoZxgk courtInfoZxgk =
                CourtInfoLoader.loadCourtInfoZxgkFromDoc(indexIdToDoc.getDocFileNameFromID(20000001));

        System.out.println(JSON.toJSONString(CourtInfoLoader.createCourtInfoFromAllFormats(courtInfoHshfy)));
        System.out.println(JSON.toJSONString(CourtInfoLoader.createCourtInfoFromAllFormats(instrumentInfo)));
        System.out.println(JSON.toJSONString(CourtInfoLoader.createCourtInfoFromAllFormats(courtInfoZxgk)));

    }
}
