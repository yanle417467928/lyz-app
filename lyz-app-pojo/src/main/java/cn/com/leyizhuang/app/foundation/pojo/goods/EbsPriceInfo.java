package cn.com.leyizhuang.app.foundation.pojo.goods;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品价格表
 *
 * @author Richard
 * Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EbsPriceInfo {


    private String STRTABLE;

    private String STOREID;

    private String ERP;

}
