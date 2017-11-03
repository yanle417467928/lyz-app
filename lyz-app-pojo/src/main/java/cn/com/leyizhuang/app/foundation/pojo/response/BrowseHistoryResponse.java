package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by caiyu on 2017/11/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrowseHistoryResponse {
    //下料清单id
    private Long id;
    //商品id
    private Long goodsId;
    //商品名称
    private String skuName;
    //商品规格
    private String goodsSpecification;
    //封面图片路径
    private String coverImageUri;
    //商品单位
    private String goodsUnit;
    //商品单价
    private Double retailPrice;
    //浏览时间
    private Date createTime;

}
