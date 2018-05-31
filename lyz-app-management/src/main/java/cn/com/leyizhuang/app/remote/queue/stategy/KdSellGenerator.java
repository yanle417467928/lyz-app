package cn.com.leyizhuang.app.remote.queue.stategy;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.KdSell;

import java.util.List;

/**
 * @Description: 金蝶销退货执行类
 * @Author Richard
 * @Date 2018/5/25 15:38
 */
public class KdSellGenerator {

    private KdSellStrategy sellStrategy;

    /**
     * 构造函数，传入一个具体的策略对象
     *
     * @param sellStrategy 具体的策略对象
     */
    public KdSellGenerator(KdSellStrategy sellStrategy) {
        this.sellStrategy = sellStrategy;
    }


    /**
     * 生成 金蝶销退货表
     *
     * @param mainOrderNumber 相关单号
     * @return 金蝶销退货表对象
     */
    public List<KdSell> generate(String mainOrderNumber) {
        return this.sellStrategy.generateKdSell(mainOrderNumber);
    }
}
