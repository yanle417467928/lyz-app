package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/29
 */
@Setter
@Getter
@ToString
public class GoodsDetailResponse {
    //产品封面图路径
    private List<String> coverImageUriList;
    //商品轮播图路径
    private List<String> rotationImageUriList;

    private Long id;
    //商品名称
    private String skuName;
    //商品单位
    private String goodsSpecification;
    //商品单位
    private String goodsUnit;
    //商品单价
    private Double price;
    //是否收藏商品
    private Boolean isCollect;

    public static final GoodsDetailResponse transform(GoodsDetailResponse goodsDetailResponse, GoodsDO goodsDO) {
        if (null != goodsDO) {
            if (null == goodsDetailResponse) {
                goodsDetailResponse = new GoodsDetailResponse();
            }
            String coverImageUri = goodsDO.getDetailsImageUri();
            String rotationImageUri = goodsDO.getRotationImageUri();
            if (null != coverImageUri) {
                List<String> list = new ArrayList<>();
                String uri[] = coverImageUri.split(",");
                for (int i = 0; i < uri.length; i++) {
                    list.add(uri[i]);
                }
                goodsDetailResponse.setCoverImageUriList(list);
            }
            if (null != rotationImageUri) {
                List<String> list = new ArrayList<>();
                String uri[] = rotationImageUri.split(",");
                for (int i = 0; i < uri.length; i++) {
                    list.add(uri[i]);
                }
                goodsDetailResponse.setRotationImageUriList(list);
            }
            return goodsDetailResponse;
        } else {
            return null;
        }
    }
}
