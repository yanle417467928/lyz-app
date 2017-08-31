package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.User;
import cn.com.leyizhuang.app.foundation.pojo.vo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.ResourceVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.UserVO;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Richard
 *         Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminUserRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminUserRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/user";

    private final Logger logger = LoggerFactory.getLogger(AppAdminUserRestController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<UserVO> dataUserPageGridGet(Integer offset, Integer size, String keywords) {
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = (offset / size) + 1;
        PageInfo<UserVO> userPage = userService.queryPageVO(page, size);
        List<UserVO> userList = userPage.getList();
        return new GridDataVO<UserVO>().transform(userList,userPage.getTotal());
    }

   /* @PostMapping
    public ResultDTO<?> restResourcePost(Resource resource, BindingResult result) {
        if(!result.hasErrors()){
            resource.setCreateTime(new Date());
            if(resource.getPid() == null){
                resource.setPid(0);
            }
            resourceService.save(resource);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,null);
        }else{
            return actFor400(result);
        }
    }

    protected ResultDTO<?> actFor400(BindingResult result) {
        return super.actFor400(result, "菜单编辑页面提交的数据有误");
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<Resource> restResourceIdGet(@PathVariable(value = "id") Long id) {
        Resource resource = resourceService.queryById(id);
        if (null == resource) {
            logger.warn("查找资源失败：Resource(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,resource);
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<?> restResourceIdPut(@Valid Resource resource, BindingResult result) {
        if (!result.hasErrors()) {
            resourceService.update(resource);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result);
        }
    }

    @DeleteMapping
    public ResultDTO<?> dataResourceDelete(Long[] ids) {
        try {
            for (Long id : ids) {
                Long childrenNumber = resourceService.countByPId(id);
                if (childrenNumber > 0L) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "资源id{"+id+"}下还有挂靠的子资源,不能删除！", null);
                } else {
                    this.resourceService.batchRemove(Arrays.asList(id));
                }
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "资源已成功删除", null);
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
    }*/



}
