package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
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
public class GoodsImageUriResponse {
    //产品封面图路径
    private List<String> coverImageUriList;
    //商品轮播图路径
    private List<String> rotationImageUriList;


    public static final GoodsImageUriResponse transform(GoodsDO goodsDO) {
        if (null != goodsDO) {
            GoodsImageUriResponse goodsImageUriResponse = new GoodsImageUriResponse();
            String coverImageUri = goodsDO.getCoverImageUri();
            String rotationImageUri = goodsDO.getRotationImageUri();
            if (null != coverImageUri) {
                List<String> list = new ArrayList<>();
                String uri[] = coverImageUri.split(",");
                for (int i = 0; i < uri.length; i++) {
                    list.add(uri[i]);
                }
                goodsImageUriResponse.setCoverImageUriList(list);
            }
            if (null != rotationImageUri) {
                List<String> list = new ArrayList<>();
                String uri[] = rotationImageUri.split(",");
                for (int i = 0; i < uri.length; i++) {
                    list.add(uri[i]);
                }
                goodsImageUriResponse.setRotationImageUriList(list);
            }
            return goodsImageUriResponse;
        } else {
            return null;
        }
    }
}
