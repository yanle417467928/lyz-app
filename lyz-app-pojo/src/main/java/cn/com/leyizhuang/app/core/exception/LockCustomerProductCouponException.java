package cn.com.leyizhuang.app.core.exception;

/**
 * 锁定顾客产品券异常
 *
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockCustomerProductCouponException extends RuntimeException {

    public LockCustomerProductCouponException(String message) {
        super(message);
    }
}
