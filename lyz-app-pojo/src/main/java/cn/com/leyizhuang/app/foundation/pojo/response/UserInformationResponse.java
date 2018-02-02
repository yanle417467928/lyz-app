package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 个人设置页面响应实体
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/27.
 * Time: 14:34.
 */
@Getter
@Setter
@ToString
public class UserInformationResponse implements Serializable {

    //头像地址
    private String picUrl;
    //姓名
    private String name;
    //昵称
    private String nikeName;
    //性别
    private String sex;
    //生日
    private String birthday;
    //电话
    private String mobile;
    //所属城市
    private String cityName;
    //所属门店
    private String storeName;
    //所属导购
    private String guideMobile;
    //工种
    private String profession;
}
