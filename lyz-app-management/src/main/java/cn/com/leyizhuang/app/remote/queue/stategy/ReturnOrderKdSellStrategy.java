package cn.com.leyizhuang.app.remote.queue.stategy;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.KdSell;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 退单金蝶销退货表生成策略实现
 * @Author Richard
 * @Date 2018/5/25 15:35
 */
@Component
public class ReturnOrderKdSellStrategy implements KdSellStrategy {

    @Resource
    private AppSeparateOrderService separateOrderService;

    @Override
    public List<KdSell> generateKdSell(String mainOrderNumber) {
        List<KdSell> kdSellList = separateOrderService.getReturnOrderKdSellByMainOrderNumber(mainOrderNumber);
        return kdSellList;
    }
}
