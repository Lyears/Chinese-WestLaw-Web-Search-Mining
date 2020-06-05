package wsm.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PeopleInfoZxgk {

    @JSONField(name = "cardNum")
    private String cardNum;
    @JSONField(name = "corporationtypename")
    private String corporationtypename;
    @JSONField(name = "iname")
    private String iname;

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
