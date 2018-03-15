package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品券模版
 * Created by panjie on 2018/1/10.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 产品id
     */
    private Long gid;
    /**
     *  面额
     */
    private Double denomination;

    /**
     * 有效期开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;

    /**
     * 有效期结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;

    /**
     * 使用说明
     */
    private String description;

    /**
     * 初始数量
     */
    private Integer initialQuantity;

    /**
     *  剩余数量
     */
    private Integer remainingQuantity;

    /**
     * 优惠券标题
     */
    private String title;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;


    /**
     * 指定门店
     */
    private Long storeId;

    /**
     * 导购id
     */
    private Long sellerId;

    /**
     * 状态 0：删除 1：正常
     */
    private Boolean status = true;

    /**
     * 排序号
     */
    private Integer sortId;
}
