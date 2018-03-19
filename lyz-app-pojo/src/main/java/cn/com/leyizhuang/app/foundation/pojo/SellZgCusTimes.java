package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import lombok.*;

/**
 * 用户参与专供促销次数记录表
 * Created by panjie on 2018/3/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellZgCusTimes {

    private Long id;

    /**
     * 顾客id
     */
    private Long cusId;

    /**
     * sku
     */
    private String sku;

    /**
     * 参与次数
     */
    private Integer times;

    /**
     * 促销标志
     */
    private ActBaseType actBaseType;
}
