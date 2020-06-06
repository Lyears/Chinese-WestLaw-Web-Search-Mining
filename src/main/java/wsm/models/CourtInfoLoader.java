package wsm.models;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourtInfoLoader {

    /**
     * load a CountInfoHshfy object from doc file
     * @param docPath the document path
     * @return a CourtInfoHshfy object
     */
    public static CourtInfoHshfy loadCourtInfoHshfyFromDoc(String docPath) {
        File file = new File(docPath);
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            return JSON.parseObject(jsonString, CourtInfoHshfy.class);
        } catch (Exception e) {
            System.out.printf("Error happens when loading a CourtInfoHshfy Object from file, %s", e);
            return null;
        }
    }

    /**
     * load a CountInfoZxgk object from doc file
     * @param docPath the document path
     * @return a CourtInfoZxgk object
     */
    public static CourtInfoZxgk loadCourtInfoZxgkFromDoc(String docPath) {
        File file = new File(docPath);
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            return JSON.parseObject(jsonString, CourtInfoZxgk.class);
        } catch (Exception e) {
            System.out.printf("Error happens when loading a CourtInfoZxgk Object from file, %s", e);
            return null;
        }

    }

    /**
     * load a CountInstrument object from doc file
     * @param docPath the document path
     * @return a CourtInstrument object
     */
    public static CourtInstrumentHshfy loadCourtInstrumentFromDoc(String docPath) {

        File file = new File(docPath);
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            return JSON.parseObject(jsonString, CourtInstrumentHshfy.class);
        } catch (Exception e) {
            System.out.printf("Error happens when loading a CourtInstrumentHshfy Object from file, %s", e);
            return null;
        }
    }

    /**
     * Transform CourtInfo of three formats into the same format
     * @param courtInfoFormatted all cases of CourtInfo Object,
     *                           including CourtInfoHshfy, CourtInfoZxgk, CourtInstrumentHshfy
     * @return A universal CourtInfo Object, for all kinds of CourtInfo data
     */
    public static CourtInfo createCourtInfoFromAllFormats(Object courtInfoFormatted){

        // judge the format of the courtInfo object
        if (courtInfoFormatted instanceof CourtInfoZxgk) {

            // the feedback and converted Object
            CourtInfoZxgk courtInfoZxgk = (CourtInfoZxgk) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setAge(courtInfoZxgk.getAge());
            courtInfo.setAreaName(changeNullToEmpty(courtInfoZxgk.getAreaName()));
            courtInfo.setId(courtInfoZxgk.getId());
            courtInfo.setIname(changeNullToEmpty(courtInfoZxgk.getIname()));
            courtInfo.setCaseCode(changeNullToEmpty(courtInfoZxgk.getCaseCode()));
            if (courtInfoZxgk.getSexy() != null) {
                List<String> sexyList = Arrays.asList("男", "男性", "女性", "女");
                if (sexyList.contains(courtInfoZxgk.getSexy())){
                    courtInfo.setSexy(changeNullToEmpty(courtInfoZxgk.getSexy()).charAt(0) + "");
                } else {
                    courtInfo.setSexy(changeNullToEmpty(courtInfoZxgk.getSexy()));
                }
            } else {
                courtInfo.setSexy("");
            }
            courtInfo.setBusinessEntity(changeNullToEmpty(courtInfoZxgk.getBusinessEntity()));
            courtInfo.setCardNum(changeNullToEmpty(courtInfoZxgk.getCardNum()));
            courtInfo.setCourtName(changeNullToEmpty(courtInfoZxgk.getCourtName()));
            courtInfo.setPartyTypeName(changeNullToEmpty(courtInfoZxgk.getPartyTypeName()));
            courtInfo.setGistId(changeNullToEmpty(courtInfoZxgk.getGistId()));
            courtInfo.setRegDate(courtInfoZxgk.getRegDate());
            courtInfo.setGistUnit(changeNullToEmpty(courtInfoZxgk.getGistUnit()));
            courtInfo.setDuty(changeNullToEmpty(courtInfoZxgk.getDuty()));
            courtInfo.setPerformance(changeNullToEmpty(courtInfoZxgk.getPerformance()));
            courtInfo.setPerformedPart(changeNullToEmpty(courtInfoZxgk.getPerformedPart()));
            courtInfo.setDisruptTypeName(changeNullToEmpty(courtInfoZxgk.getDisruptTypeName()));
            courtInfo.setPublishDate(courtInfoZxgk.getPublishDate());
            courtInfo.setPeopleInfo(courtInfoZxgk.getPeopleInfo());
            courtInfo.setUnperformPart(changeNullToEmpty(courtInfoZxgk.getUnperformPart()).trim());

            if (courtInfo.getPeopleInfo() != null){
                for (PeopleInfoZxgk peopleInfoZxgk: courtInfo.getPeopleInfo()){
                    peopleInfoZxgk.setCardNum(changeNullToEmpty(peopleInfoZxgk.getCardNum()));
                    peopleInfoZxgk.setIname(changeNullToEmpty(peopleInfoZxgk.getIname()));
                    peopleInfoZxgk.setCorporationtypename(
                            changeNullToEmpty(peopleInfoZxgk.getCorporationtypename()));
                }
            }
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInfoHshfy) {

            // the feedback and converted Object
            CourtInfoHshfy courtInfoHshfy = (CourtInfoHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setCaseCode(changeNullToEmpty(courtInfoHshfy.getCaseCode()));
            courtInfo.setIname(changeNullToEmpty(courtInfoHshfy.getIname()));
            courtInfo.setAddress(changeNullToEmpty(courtInfoHshfy.getAddress()));
            courtInfo.setMoney(changeNullToEmpty(courtInfoHshfy.getMoney()));
            courtInfo.setApplicant(changeNullToEmpty(courtInfoHshfy.getApplicant()));
            String courtAndPhone = changeNullToEmpty(courtInfoHshfy.getCourtAndPhone());
            // use regex to match the courtName and Phone number
            if (!courtAndPhone.isBlank()){
                String pattern = "([\\d,-]+)$";
                Pattern regex = Pattern.compile(pattern);
                Matcher match = regex.matcher(courtAndPhone);
                if (match.find()){
                    courtInfo.setCourtName(courtAndPhone.substring(0, match.start()).trim());
                    courtInfo.setCourtPhone(match.group(0).trim());
                }
            }
            // match nothing, then set as ""
            if (courtInfo.getCourtName() == null) {
                courtInfo.setCourtName("");
                courtInfo.setCourtPhone("");
            }
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInstrumentHshfy) {

            // the feedback and converted Object
            CourtInstrumentHshfy courtInstrumentHshfy = (CourtInstrumentHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setInstrumentId(changeNullToEmpty(courtInstrumentHshfy.getInstrumentId()));
            courtInfo.setCaseCode(changeNullToEmpty(courtInstrumentHshfy.getCaseCode()));
            courtInfo.setTheme(changeNullToEmpty(courtInstrumentHshfy.getTheme()));
            courtInfo.setType(changeNullToEmpty(courtInstrumentHshfy.getType()));
            courtInfo.setCause(changeNullToEmpty(courtInstrumentHshfy.getCause()));
            courtInfo.setCourtName(changeNullToEmpty(courtInstrumentHshfy.getCourtName()));
            courtInfo.setCourtLevel(changeNullToEmpty(courtInstrumentHshfy.getCourtLevel()));
            courtInfo.setCaseDue(courtInstrumentHshfy.getCaseDue());
            courtInfo.setContent(changeNullToEmpty(courtInstrumentHshfy.getContent()));

            return courtInfo;

        } else {
            return null;
        }
    }

    /**
     * a function for directly get CourtInfo from file path
     * @param docFile the file path
     * @param docId the docId, used to judge id
     * @param docIdOffsetList the doc id offset list, e.g. [0,10000000,20000000]
     * @return the loaded CourtInfo object
     */
    public static CourtInfo loadCourtInfoFromDoc(String docFile, int docId, List<Integer> docIdOffsetList) {

       if (docIdOffsetList == null || docIdOffsetList.size() != 3) {
           System.out.println("Doc Id offset fails, should have 3 elements.");
       }

       if (docId < docIdOffsetList.get(1) && docId >= docIdOffsetList.get(0)) {
           return CourtInfoLoader.createCourtInfoFromAllFormats(
                   CourtInfoLoader.loadCourtInfoHshfyFromDoc(docFile));
       } else if (docId < docIdOffsetList.get(2)) {
           return CourtInfoLoader.createCourtInfoFromAllFormats(
                   CourtInfoLoader.loadCourtInstrumentFromDoc(docFile));
       } else if (docId >= docIdOffsetList.get(2)){
            return CourtInfoLoader.createCourtInfoFromAllFormats(
                    CourtInfoLoader.loadCourtInfoZxgkFromDoc(docFile));
       }
       System.out.printf("Cannot load a valid CourtInfo object from disk, docId %d", docId);
       return null;
    }

    private static String changeNullToEmpty(String str){
        if (str == null){
            return "";
        } else {
            return str.replace('\u00A0', ' ').trim();
        }
    }
}