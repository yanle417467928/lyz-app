package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品业务实体
 *
 * @author Richard
 *         Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
public class Goods extends BaseDO {

    //商品名称
    private String goodsName;
    //商品编码
    private String goodsCode;
}
