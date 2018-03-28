package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * @author liuh
 * @date 2018/03/27
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsGrade {

    private Long id;
    /**
     * 单品档次   0: 低端；1: 中端；2: 高端 ; 3:工程 ; 4:外墙漆; 5:硝基
     */
    private Integer grade;
    /**
     * 商品编号(唯一)
     */
    private String sku;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 分公司id
     */
    private String sobId;

}
