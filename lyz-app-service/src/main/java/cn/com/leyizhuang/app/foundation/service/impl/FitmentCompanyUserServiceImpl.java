package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.FitmentCompanyUserDAO;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyUserDO;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.dto.FitmentCompanyUserDTO;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import cn.com.leyizhuang.app.core.utils.TimeTransformUtils;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyUserService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
@Service
@Transactional
public class FitmentCompanyUserServiceImpl extends BaseServiceImpl<FitmentCompanyUserDO> implements FitmentCompanyUserService {
    private FitmentCompanyUserDAO fitmentCompanyUserDAO;

    @Autowired
    private CommonService commonServiceImpl;

    @Autowired
    private UserService userServiceImpl;

    public FitmentCompanyUserServiceImpl(FitmentCompanyUserDAO fitmentCompanyUserDAO) {
        super(fitmentCompanyUserDAO);
        this.fitmentCompanyUserDAO = fitmentCompanyUserDAO;
    }

    @Override
    public PageInfo<FitmentCompanyUserDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<FitmentCompanyUserDO> fitmentCompanyUserDOList = this.fitmentCompanyUserDAO.queryList();
        return new PageInfo<>(fitmentCompanyUserDOList);
    }

    @Override
    public FitmentCompanyUserDO managerModifyCompanyUser(FitmentCompanyUserDTO fitmentCompanyUserDTO) {
        FitmentCompanyUserDO fitmentCompanyUserDO = transform(fitmentCompanyUserDTO);
        fitmentCompanyUserDO.setModifierInfoByManager(0L);

        User user = transform(fitmentCompanyUserDO);
        this.userServiceImpl.update(user);
        this.fitmentCompanyUserDAO.modify(fitmentCompanyUserDO);
        return fitmentCompanyUserDO;
    }

    @Override
    public FitmentCompanyUserDO managerSaveCompanyUser(FitmentCompanyUserDTO fitmentCompanyUserDTO) {
        FitmentCompanyUserDO fitmentCompanyUserDO = transform(fitmentCompanyUserDTO);
        fitmentCompanyUserDO.setCreatorInfoByManager(0L);
        UserVO user = transform1(fitmentCompanyUserDO);
        this.commonServiceImpl.saveUserAndUserRoleByUserVO(user);
        this.fitmentCompanyUserDAO.save(fitmentCompanyUserDO);
        return fitmentCompanyUserDO;
    }

    private FitmentCompanyUserDO transform(FitmentCompanyUserDTO fitmentCompanyUserDTO) {
        FitmentCompanyUserDO fitmentCompanyUserDO = null;
        if (null != fitmentCompanyUserDTO) {
            fitmentCompanyUserDO = new FitmentCompanyUserDO();
            Long id = fitmentCompanyUserDTO.getId();
            if (null != id && id != 0) {
                fitmentCompanyUserDO.setId(id);
            }
            fitmentCompanyUserDO.setUserName(fitmentCompanyUserDTO.getUserName());
            fitmentCompanyUserDO.setMobile(fitmentCompanyUserDTO.getMobile());
            fitmentCompanyUserDO.setIsMain(fitmentCompanyUserDTO.getIsMain());
            fitmentCompanyUserDO.setCompanyId(fitmentCompanyUserDTO.getCompanyId());
            fitmentCompanyUserDO.setSex(fitmentCompanyUserDTO.getSex());
            fitmentCompanyUserDO.setAge(fitmentCompanyUserDTO.getAge());
            fitmentCompanyUserDO.setFrozen(fitmentCompanyUserDTO.getFrozen());
        }
        return fitmentCompanyUserDO;
    }

    private UserVO transform1(FitmentCompanyUserDO fitmentCompanyUserDO){
        UserVO user = null;
        if (null != fitmentCompanyUserDO){
            user = new UserVO();
            user.setCreateTime(TimeTransformUtils.localDateTimeToDate(fitmentCompanyUserDO.getCreateTime()));
            user.setUserType(2);
            user.setPassword("123456");
            user.setLoginName(fitmentCompanyUserDO.getMobile());
            user.setAge(fitmentCompanyUserDO.getAge());
            user.setName(fitmentCompanyUserDO.getUserName());
            user.setPhone(fitmentCompanyUserDO.getMobile());
            if (fitmentCompanyUserDO.getSex()){
                user.setSex(SexType.MALE);
            } else if (!fitmentCompanyUserDO.getSex()){
                user.setSex(SexType.FEMALE);
            } else {
                user.setSex(SexType.SECRET);
            }
            user.setStatus(!fitmentCompanyUserDO.getFrozen());

        }
        return user;
    }

    private User transform(FitmentCompanyUserDO fitmentCompanyUserDO) {

        User user = null;
        UserVO userVO = null;
        if (null != fitmentCompanyUserDO){
            user = new User();
            userVO = new UserVO();
            if (null != fitmentCompanyUserDO.getId() || fitmentCompanyUserDO.getId() != 0){
                FitmentCompanyUserDO fitmentCompanyUserDO1 = this.fitmentCompanyUserDAO.queryById(fitmentCompanyUserDO.getId());
                if (null != fitmentCompanyUserDO1){
                    userVO.setLoginName(fitmentCompanyUserDO1.getMobile());
                }
            }
            user = this.userServiceImpl.queryByLoginName(userVO);
            if (null != user) {
                user.setAge(fitmentCompanyUserDO.getAge());
                user.setName(fitmentCompanyUserDO.getUserName());
                user.setMobile(fitmentCompanyUserDO.getMobile());
                user.setLoginName(fitmentCompanyUserDO.getMobile());
                if (fitmentCompanyUserDO.getSex()) {
                    user.setSex(SexType.MALE);
                } else if (!fitmentCompanyUserDO.getSex()) {
                    user.setSex(SexType.FEMALE);
                } else {
                    user.setSex(SexType.SECRET);
                }
                user.setStatus(!fitmentCompanyUserDO.getFrozen());
            }

        }
        return user;
    }
}

