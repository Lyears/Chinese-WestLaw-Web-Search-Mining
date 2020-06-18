package wsm.server.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.engine.booleanIndex.BooleanIndexCollection;
import wsm.exception.EmptyResultException;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.server.repository.IndexRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class IndexService implements IndexRepository {

    private final String wsmRootDir = System.getenv("WSM_ROOT_DIR");
    BooleanIndexCollection booleanIndexCollection = BooleanIndexCollection.recoverIndexFromDisk(wsmRootDir);
    IndexIdToDoc indexIdToDoc = IndexIdToDoc.recoverIndexFromDisk(wsmRootDir);
    private final int DEFAULT_SORT = 0;
    private final int AGE_SORT = 1;
    private final int REG_DATE_SORT = 2;
    private final int PUB_DATE_SORT = 3;
    private final int FINES_SORT = 4;

    @Override
    public List<CourtInfo> indexQuery(String query, int sortType) {
        Comparator<CourtInfo> comparator = null;
        switch (sortType) {
            case DEFAULT_SORT:
                break;
            case AGE_SORT:
                comparator = Comparator.comparing(CourtInfo::getAge, Comparator.nullsFirst(Integer::compareTo).reversed());
                break;
            case REG_DATE_SORT:
                comparator = Comparator.comparing(CourtInfo::getRegDate, Comparator.nullsFirst(LocalDate::compareTo).reversed());
                break;
            case PUB_DATE_SORT:
                comparator = Comparator.comparing(CourtInfo::getPublishDate, Comparator.nullsFirst(LocalDate::compareTo).reversed());
                break;
            case FINES_SORT:
                comparator = Comparator.comparing(c -> {
                    if (c.getMoney() == null) {
                        return null;
                    } else {
                        return Double.parseDouble(c.getMoney());
                    }
                }, Comparator.nullsFirst(Double::compareTo).reversed());
                break;
        }
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
                String docStr;
                if (docId < 10000000) {
                    docStr = "doc0" + docId;
                } else {
                    docStr = "doc2" + docId;
                }
                courtInfo.setDocId(docStr);
                result.add(courtInfo);
            }
        }
        if (result.isEmpty()) {
            throw new EmptyResultException("No results found!");
        }
        if (comparator != null) {
            result.sort(comparator);
        }
        return result.parallelStream().limit(500).collect(Collectors.toList());
    }
}

