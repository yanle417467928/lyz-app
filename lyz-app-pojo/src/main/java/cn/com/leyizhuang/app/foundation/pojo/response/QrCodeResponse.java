package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 导购二维码返回类
 * Created by caiyu on 2017/11/30.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeResponse {
    /**
     * 二维码
     */
    private String qrCode;
}
