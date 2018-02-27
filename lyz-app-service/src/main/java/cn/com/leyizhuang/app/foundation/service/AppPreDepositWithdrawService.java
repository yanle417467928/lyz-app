package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import com.github.pagehelper.PageInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 预存款提现接口 服务类
 * Created by panjie on 2018/2/5.
 */
public interface AppPreDepositWithdrawService {

    /**
     * 新增顾客提现申请
     * @param param 提现申请类
     */
    void cusSave(PreDepositWithdrawParam param) throws UnsupportedEncodingException;

    /**
     * 新增门店提现申请
     * @param param
     */
    void stSave(PreDepositWithdrawParam param);


    /**
     * 顾客申请列表
     * @return
     */
    PageInfo<CusPreDepositWithdraw> cusApplyList(Integer page,Integer size,Long cusId);

    /**
     * 门店申请列表
     */
    PageInfo<StPreDepositWithdraw> stApplyList(Integer page, Integer size, Long stId);

    /**
     * 顾客取消申请
     */
    void cusCancelApply(Long applyId,Long cusId);

    /**
     * 门店取消申请
     */
    void stCancelApply(Long applyId,Long stId);


    /******************************************** 后台 ********************************************/

    /**
     * 顾客 预存款提现申请列表
     * @param page
     * @param size
     * @param keywords
     * @param status
     * @return
     */
    PageInfo<CusPreDepositWithdraw> getCusPageInfo(Integer page, Integer size, String keywords, String status);

    /**
     * 门店 预存款提现申请列表
     * @param page
     * @param size
     * @param keywords
     * @param status
     * @return
     */
    PageInfo<StPreDepositWithdraw> getStPageInfo(Integer page, Integer size, String keywords, String status);
}
