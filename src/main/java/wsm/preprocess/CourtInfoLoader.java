package wsm.preprocess;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoHshfy;
import wsm.models.CourtInfoZxgk;
import wsm.models.CourtInstrumentHshfy;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourtInfoLoader {

    /**
     * load a CountInfoHshfy object from doc file
     * @param docPath the document path
     * @return a CourtInfoHshfy object
     * @throws IOException io exception
     */
    public static CourtInfoHshfy loadCourtInfoHshfyFromDoc(String docPath) throws IOException {
        File file = new File(docPath);
        String jsonString = FileUtils.readFileToString(file, "UTF-8");
        return JSON.parseObject(jsonString, CourtInfoHshfy.class);
    }

    /**
     * load a CountInfoZxgk object from doc file
     * @param docPath the document path
     * @return a CourtInfoZxgk object
     * @throws IOException io exception
     */
    public static CourtInfoZxgk loadCourtInfoZxgkFromDoc(String docPath) throws IOException {
        File file = new File(docPath);
        String jsonString = FileUtils.readFileToString(file, "UTF-8");
        return JSON.parseObject(jsonString, CourtInfoZxgk.class);
    }

    /**
     * load a CountInstrument object from doc file
     * @param docPath the document path
     * @return a CourtInstrument object
     * @throws IOException io exception
     */
    public static CourtInstrumentHshfy loadCourtInstrumentFromDoc(String docPath) throws IOException {

        File file = new File(docPath);
        String jsonString = FileUtils.readFileToString(file, "UTF-8");
        return JSON.parseObject(jsonString, CourtInstrumentHshfy.class);
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
            courtInfo.setAreaName(courtInfoZxgk.getAreaName());
            courtInfo.setId(courtInfoZxgk.getId());
            courtInfo.setIname(courtInfoZxgk.getIname());
            courtInfo.setCaseCode(courtInfoZxgk.getCaseCode());
            courtInfo.setSexy(courtInfoZxgk.getSexy());
            courtInfo.setBussinessEntity(courtInfoZxgk.getBusinessEntity());
            courtInfo.setCardNum(courtInfoZxgk.getCardNum());
            courtInfo.setCourtName(courtInfoZxgk.getCourtName());
            courtInfo.setPartyTypeName(courtInfoZxgk.getPartyTypeName());
            courtInfo.setGistId(courtInfoZxgk.getGistId());
            courtInfo.setRegDate(courtInfoZxgk.getRegDate());
            courtInfo.setGistUnit(courtInfoZxgk.getGistUnit());
            courtInfo.setDuty(courtInfoZxgk.getDuty());
            courtInfo.setPerformance(courtInfoZxgk.getPerformance());
            courtInfo.setPerformedPart(courtInfoZxgk.getPerformedPart());
            courtInfo.setDisruptTypeName(courtInfoZxgk.getDisruptTypeName());
            courtInfo.setPublishDate(courtInfoZxgk.getPublishDate());
            courtInfo.setPeopleInfo(courtInfoZxgk.getPeopleInfo());
            courtInfo.setUnperformPart(courtInfoZxgk.getUnperformPart());
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInfoHshfy) {

            // the feedback and converted Object
            CourtInfoHshfy courtInfoHshfy = (CourtInfoHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setCaseCode(courtInfoHshfy.getCaseCode());
            courtInfo.setIname(courtInfoHshfy.getIname());
            courtInfo.setAddress(courtInfoHshfy.getAddress());
            courtInfo.setMoney(courtInfoHshfy.getMoney());
            courtInfo.setApplicant(courtInfoHshfy.getApplicant());
            String courtAndPhone = courtInfoHshfy.getCourtAndPhone();
            // use regex to match the courtName and Phone number
            if (!courtAndPhone.isBlank()){
                String pattern = "(\\d+)$";
                Pattern regex = Pattern.compile(pattern);
                Matcher match = regex.matcher(courtAndPhone);
                if (match.find()){
                    courtInfo.setCourtName(courtAndPhone.substring(0, match.start()));
                    courtInfo.setCourtPhone(match.group(1));
                }
            }
            return courtInfo;

        } else if (courtInfoFormatted instanceof CourtInstrumentHshfy) {

            // the feedback and converted Object
            CourtInstrumentHshfy courtInstrumentHshfy = (CourtInstrumentHshfy) courtInfoFormatted;
            CourtInfo courtInfo = new CourtInfo();

            // fill in all possessed fields
            courtInfo.setInstrumentId(courtInstrumentHshfy.getInstrumentId());
            courtInfo.setCaseCode(courtInstrumentHshfy.getCaseCode());
            courtInfo.setTheme(courtInstrumentHshfy.getTheme());
            courtInfo.setType(courtInstrumentHshfy.getType());
            courtInfo.setCause(courtInstrumentHshfy.getCause());
            courtInfo.setCourtName(courtInstrumentHshfy.getCourtName());
            courtInfo.setCourtLevel(courtInstrumentHshfy.getCourtLevel());
            courtInfo.setCaseDue(courtInstrumentHshfy.getCaseDue());
            courtInfo.setContent(courtInstrumentHshfy.getContent());

            return courtInfo;

        } else {
            return null;
        }
    }

}
