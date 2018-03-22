package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 排名响应类
 * Created by panjie on 2018/3/15.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsRankReponse {

    /**
     * 名次
     */
    private Integer rank;

    /**
     * 人员id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 完成数
     */
    private Integer finishQty;

    /**
     * 组织名称 ： 分公司名称 或者 门店名称
     */
    private String organizationName;


}
