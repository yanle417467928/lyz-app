package cn.com.leyizhuang.app.foundation.pojo.request;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@Getter
@Setter
@ToString
public class BrowseHistoryRequest {

    //用户ID
    private Long userId;
    //身份类型
    private AppIdentityType identityType;
    //商品ID
    private Long goodsId;
    //浏览时间
    private LocalDate createTime;

    public BrowseHistoryRequest setBrowseHistoryRequest(Long userId, Integer identityType, Long goodsId) {
        this.userId = userId;
        this.identityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
        this.goodsId = goodsId;
        this.createTime = LocalDate.now();
        return this;
    }
}
