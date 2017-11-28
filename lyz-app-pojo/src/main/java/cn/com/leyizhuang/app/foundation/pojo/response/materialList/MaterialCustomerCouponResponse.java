package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:顾客的产品券在导购下料清单中显示对象
 * Created with IntelliJ IDEA.
 * Date: 2017/11/27.
 * Time: 9:39.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCustomerCouponResponse implements Serializable {

    /**
     * 顾客电话
     */
    private String mobile;
    /**
     * 顾客姓名
     */
    private String customer;
    /**
     * 顾客的产品券
     */
    private List<CouponMaterialListResponse> couponsList;
}
