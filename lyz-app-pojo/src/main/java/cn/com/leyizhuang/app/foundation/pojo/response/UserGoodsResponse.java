package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 获取通用个人商品接口实体
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 16:41.
 */

@Getter
@Setter
@ToString
public class UserGoodsResponse implements Serializable {

    private static final long serialVersionUID = -648160133580382322L;

    //商品ID
    private Long id;

    //商品名称
    private String goodsName;

    //商品sku
    private String sku;

    //封面图片
    private String coverImageUri;

    //商品规格
    private String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //会员价
    private Double vipPrice;

    //零售价
    private Double retailPrice;

    //二级分类编码id
    private Long categoryId;

    //商品品牌分类编码id
    private Long brandId;

    //商品类型分类编码 id
    private Long typeId;

    //下料清单商品数量
    private Integer materialQty;

    // 施工面积
    private String workArea;

    // 施工成不
    private String workCost;

//    public void setCoverImageUri(String coverImageUri){
//        String[] urlArr = coverImageUri.split(",");
//        if (urlArr != null || urlArr.length > 0){
//            // 多张图片默认取第一张
//            this.coverImageUri = urlArr[0];
//        }
//    }
}
