package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人主页(我的)页面信息响应实体
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 10:29.
 */

@Getter
@Setter
@ToString
public class CustomerHomePageResponse implements Serializable {

    /**
     *  头像地址
     */
    private String picUrl;
    /**
     * 姓名
     */
    private String name;
    /**
     *  顾客电话
     */
    private String mobile;
    /**
     * 所属导购
     */
    private String guideName;
    /**
     * 所属导购电话
     */
    private String guideMobile;
    /**
     * 灯号
     */
    private String light;
    /**
     * 乐币数量
     */
    private Integer lb;
    /**
     * 账户余额
     */
    private Double balance;
    /**
     * 上次签到时间
     */
    private Date lastSignTime;
    /**
     * 是否可以签到
     */
    private Boolean canSign;
    /**
     *  城市ID
     */
    private Long cityId;
    /**
     * 优惠券数量
     */
    private Integer cashCouponQty;
    /**
     * 产品券数量
     */
    private Integer productCouponQty;
    /**
     * 顾客类型
     */
    private String customerType;

    public void setCustomerType(AppCustomerType customerType) {
        this.customerType = customerType.getDescription();
    }


}
