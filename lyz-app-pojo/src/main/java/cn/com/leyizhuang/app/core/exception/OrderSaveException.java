package cn.com.leyizhuang.app.core.exception;

/**
 *  存储订单相关信息异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class OrderSaveException extends RuntimeException {

    private static final long serialVersionUID = 4892663667104593165L;

    public OrderSaveException(String message) {
        super(message);
    }
}
