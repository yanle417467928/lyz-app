package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 获取商品规格接口返回值
 *
 * @author liuh
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class GoodsSpecificationResponse implements Serializable {

    private Long specificationId;

    private String specificationName;
}
