package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPhotoOrderResponse implements Serializable{

    private String photoOrderNo;
    /**
     * 物料单商品list
     */
    private List<PhotoOrderMaterialListResponse> goodsList;
}
