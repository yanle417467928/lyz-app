package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
public class UserHomePageResponse implements Serializable {

    //头像地址
    private String picUrl;
    //姓名
    private String name;
    //员工编码
    private String number;
    //所属导购
    private String guideName;
    //所属导购ID
    private String  guideMobile;

}
