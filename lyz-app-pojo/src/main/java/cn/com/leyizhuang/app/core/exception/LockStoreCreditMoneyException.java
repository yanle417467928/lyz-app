package cn.com.leyizhuang.app.core.exception;

/**
 *  扣减门店（装饰公司）信用额度异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockStoreCreditMoneyException extends RuntimeException {

    public LockStoreCreditMoneyException(String message) {
        super(message);
    }
}
