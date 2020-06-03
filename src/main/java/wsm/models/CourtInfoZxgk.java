package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourtInfoZXGK {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "iname")
    private String iname;
    @JSONField(name = "caseCode")
    private String caseCode;
    @JSONField(name = "age")
    private int age;
    @JSONField(name = "sexy")
    private String sexy;
    @JSONField(name = "cardNum")
    private String cardNum;
    @JSONField(name = "bussinessEntity")
    private String bussinessEntity;
    @JSONField(name = "courtName")
    private String courtName;
    @JSONField(name = "areaName")
    private String areaName;
    @JSONField(name = "partyTypeName")
    private String partyTypeName;
    @JSONField(name = "gistId")
    private String gistId;
    @JSONField(name = "regDate")
    private String regDate;
    @JSONField(name = "gistUnit")
    private String gistUnit;
    @JSONField(name = "duty")
    private String duty;
    @JSONField(name = "performance")
    private String performance;
    @JSONField(name = "performedPart")
    private String performedPart;
    @JSONField(name = "unperformPart")
    private String unperformPart;
    @JSONField(name = "disruptedTypeName")
    private String disruptTypeName;
    @JSONField(name = "publishDate")
    private String publishDate;
    @JSONField(name = "qysler")
    private PeopleInfoZXGK[] peopleInfo;
}
