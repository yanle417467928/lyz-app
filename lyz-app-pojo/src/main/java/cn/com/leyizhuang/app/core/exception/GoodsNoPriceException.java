package cn.com.leyizhuang.app.core.exception;

/**
 * 商品无价格异常
 *
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class GoodsNoPriceException extends RuntimeException {

    public GoodsNoPriceException(String message) {
        super(message);
    }
}
