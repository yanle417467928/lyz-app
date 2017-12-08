package cn.com.leyizhuang.app.core.exception;

/**
 *  订单顾客信息异常
 * @author Richard
 * Created on 2017/12/08.
 */
public class OrderCustomerException extends RuntimeException {

    private static final long serialVersionUID = 4892663667104593165L;

    public OrderCustomerException(String message) {
        super(message);
    }
}
