package cn.com.leyizhuang.app.core.exception;

/**
 *  锁定顾客优惠券异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockCustomerCashCouponException extends RuntimeException {

    public LockCustomerCashCouponException(String message) {
        super(message);
    }
}
