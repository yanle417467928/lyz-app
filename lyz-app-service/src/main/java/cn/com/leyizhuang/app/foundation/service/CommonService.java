package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.vo.UserVO;

/**
 * 通用方法
 *
 * @author Richard
 * Created on 2017-09-12 15:42
 **/
public interface CommonService {

    void saveUserAndUserRoleByUserVO(UserVO userVO);

    void updateUserAndUserRoleByUserVO(UserVO userVO);

    void deleteUserAndUserRoleByUserId(Long uid);

}
