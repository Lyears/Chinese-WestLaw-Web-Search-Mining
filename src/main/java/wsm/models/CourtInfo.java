package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.time.LocalDate;

/*
    the overall object for court information
    Note that original doc data has three formats
 */
@Getter
@Setter
@NoArgsConstructor
public class CourtInfo {

    // fields from Hshfy
    private String caseCode;
    private String iname;
    private String address;
    private String money;
    private String applicant;
    // should split court and phone
    private String courtName;
    private String courtPhone;

    // fields from zxgk
    // iname, caseCode and courtName have been defined above
    private Integer id;
    private Integer age;
    private String sexy;
    private String cardNum;
    private String businessEntity;
    private String areaName;
    private String partyTypeName;
    private String gistId;
    private LocalDate regDate;
    private String gistUnit;
    private String duty;
    private String performance;
    private String performedPart;
    private String unperformPart;
    private String disruptTypeName;
    private LocalDate publishDate;
    private PeopleInfoZxgk[] peopleInfo;

    // fields from instruments
    // Note that courtName, caseCode have been defined above
    private String instrumentId;
    private String theme;
    private String type;
    private String cause;
    private String courtLevel;
    private LocalDate caseDue;
    private String content;

    /**
     * get field value bu field name, with JAVA reflect
     * @param fieldName the field Name
     * @param object the java object
     * @return the returned field
     */
    public String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }
}
