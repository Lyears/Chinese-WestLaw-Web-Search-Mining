package wsm.server.service;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.engine.booleanIndex.BooleanIndexCollection;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.server.repository.IndexRepository;

import java.time.LocalDate;
import java.util.*;


public class IndexService implements IndexRepository {

    private final String wsmRootDir = System.getenv("WSM_ROOT_DIR");
    BooleanIndexCollection booleanIndexCollection = BooleanIndexCollection.recoverIndexFromDisk(wsmRootDir);
    IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);
    private final int DEFAULT_SORT = 0;
    private final int AGE_SORT = 1;
    private final int DATE_SORT = 2;
    private final int NAME_SORT = 3;
    private final int FINES_SORT = 4;

    @Override
    public List<CourtInfo> indexQuery(String query, int sortType) {
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
                courtInfo.setDocId("doc" + docId);
                result.add(courtInfo);
            }
        }
        switch (sortType) {
            case AGE_SORT:
                result.sort(Comparator.comparing(CourtInfo::getAge, Comparator.nullsFirst(Integer::compareTo).reversed()));
                break;
            case DATE_SORT:
                result.sort(Comparator.comparing(CourtInfo::getRegDate, Comparator.nullsFirst(LocalDate::compareTo).reversed()));
                break;
            case NAME_SORT:
                result.sort(Comparator.comparing(CourtInfo::getIname, Comparator.nullsFirst(String::compareTo).reversed()));
                break;
            case FINES_SORT:
                result.sort(Comparator.comparing(c -> Double.parseDouble(c.getMoney()), Comparator.nullsFirst(Double::compareTo).reversed()));
                break;
        }
        return result;
    }
}

