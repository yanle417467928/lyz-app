package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.FitExcelImportGoodsErrorType;
import lombok.*;

/**
 * @Description: excel解析结果返回页面dataTable对象
 * @Author Richard
 * @Date 2018/4/1613:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FitOrderExcelPageVO {

    /**
     * 外部编码
     */
    private String externalCode;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 内部编码
     */
    private String internalCode;

    /**
     * 内部名称
     */
    private String internalName;

    /**
     * 城市库存余量
     */
    private Integer inventory;

    /**
     * 库存差额 = 库存-数量
     */
    private Integer invDifference;

    /**
     * 商品是否存在
     */
    private Boolean isInternalCodeExists;

    /**
     * 库存是否充足
     */
    private Boolean isInvEnough;

    /**
     * 价目表是否存在
     */
    private Boolean isPriceItemExists;

    /**
     * 错误信息
     */
    private FitExcelImportGoodsErrorType errorType;
}
