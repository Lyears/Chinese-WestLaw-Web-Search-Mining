package wsm.engine.auxiliaryIndex;

import java.util.Arrays;
import java.util.List;

public class IndexConsts {

    // the offset for docId
    // respectively for [hshfy, instrument, zxgk]
    public static final List<Integer> docIdOffsetList = Arrays.asList(0, 10000000, 20000000);

    // the keys that need no split
    public static final List<String> noSplitKeys =
            Arrays.asList("id", "age", "sexy", "cardNum", "areaName",
                    "courtLevel", "type", "courtPhone", "performance");

    // the keys that need normal split
    public static final List<String> normalSplitKeys =
            Arrays.asList("iname", "courtName", "gistId", "gistUnit", "applicant", "address", "money",
                    "disruptTypeName", "businessEntity", "corporationtypename",
                    "theme", "cause", "performedPart", "unperformPart");

    // the keys that need LocalDate split
    public static final List<String> localDateKeys =
            Arrays.asList("regDate", "publishDate", "caseDue");

}
