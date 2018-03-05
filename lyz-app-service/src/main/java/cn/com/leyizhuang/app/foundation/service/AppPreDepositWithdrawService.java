package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import com.github.pagehelper.PageInfo;

import java.io.UnsupportedEncodingException;

/**
 * 预存款提现接口 服务类
 * Created by panjie on 2018/2/5.
 */
public interface AppPreDepositWithdrawService {

    /**
     * 新增顾客提现申请
     *
     * @param param 提现申请类
     */
    String cusSave(PreDepositWithdrawParam param) throws UnsupportedEncodingException;

    /**
     * 新增门店提现申请
     *
     * @param param
     */
    String stSave(PreDepositWithdrawParam param);


    /**
     * 顾客申请列表
     *
     * @return
     */
    PageInfo<CusPreDepositWithdraw> cusApplyList(Integer page, Integer size, Long cusId);

    /**
     * 门店申请列表
     */
    PageInfo<StPreDepositWithdraw> stApplyList(Integer page, Integer size, Long stId);

    /**
     * 顾客取消申请
     */
    String cusCancelApply(Long applyId, Long cusId);

    /**
     * 门店取消申请
     */
    String stCancelApply(Long applyId, Long stId);


    /******************************************** 后台 ********************************************/

    /**
     * 顾客 预存款提现申请列表
     *
     * @param page
     * @param size
     * @param keywords
     * @param status
     * @return
     */
    PageInfo<CusPreDepositWithdraw> getCusPageInfo(Integer page, Integer size, String keywords, String status,String startDateTime,String endDateTime);


    /**
     * 顾客 通过申请
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void cusApplyPass(Long applyId, ShiroUser shiroUser) throws Exception;

    /**
     * 顾客 驳回
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void cusApplyreject(Long applyId, ShiroUser shiroUser) throws Exception;

    /**
     * 顾客 打款
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void cusApplyRemit(Long applyId, ShiroUser shiroUser) throws Exception;


    /**
     * 门店 预存款提现申请列表
     *
     * @param page
     * @param size
     * @param keywords
     * @param status
     * @return
     */
    PageInfo<StPreDepositWithdraw> getStPageInfo(Integer page, Integer size, String keywords, String status,String startDateTime,String endDateTime);

    /**
     * 门店 通过
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void stApplyPass(Long applyId, ShiroUser shiroUser) throws Exception;

    /**
     * 门店 驳回
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void stApplyreject(Long applyId, ShiroUser shiroUser) throws Exception;

    /**
     * 门店 打款
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    void stApplyRemit(Long applyId, ShiroUser shiroUser) throws Exception;
}
