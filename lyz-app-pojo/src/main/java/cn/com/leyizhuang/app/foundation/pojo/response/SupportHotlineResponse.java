package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SupportHotlineResponse {
    //成都客服电话
    private final  String cdSupportHotline="028-85989720/028-85989719";
    //郑州客服电话
    private final  String zzSupportHotline="0371-86060877";
    //导购电话
    private String sellerMobile;
}
