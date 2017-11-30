package cn.com.leyizhuang.app.core.exception;

/**
 *  扣减导购信用额度异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockEmpCreditMoneyException extends RuntimeException {

    public LockEmpCreditMoneyException(String message) {
        super(message);
    }
}
