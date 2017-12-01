package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActStoreDO implements Serializable{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 门店id
    private Long storeId;

    // 门店编码
    private  String storeCode;

    // 门店标题
    private  String storeTilte;

}
