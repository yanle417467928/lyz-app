package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.User;
import cn.com.leyizhuang.app.foundation.pojo.vo.UserVO;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author Richard
 * Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminUserRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminUserRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/user";

    private final Logger logger = LoggerFactory.getLogger(AppAdminUserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    /**
     * 管理员列表分页显示
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<UserVO> dataUserPageGridGet(Integer offset, Integer size, String keywords) {
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = (offset / size) + 1;
        PageInfo<UserVO> userPage = userService.queryPageVO(page, size);
        List<UserVO> userList = userPage.getList();
        return new GridDataVO<UserVO>().transform(userList, userPage.getTotal());
    }

    /**
     * 查看管理员详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<User> restUserIdGet(@PathVariable(value = "id") Long id) {
        User user = userService.queryById(id);
        if (null == user) {
            logger.warn("查找用户详情失败：Resource(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, user);
        }
    }

    @PostMapping
    public ResultDTO<?> restUserPost(@Valid UserVO userVO, @RequestParam(value = "roleIdsStr[]",required = false) String[] roleIdsStr, BindingResult result) {
        if (!result.hasErrors()) {
            userVO.setCreateTime(new Date());
            if(null != roleIdsStr && roleIdsStr.length>0){
                Long[] roleIds = new Long[roleIdsStr.length];
                for (int i = 0; i < roleIdsStr.length; i++) {
                    roleIds[i] = Long.parseLong(roleIdsStr[i]);
                }
                userVO.setRoleIds(roleIds);
            }
            userVO.setPassword(null == userVO.getPassword() ? "123456" : userVO.getPassword());
            commonService.saveUserAndUserRoleByUserVO(userVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    protected ResultDTO<?> actFor400(BindingResult result) {
        return super.actFor400(result, "新增用户页面提交的数据有误");
    }

    @DeleteMapping
    public ResultDTO<?> dataUserDelete(Long[] ids) {
        try {
            for (Long id : ids) {
                User user = userService.queryById(id);
                if (user.getUserType() == 1) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户是超级管理员,不能删除！", null);
                } else {
                    this.userService.delete(id);
                }
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "用户已成功删除", null);
        } catch (InvalidDataException e) {
            logger.error("批量删除资源数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "加载需删除的数据失败，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("批量删除资源数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<?> restUserIdPut(@Valid UserVO userVO, @RequestParam(value = "roleIdsStr[]",required = false) String[] roleIdsStr,BindingResult result) {
        if (!result.hasErrors()) {
            if(null != roleIdsStr && roleIdsStr.length>0){
                Long[] roleIds = new Long[roleIdsStr.length];
                for (int i = 0; i < roleIdsStr.length; i++) {
                    roleIds[i] = Long.parseLong(roleIdsStr[i]);
                }
                userVO.setRoleIds(roleIds);
            }
            commonService.updateUserAndUserRoleByUserVO(userVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }


    /**
     * 验证用户名是否已存在
     *
     * @param loginName
     * @return
     */
    @PostMapping(value = "/loginName/check")
    public ValidatorResultDTO userValidatorLoginNamePost(@RequestParam String loginName) {
        Boolean result;
        result = userService.existsByLoginName(loginName);
        return new ValidatorResultDTO(!result);
    }


}
