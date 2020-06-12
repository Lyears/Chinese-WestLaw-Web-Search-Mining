package wsm.engine.auxiliaryIndex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

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

    // the query key replacement map
    public static final HashMap<String, String> replaceMap = new HashMap<String, String>(){{
        put("\u00A0", "");
        put(" ", "");
        put("{", "<");
        put("}", ">");
        put("", "id");
        put("", "age");
        put("", "sexy");
        put("", "cardNum");
        put("", "areaName");
        put("", "courtLevel");
        put("", "type");
        put("", "courtPhone");
        put("", "performance");
        put("", "iname");
        put("", "courtName");
        put("", "gistId");
        put("", "gistUnit");
        put("", "applicant");
        put("", "money");
        put("", "address");
        put("", "disruptTypeName");
        put("", "businessEntity");
        put("", "corporationtypename");
        put("", "theme");
        put("", "cause");
        put("", "performedPart");
        put("", "unperformPart");
        put("", "regDate");
        put("", "publishDate");
        put("", "caseDue");
        put("", "caseCode");
        put("", "duty");
    }};


}
