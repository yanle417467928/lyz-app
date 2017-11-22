package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 获取可选自提门店列表返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SelfTakeStoreResponse implements Serializable{

    //是否允许门店自提
    private Boolean isSelfTakePermitted;



    private Boolean isExist;
    //用户id
    private Long userId;
    //用户电话
    private String mobile;

    private Long cityId;

}
