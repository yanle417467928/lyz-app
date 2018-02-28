package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes:WMS~APP退单返配上架商品明细
 * Created with IntelliJ IDEA.
 * Date: 2018/1/12.
 * Time: 15:24.
 */

@Getter
@Setter
@ToString
public class WtaReturningOrderGoods {
    private Long id;

    /**
     * 任务编号
     */
    private String recNo;

    /**
     * 任务id
     */
    private String recId;

    /**
     * 商品编号
     */
    private String gcode;
    /**
     * 验收数量
     */
    private Integer recQty;
}
