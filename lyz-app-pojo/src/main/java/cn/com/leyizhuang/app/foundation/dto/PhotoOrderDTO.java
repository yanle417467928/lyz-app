package cn.com.leyizhuang.app.foundation.dto;

import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhotoOrderDTO implements Serializable {
    //拍照下单ID
    private Long photoId;

    //商品列表
    private List<MaterialListDO> combList;

}
