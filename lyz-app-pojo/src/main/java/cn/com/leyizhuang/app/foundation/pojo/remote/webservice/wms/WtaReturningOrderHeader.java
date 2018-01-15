package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes: WMS~APP退单单头返配上架
 * Created with IntelliJ IDEA.
 * Date: 2018/1/12.
 * Time: 15:24.
 */

@Getter
@Setter
@ToString
public class WtaReturningOrderHeader {


    private Long id;

    /**
     * 仓库编号
     */
    private String whNo;
    /**
     * 委托业主
     */
    private String ownerNo;
    /**
     * 返配验收单号
     */
    private String recNo;

    /**
     * 打印次数
     */
    private Integer printTimes;

    /**
     * 返配单号
     */
    private String backNo;

    /**
     * 返配单类型（一般返配）
     */
    private String backType;

    /**
     * 返配单分类(存储型、越库型)
     */
    private String backClass;

    /**
     * 客户信息
     */
    private String customerNo;

    /**
     * 月台
     */
    private String platNo;

    /**
     * 验收人员
     */
    private String recUser;

    /**
     * 操作工具(表单,pda,电子标签)
     */
    private String opTools;

    /**
     * 操作状态(初始、作业中、完成、结案)
     */
    private String opStatus;

    /**
     * 备注
     */
    private String note;

    /**
     * 建立人员
     */
    private String mkUserno;

    /**
     * 修改人员
     */
    private String modifiedUserno;

    /**
     * 门店退单
     */
    private String poNo;

    /**
     * 开始操作时间
     */
    private Date beginDt;

    /**
     * 结束操作时间
     */
    private Date endDt;

    /**
     * 配送人员
     */
    private String driver;

    /**
     * 建立时间
     */
    private Date mkDt;

    /**
     * 修改时间
     */
    private Date modifiedDt;
}
