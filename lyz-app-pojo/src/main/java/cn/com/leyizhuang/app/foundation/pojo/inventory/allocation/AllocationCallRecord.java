package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:门店调拨单通知EBS记录实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:51.
 */
@Getter
@Setter
@ToString
public class AllocationCallRecord {
    private Long id;
    /**
     * 调拨单ID
     */
    private Long allocationId;
    /**
     * 单号
     */
    private String number;

    /**
     * 1:调拨单头； 2:调拨单商品明细；3:调拨单入库
     */
    private Integer type;

    /**
     * 0:成功； 1:失败
     */
    private Integer status;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 主体
     */
    private String content;
    /**
     * 次数
     */
    private Integer times;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
}
