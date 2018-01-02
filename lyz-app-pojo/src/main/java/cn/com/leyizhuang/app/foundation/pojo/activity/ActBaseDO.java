package cn.com.leyizhuang.app.foundation.pojo.activity;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.core.constant.ActConditionType;
import cn.com.leyizhuang.app.core.constant.ActPromotionType;
import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 促销基础实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActBaseDO{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销代号
    private String actCode;

    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 标题
    private String title;

    // 活动开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    // 活动结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    // 城市id
    private Long cityId;

    // 城市名称
    private String cityName;

    // 活动目标对象
    private String actTarget;

    // 促销基本类型
    private ActBaseType baseType;

    // 结果类型
    private ActPromotionType promotionType;

    // 条件类型
    private ActConditionType conditionType;

    // 是否叠加
    private Boolean isDouble;

    // 赠品选择最大数量
    private Integer giftChooseNumber;

    // 满足额度
    private Double fullAmount;

    // 满足数量
    private Integer fullNumber;

    // 加价购需要加价金额
    private Double addAmount;

    // 是否可退货
    private Boolean isReturnable;

    // 工程单可享受
    private Boolean isGcOrder = false;

    // 本品数量任选
    private Boolean isGoodsOptionalQty;

    // 赠品数量任选
    private Boolean isGiftOptionalQty;

    // 促销状态
    private ActStatusType status;

    // 排序号
    private Integer sortId;

    /**
     * 获取最终促销类型
     */
    public String getActType(){
        return this.baseType.getValue()+"_"+this.conditionType.getValue()+"_"+this.promotionType.getValue();
    }

    /**
     * 根据促销类型+当前时间+随机数,生成一个促销编码
     */
    public String createCode(){
        String code = this.baseType.getValue().substring(0,1)+this.conditionType.getValue().substring(0,2)+this.promotionType.getValue().substring(0,1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String now = LocalDateTime.now().format(format);
        Random random = new Random();
        String suiji = random.nextInt(900)+100+"";
        return  code+now+suiji;
    }
}
