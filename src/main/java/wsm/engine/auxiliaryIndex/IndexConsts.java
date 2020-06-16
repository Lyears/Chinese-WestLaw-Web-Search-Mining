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
        put("年龄", "age");
        put("性别", "sexy");
        put("身份证号码", "cardNum");
        put("组织机构代码", "cardNum");
        put("省份", "areaName");
        put("级别", "courtLevel");
        put("文书类别", "type");
        put("联系电话", "courtPhone");
        put("被执行人履行情况", "performance");
        put("被执行人姓名", "iname");
        put("被执行人名称", "iname");
        put("执行法院", "courtName");
        put("执行依据文号", "gistId");
        put("做出执行依据单位", "gistUnit");
        put("申请执行人", "applicant");
        put("被执行的金额", "money");
        put("被执行人地址", "address");
        put("被执行人行为具体情形", "disruptTypeName");
        put("法人", "businessEntity");
        put("人员与公司关系", "corporationtypename");
        put("标题", "theme");
        put("案由", "cause");
        put("履行部分", "performedPart");
        put("未履行部分", "unperformPart");
        put("立案时间", "regDate");
        put("发布时间", "publishDate");
        put("结案日期", "caseDue");
        put("案号", "caseCode");
        put("义务", "duty");
    }};

    // the pre-query string replacement map
    public static final HashMap<String, String> replaceMapPre = new HashMap<String, String>(){{
        put("\u00A0", "");
        put(" ", "");
        put("{", "<");
        put("}", ">");
        put("AND", "&");
        put("OR", "|");
        put("SUB", "\\");
        put("XOR", "^");
        put("SIMDIF", "^");
    }};

    public static float fuzzyThreshold = (float) 0.85;
    public static float fuzzyThresholdCaseCode = (float) 0.90;
}
