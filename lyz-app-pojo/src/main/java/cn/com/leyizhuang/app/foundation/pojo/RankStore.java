package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2018/5/3.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RankStore {
    /**
     * 自增id
     */
    private Long id;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 分公司编码
     */
    private String companyCode;
    /**
     * 分公司名称
     */
    private String companyName;
    /**
     * 创建时间
     */
    private Date createTime;



}
