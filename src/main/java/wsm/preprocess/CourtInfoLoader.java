package wsm.preprocess;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import wsm.models.*;

import java.io.File;
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
            courtInfo.setAreaName(courtInfoZxgk.getAreaName().replace('\u00A0', ' ').trim());
            courtInfo.setId(courtInfoZxgk.getId());
            courtInfo.setIname(courtInfoZxgk.getIname().replace('\u00A0', ' ').trim());
            courtInfo.setCaseCode(courtInfoZxgk.getCaseCode().replace('\u00A0', ' ').trim());
            courtInfo.setSexy(courtInfoZxgk.getSexy().replace('\u00A0', ' ').trim());
            courtInfo.setBusinessEntity(courtInfoZxgk.getBusinessEntity().replace('\u00A0', ' ').trim());
            courtInfo.setCardNum(courtInfoZxgk.getCardNum().replace('\u00A0', ' ').trim());
            courtInfo.setCourtName(courtInfoZxgk.getCourtName().replace('\u00A0', ' ').trim());
            courtInfo.setPartyTypeName(courtInfoZxgk.getPartyTypeName().replace('\u00A0', ' ').trim());
            courtInfo.setGistId(courtInfoZxgk.getGistId().replace('\u00A0', ' ').trim());
            courtInfo.setRegDate(courtInfoZxgk.getRegDate());
            courtInfo.setGistUnit(courtInfoZxgk.getGistUnit().replace('\u00A0', ' ').trim());
            courtInfo.setDuty(courtInfoZxgk.getDuty().replace('\u00A0', ' ').trim());
            courtInfo.setPerformance(courtInfoZxgk.getPerformance().replace('\u00A0', ' ').trim());
            courtInfo.setPerformedPart(courtInfoZxgk.getPerformedPart().replace('\u00A0', ' ').trim());
            courtInfo.setDisruptTypeName(courtInfoZxgk.getDisruptTypeName().replace('\u00A0', ' ').trim());
            courtInfo.setPublishDate(courtInfoZxgk.getPublishDate());
            courtInfo.setPeopleInfo(courtInfoZxgk.getPeopleInfo());
            courtInfo.setUnperformPart(courtInfoZxgk.getUnperformPart().replace('\u00A0', ' ').trim());

            if (courtInfo.getPeopleInfo() != null){
                for (PeopleInfoZxgk peopleInfoZxgk: courtInfo.getPeopleInfo()){
                    peopleInfoZxgk.setCardNum(peopleInfoZxgk.getCardNum().replace('\u00A0', ' ').trim());
                    peopleInfoZxgk.setIname(peopleInfoZxgk.getIname().replace('\u00A0', ' ').trim());
                    peopleInfoZxgk.setCorporationtypename(
                            peopleInfoZxgk.getCorporationtypename().replace('\u00A0', ' ').trim());
                }
            }
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInfoHshfy) {

            // the feedback and converted Object
            CourtInfoHshfy courtInfoHshfy = (CourtInfoHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setCaseCode(courtInfoHshfy.getCaseCode().replace('\u00A0', ' ').trim());
            courtInfo.setIname(courtInfoHshfy.getIname().replace('\u00A0', ' ').trim());
            courtInfo.setAddress(courtInfoHshfy.getAddress().replace('\u00A0', ' ').trim());
            courtInfo.setMoney(courtInfoHshfy.getMoney().replace('\u00A0', ' ').trim());
            courtInfo.setApplicant(courtInfoHshfy.getApplicant().replace('\u00A0', ' ').trim());
            String courtAndPhone = courtInfoHshfy.getCourtAndPhone().replace('\u00A0', ' ').trim();
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
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInstrumentHshfy) {

            // the feedback and converted Object
            CourtInstrumentHshfy courtInstrumentHshfy = (CourtInstrumentHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setInstrumentId(courtInstrumentHshfy.getInstrumentId().replace('\u00A0', ' ').trim());
            courtInfo.setCaseCode(courtInstrumentHshfy.getCaseCode().replace('\u00A0', ' ').trim());
            courtInfo.setTheme(courtInstrumentHshfy.getTheme().replace('\u00A0', ' ').trim());
            courtInfo.setType(courtInstrumentHshfy.getType().replace('\u00A0', ' ').trim());
            courtInfo.setCause(courtInstrumentHshfy.getCause().replace('\u00A0', ' ').trim());
            courtInfo.setCourtName(courtInstrumentHshfy.getCourtName().replace('\u00A0', ' ').trim());
            courtInfo.setCourtLevel(courtInstrumentHshfy.getCourtLevel().replace('\u00A0', ' ').trim());
            courtInfo.setCaseDue(courtInstrumentHshfy.getCaseDue());
            courtInfo.setContent(courtInstrumentHshfy.getContent().replace('\u00A0', ' ').trim());

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
}