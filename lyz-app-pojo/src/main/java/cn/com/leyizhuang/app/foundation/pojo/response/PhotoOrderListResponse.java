package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/28
 */
@Getter
@Setter
@ToString
public class PhotoOrderListResponse {

    private Long id;
    //下单照片，多张照片用逗号分隔
    private List<String> photos;
    //创建时间
    private String createTime;
    //状态（待处理、已下单、已支付、完成）
    private String status;
    //拍照下单单号
    private String photoOrderNo;

    public void setStatus(PhotoOrderStatus status){
        this.status = status.getValue();
    }

    public void setPhotos(String photos){
        this.photos = new ArrayList<>();
        String uri[] = photos.split(",");
        for (int i = 0; i < uri.length; i++) {
            this.photos.add(uri[i]);
        }
    }

}
