package cn.com.leyizhuang.app.core.exception;

/**
 *  更新城市库存异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockCityInventoryException extends RuntimeException {

    public LockCityInventoryException(String message) {
        super(message);
    }
}
