package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 *  城市配送时间返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class CityDeliveryTimeResponse implements Serializable{

    private static final long serialVersionUID = -7400298636440255384L;


    private String day;

    private List<String> deliveryTime;
}
