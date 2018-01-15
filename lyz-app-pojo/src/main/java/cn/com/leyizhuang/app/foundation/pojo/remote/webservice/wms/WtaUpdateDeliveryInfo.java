package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/13.
 * Time: 11:16.
 */

@Getter
@Setter
@ToString
public class WtaUpdateDeliveryInfo {

    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 开始时间
     */
    private String beginDateTime;
    /**
     * 结束时间
     */
    private String endDateTime;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 操作状态(初始、作业中、完成、结案)
     */
    private String operatorStatus;
    /**
     * 作业人员
     */
    private String operatorUser;
    /**
     * 修改人员
     */
    private String modifiedUserNo;
    /**
     * 委托业主
     */
    private String ownerNo;
    /**
     * 分单号
     */
    private String reserved1;
    /**
     * 送货员
     */
    private String driver;
}
