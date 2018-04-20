package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * @Description: ${todo}
 * @Author Richard
 * @Date 2018/4/1613:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FitOrderExcelModel {

    private String externalCode;

    private Integer qty;

    private String internalCode;

    private String internalName;
}
