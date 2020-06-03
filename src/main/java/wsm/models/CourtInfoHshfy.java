package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourtInfoHshfy {

    @JSONField(name = "案号")
    private String caseCode;

    @JSONField(name = "被执行人")
    private String iname;

    @JSONField(name = "被执行人地址")
    private String address;

    @JSONField(name = "执行标的金额（元）")
    private String money;

    @JSONField(name = "申请执行人")
    private String applicant;

    @JSONField(name = "承办法院、联系电话")
    private String courtAndPhone;

}
