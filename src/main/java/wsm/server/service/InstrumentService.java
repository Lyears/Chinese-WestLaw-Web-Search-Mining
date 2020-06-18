package wsm.server.service;

import wsm.engine.InstrumentConstruction;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.server.repository.InstrumentRepository;
import wsm.utils.TfIdfUtil;

import java.util.ArrayList;
import java.util.List;

public class InstrumentService implements InstrumentRepository {

    private final String wsmRootDir = System.getenv("WSM_ROOT_DIR");
    private final TfIdfUtil tfIdfUtil = TfIdfUtil.recoverFromDisk(wsmRootDir);

    @Override
    public List<CourtInfo> queryInstrument(String query) {
        List<Integer> docNumList = new ArrayList<>();
        List<Integer> docIdOffset = IndexConsts.docIdOffsetList;
        IndexIdToDoc indexIdToDoc = InstrumentConstruction.constructInstrumentMapFromDisk(
                wsmRootDir, docNumList);
        List<Integer> indices = tfIdfUtil.getRelevantTopKIndices(query, 20);
        // update the index from all CourtInfos
        // maximum update entry number
        List<CourtInfo> courtInfos = new ArrayList<>();
        for (int i : indices) {
            int docId = i + docIdOffset.get(1);
            CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                    indexIdToDoc.getDocFileNameFromID(docId), docId, docIdOffset);
            courtInfos.add(courtInfo);
        }
        return courtInfos;
    }
}
