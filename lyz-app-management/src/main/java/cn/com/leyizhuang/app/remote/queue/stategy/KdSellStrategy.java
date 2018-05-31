package cn.com.leyizhuang.app.remote.queue.stategy;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.KdSell;

import java.util.List;

/**
 * @Description: 金蝶销退货表生成策略接口
 * @Author Richard
 * @Date 2018/5/25 15:08
 */
public interface KdSellStrategy {

    List<KdSell> generateKdSell(String mainOrderNumber);
}
