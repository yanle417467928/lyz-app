package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * 自提门店对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SelfTakeStore implements Serializable {

    private static final long serialVersionUID = -2500829151422966556L;
    private Long storeId;
    private String storeName;
    private String detailedAddress;
}
