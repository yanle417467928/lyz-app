package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.List;

/**
 * @Description: 装饰公司下单订单excel抽象
 * @Author Richard
 * @Date 2018/4/1613:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FitOrderExcel {

    private String remark;

    List<FitOrderExcelModel> excelModelList;
}
