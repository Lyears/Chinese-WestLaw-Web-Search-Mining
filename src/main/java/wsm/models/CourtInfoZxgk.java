package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourtInfoZxgk {

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
    @JSONField(name = "businessEntity")
    private String businessEntity;
    @JSONField(name = "courtName")
    private String courtName;
    @JSONField(name = "areaName")
    private String areaName;
    @JSONField(name = "partyTypeName")
    private String partyTypeName;
    @JSONField(name = "gistId")
    private String gistId;
    @JSONField(name = "regDate", format = "yyyy年MM月dd日")
    private LocalDate regDate;
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
    @JSONField(name = "disruptTypeName")
    private String disruptTypeName;
    @JSONField(name = "publishDate", format = "yyyy年MM月dd日")
    private LocalDate publishDate;
    @JSONField(name = "qysler")
    private PeopleInfoZxgk[] peopleInfo;
}
