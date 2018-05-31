package cn.com.leyizhuang.app.core.constant;

/**
 * Richard
 *
 * @author App 分布式锁类型
 * Created on 2017-09-20 15:38
 **/
public class AppLock {

    public static final String SELF_TAKE_ORDER_RECEIPT = "REDIS_LOCK_SELF_TAKE_ORDER_RECEIPT";

    public static final String REFUSE_ORDER = "REDIS_LOCK_REFUSE_ORDER";

    public static final String CANCEL_ORDER = "REDIS_LOCK_CANCEL_ORDER";

    public static final String NORMAL_RETURN = "REDIS_LOCK_NORMAL_RETURN";

    /**
     * 返配上架
     */
    public static final String BACK_ORDER = "REDIS_LOCK_BACK_ORDER";

}
