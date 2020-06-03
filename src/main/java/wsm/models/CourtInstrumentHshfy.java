package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtInstrumentHshfy {

    @JSONField(name = "id")
    private String instrumentId;
    @JSONField(name = "案号")
    private String caseCode;
    @JSONField(name = "标题")
    private String theme;
    @JSONField(name = "文书类别")
    private String type;
    @JSONField(name = "案由")
    private String cause;
    @JSONField(name = "承办部门")
    private String courtName;
    @JSONField(name = "级别")
    private String courtLevel;
    @JSONField(name = "结案日期", format = "yyyy-MM-dd")
    private LocalDate caseDue;
    @JSONField(name = "content")
    private String content;
}
