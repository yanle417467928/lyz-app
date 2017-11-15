package cn.com.leyizhuang.app.foundation.pojo.goods;

import lombok.*;

/**
 * 商品类型
 *
 * @author Richard
 * Created on 2017-09-25 9:54
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsType {

    private Long gtid;

    //类型名称
    private String typeName;

    //类型编码
    private String typeCode;

    //排序id
    private Long sortId;

}
