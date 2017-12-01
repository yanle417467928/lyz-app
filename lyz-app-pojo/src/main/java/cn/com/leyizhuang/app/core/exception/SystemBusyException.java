package cn.com.leyizhuang.app.core.exception;

/**
 *  系统繁忙异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class SystemBusyException extends RuntimeException {

    public SystemBusyException(String message) {
        super(message);
    }
}
