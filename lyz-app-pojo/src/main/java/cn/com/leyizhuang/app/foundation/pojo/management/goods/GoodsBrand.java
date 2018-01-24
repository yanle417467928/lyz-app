package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品品牌
 *
 * @author liuh
 * Created on 2017-11-25 9:54
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
public class GoodsBrand {

    private Long brdId;

    //品牌名称
    private String brandName;

    //品牌编码
    private String brandCode;

    //排序id
    private Long sortId;

    public GoodsBrand() {
    }

}
