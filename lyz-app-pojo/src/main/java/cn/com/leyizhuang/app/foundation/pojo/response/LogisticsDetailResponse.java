package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 物流详情放回类
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsDetailResponse {

    private String describe;

    private String logisticsType;

    private String createTime;
}
