package cn.com.leyizhuang.app.core.exception;

/**
 *  更新门店库存异常
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class LockStoreInventoryException extends RuntimeException {

    public LockStoreInventoryException(String message) {
        super(message);
    }
}
