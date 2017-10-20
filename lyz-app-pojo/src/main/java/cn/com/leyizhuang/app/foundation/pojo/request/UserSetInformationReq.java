package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile headPic;
    //n昵称
    private String nikeName;
    //姓名
    private String name;
    //性别
    private String sex;
    //生日
    private String birthday;
    //所属城市
//    private String cityName;
    //所属门店
//    private String storeName;
    //所属导购
//    private String guideName;
}
