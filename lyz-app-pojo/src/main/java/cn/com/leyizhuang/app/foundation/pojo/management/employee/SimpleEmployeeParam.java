package cn.com.leyizhuang.app.foundation.pojo.management.employee;

import lombok.*;
/**
 * Created with IntelliJ IDEA.
 * 后台简化员工信息
 *
 * @author liuh
 * Date: 2017/12/24.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEmployeeParam {
    //员工id
    private Long id;
    //姓名
    private String name;
}
