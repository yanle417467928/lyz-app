package cn.com.leyizhuang.app.core.exception;

/**
 * 订单应付款异常
 *
 * @author Richard
 * Created on 2017/12/08.
 */
public class OrderPayableAmountException extends RuntimeException {

    private static final long serialVersionUID = 4892663667104593165L;

    public OrderPayableAmountException(String message) {
        super(message);
    }
}
