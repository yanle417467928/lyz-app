package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 下料清单
 *
 * @author GenerationRoad
 * @date 2017/10/17
 */
@Setter
@Getter
@ToString
public class MaterialListDO {
    private Long id;
    /**
     * /用户id
     */
    private Long userId;
    /**
     * /身份类型
     */
    private AppIdentityType identityType;
    /**
     * 代下单顾客Id
     */
    private Long cusId;
    /**
     * 商品id
     */
    private Long gid;
    /**
     * /商品编码
     */
    private String sku;
    /**
     * /商品名称
     */
    private String skuName;
    /**
     * /商品规格
     */
    private String goodsSpecification;
    /**
     * 封面图片路径
     */
    private String coverImageUri;
    /**
     * 商品单位
     */
    private String goodsUnit;
    /**
     * 商品数量
     */
    private Integer qty;
    /**
     * 物料审核单号
     */
    private String auditNo;
    /**
     * 加入类型
     */
    private MaterialListType materialListType;

    /**
     * 备注信息
     */
    private String remark;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaterialListDO that = (MaterialListDO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) {
            return false;
        }
        return true;
    }

}
