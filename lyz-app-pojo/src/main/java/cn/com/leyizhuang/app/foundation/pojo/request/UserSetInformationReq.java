package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/19.
 * Time: 9:02.
 */

@Setter
@Getter
@ToString
public class UserSetInformationReq {

    //用户ID
    private Long userId;
    //身份类型
    private Integer identityType;
    //头像地址
    private String picUrl;
    //姓名
    private String name;
    //性别
    private String sex;
    //生日
    private Date birthday;
    //电话
    private String mobile;
    //所属城市
//    private String cityName;
    //所属门店
//    private String storeName;
    //所属导购
//    private String guideName;
}
