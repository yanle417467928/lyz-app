package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;

import java.util.List;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferService {

    void TransferArrearsAudit();

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqBySize(int size);

    TdDeliveryInfoDetails queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListBySize(int size);

    List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id);
}
