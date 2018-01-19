package cn.com.leyizhuang.app.foundation.pojo.inventory.requiring;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 19:02.
 */
@Getter
@Setter
@ToString
public class RequiringVO {

    private Long id;
    /**
     * 城市
     */
    private String city;

    /**
     * 要货单号
     */
    private String orderNumber;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 备注信息
     */
    private String remarkInfo;
    /**
     * 下单时间
     */
    private String orderTime;


    public static RequiringVO transform(AtwRequisitionOrder atwRequisitionOrder) {

        RequiringVO requiringVO = new RequiringVO();
        requiringVO.setId(atwRequisitionOrder.getId());
        requiringVO.setCity(atwRequisitionOrder.getCity());
        requiringVO.setOrderNumber(atwRequisitionOrder.getOrderNumber());
        requiringVO.setRemarkInfo(atwRequisitionOrder.getRemarkInfo());
        requiringVO.setOrderTime(atwRequisitionOrder.getOrderNumber());
        requiringVO.setStoreName(atwRequisitionOrder.getDiySiteTitle());

        return requiringVO;
    }

    public static List<RequiringVO> transform(List<AtwRequisitionOrder> requisitionOrderList) {

        List<RequiringVO> requiringVOList;
        if (null != requisitionOrderList && requisitionOrderList.size() > 0) {
            requiringVOList = new ArrayList<>(requisitionOrderList.size());
            requisitionOrderList.forEach(atwRequisitionOrder -> requiringVOList.add(transform(atwRequisitionOrder)));
        } else {
            requiringVOList = new ArrayList<>(0);
        }
        return requiringVOList;
    }
}
