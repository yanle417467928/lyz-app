package cn.com.leyizhuang.app.foundation.pojo.response;

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

    //头像地址
    private String picUrl;
    //姓名
    private String name;
    //所属导购
    private String guideName;
    //所属导购电话
    private String  guideMobile;
    //灯号
    private String light;
    //乐币数量
    private Integer lb;
    //账户余额
    private Double balance;
    //上次签到时间
    private Date lastSignTime;
    //是否可以签到
    private Boolean canSign;

}
