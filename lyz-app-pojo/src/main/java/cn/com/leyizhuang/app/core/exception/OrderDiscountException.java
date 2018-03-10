package cn.com.leyizhuang.app.core.exception;

/**
 * 订单折扣异常
 *
 * @author Richard
 * Created on 2017/12/08.
 */
public class OrderDiscountException extends RuntimeException {

    private static final long serialVersionUID = 4892663667104593165L;

    public OrderDiscountException(String message) {
        super(message);
    }
}
