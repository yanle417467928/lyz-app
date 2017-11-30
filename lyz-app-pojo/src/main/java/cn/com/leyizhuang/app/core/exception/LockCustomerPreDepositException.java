package cn.com.leyizhuang.app.core.exception;

/**
 *  扣减顾客预存款异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockCustomerPreDepositException extends RuntimeException {

    public LockCustomerPreDepositException(String message) {
        super(message);
    }
}
