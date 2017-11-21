package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by caiyu on 2017/11/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditGoPayResponse {
    //物料单编号
    private String auditNo;
    //物料单商品list
    private List<MaterialListResponse> goodsList;
    //提交人姓名
    private String worker;

}
