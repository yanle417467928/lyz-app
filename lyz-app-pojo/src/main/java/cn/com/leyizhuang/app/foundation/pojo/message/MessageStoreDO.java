package cn.com.leyizhuang.app.foundation.pojo.message;

import lombok.*;

import java.io.Serializable;

/**
 * Created by 王浩 on 2018/07/27.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageStoreDO implements Serializable{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 消息ID
    private Long messageId;

    // 消息代号
    private String messageCode;

    // 门店id
    private Long storeId;

    // 门店编码
    private  String storeCode;

    // 门店标题
    private  String storeTitle;

    // 消息代号
    private String MessageCode;

}
