package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;

/**
 * 预存款提现接口 服务类
 * Created by panjie on 2018/2/5.
 */
public interface AppPreDepositWithdrawService {

    /**
     * 新增顾客提现申请
     * @param param 提现申请类
     */
    void cusSave(PreDepositWithdrawParam param);

    /**
     * 新增门店提现申请
     * @param param
     */
    void stSave(PreDepositWithdrawParam param);


}
