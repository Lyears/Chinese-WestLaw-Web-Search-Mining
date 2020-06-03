package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PeopleInfoZXGK {

    @JSONField(name = "cardNum")
    private String cardNum;
    @JSONField(name = "corporationtypename")
    private String corporationtypename;
    @JSONField(name = "iname")
    private String iname;
}
