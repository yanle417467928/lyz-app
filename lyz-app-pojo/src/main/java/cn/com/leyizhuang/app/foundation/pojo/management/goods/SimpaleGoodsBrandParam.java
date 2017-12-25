package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 后台简化商品品牌
 *
 * @author Richard
 * Created on 2017-09-25 9:54
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SimpaleGoodsBrandParam {

    private Long brdId;

    //品牌名称
    private String brandName;
}
