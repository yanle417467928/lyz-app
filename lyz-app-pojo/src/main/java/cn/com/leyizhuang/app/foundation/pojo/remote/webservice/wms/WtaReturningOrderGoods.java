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
     * 委托业主
     */
    private String ownerNo;

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
     * 包装数量
     */
    private String packQty;

    /**
     * 价格
     */
    private String price;

    /**
     * 验收赠品数量
     */
    private String giftQty;

    /**
     * 验收赠品不良品数量
     */
    private String badQty;

    /**
     * 验收数量
     */
    private String recQty;

    /**
     * 作业人员
     */
    private String recUser;

    /**
     * 月台
     */
    private String platNo;

    /**
     * 操作工具(表单,pda,电子标签)
     */
    private String opTools;

    /**
     * 状态（初始、作业中、完成、结案）
     */
    private String opStatus;

    // 预留
    private String reserved1;

    // 预留
    private String reserved2;

    // 预留
    private String reserved3;

    // 预留
    private String reserved4;

    // 预留
    private String reserved5;

    // 备注
    private String note;

    // 建立人员
    private String mkUserno;

    // 修改人员
    private String modifiedUserno;

    // 建立时间
    private String mkDt;

    // 修改时间
    private String modifiedDt;
}
