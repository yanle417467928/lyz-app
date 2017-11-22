package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

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
@NoArgsConstructor
public class SelfTakeStoreResponse implements Serializable{

    private static final long serialVersionUID = -8432769934421527145L;
    /**
     * 是否允许门店自提
     */
    private Boolean isSelfTakePermitted;

    /**
     * 自提门店列表
     */
    private List<SelfTakeStore> storeList;


}
