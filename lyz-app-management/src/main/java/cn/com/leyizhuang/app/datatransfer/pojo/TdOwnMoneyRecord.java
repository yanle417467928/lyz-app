package cn.com.leyizhuang.app.datatransfer.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/24
 */
@Getter
@Setter
@ToString
public class TdOwnMoneyRecord {

    private Long id;

    // 用户名
    private String username;

    // 订单号(主单号)
    private String orderNumber;

    // 已付 (配送员收款)
    private Double payed;

    // 欠款
    private Double owned;

    // 创建时间
    private Date createTime;

    // 排序号
    private Long sortId;

    // 是否审批通过
    private Boolean isEnable;

    // 是否还清
    private Boolean isPayed;

    // 门店编码
    private String diyCode;

    // 是否审核 0:不同过，1：通过
    private Boolean ispassed;

    //是否是欠款记录 （ 后面添加的字段 默认为true表示为欠款记录） zp
    private Boolean isOwn;
    //收款现金（配送员）
    private Double money;
    //收款pos（配送员）
    private Double pos;
    //还款现金（门店）
    private Double backMoney;
    //还款pos（门店）
    private Double backPos;
    //POS刷卡流水号
    private String serialNumber;

    // 新增：2016-08-25增加还款方式其他（门店）
    private Double backOther;

    //收款时间
    private Date payTime;

    //还款时间
    private Date ownTime;

    // 实际收款时间
    private Date realPayTime;
}
