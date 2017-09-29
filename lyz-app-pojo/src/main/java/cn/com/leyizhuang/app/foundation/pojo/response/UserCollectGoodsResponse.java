package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 获取个人商品收藏列表接口实体
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 16:41.
 */

@Getter
@Setter
@ToString
public class UserCollectGoodsResponse implements Serializable {

    //商品ID
    private Long id;

    //商品名称
    private String goodsName;

    //封面图片
    private String coverImageUri;

    //商品规格
    private  String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //会员价
    private Double memberPrice;

    //零售价
    private Double retailPrice;

    //二级分类编码
    private String secondCategoryCode;

    //商品品牌分类编码
    private String goodsBrandCode;

    //商品类型分类编码
    private String goodsTypeCode;
}
