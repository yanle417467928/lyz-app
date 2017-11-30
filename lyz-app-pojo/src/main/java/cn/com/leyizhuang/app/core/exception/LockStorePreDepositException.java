package cn.com.leyizhuang.app.core.exception;

/**
 *  扣减门店预存款异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockStorePreDepositException extends RuntimeException {

    public LockStorePreDepositException(String message) {
        super(message);
    }
}
