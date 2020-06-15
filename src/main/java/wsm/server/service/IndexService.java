package wsm.server.service;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.engine.booleanIndex.BooleanIndexCollection;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.server.repository.IndexRepository;

import java.util.*;


public class IndexService implements IndexRepository {

    private final String wsmRootDir = System.getenv("WSM_ROOT_DIR");
    BooleanIndexCollection booleanIndexCollection = BooleanIndexCollection.recoverIndexFromDisk(wsmRootDir);
    IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);

    @Override
    public List<CourtInfo> indexQuery(String query) {
        List<String> queryStringList = Collections.singletonList(query);
        List<CourtInfo> result = new ArrayList<>();
        for (String queryString : queryStringList) {
            TreeSet<Integer> res = booleanIndexCollection.queryFromRequestString(queryString);
            if (res == null) {
                continue;
            }
            for (Integer docId : res) {
                CourtInfo courtInfo = CourtInfoLoader.loadCourtInfoFromDoc(
                        indexIdToDoc.getDocFileNameFromID(docId), docId, IndexConsts.docIdOffsetList);
                result.add(courtInfo);
            }
        }
        return result;
    }
}

