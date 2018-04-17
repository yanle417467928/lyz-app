package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.Date;

/**
 * 会员等级信息
 * @author GenerationRoad
 * @date 2018/3/6
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CustomerRankInfoResponse {
    private Long cusId;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //等级名称
    private String rankName;
    //等级编码
    private String rankCode;
    //城市id
    private Long cityId;
    //编号
    private String number;
    //创建时间
    private Date createTime;
    //专供图片
    private String rankUrl;

}
