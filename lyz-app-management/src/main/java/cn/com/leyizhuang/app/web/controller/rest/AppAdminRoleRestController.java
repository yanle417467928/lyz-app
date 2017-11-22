package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
import cn.com.leyizhuang.app.foundation.service.RoleResourceService;
import cn.com.leyizhuang.app.foundation.service.RoleService;
import cn.com.leyizhuang.app.foundation.vo.ResourceVO;
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
import java.util.List;
import java.util.Set;

/**
 * @author Richard
 * Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminRoleRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminRoleRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/role";

    private final Logger logger = LoggerFactory.getLogger(AppAdminRoleRestController.class);

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<Role> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {

        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = (offset / size) + 1;
        PageInfo<Role> rolePage = roleService.queryPage(page, size);
        List<Role> roleList = rolePage.getList();
        return new GridDataVO<Role>().transform(roleList, rolePage.getTotal());
    }

    @PostMapping
    public ResultDTO<?> restMenuPost(Role role, BindingResult result) {
        if (!result.hasErrors()) {
            roleService.save(role);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result);
        }
    }

    @PostMapping(value = "/grant")
    public ResultDTO<?> roloGrant(Long id, @RequestParam(value = "resourceIds[]") String[] resourceIds) {
        if (id != null) {
            roleService.updateRoleResource(id, resourceIds);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "授权成功", null);
    }

    @PostMapping(value = "/showResource")
    public ResultDTO<?> showResource(Long roleId) {
        StringBuffer html = new StringBuffer();
        if (roleId != null) {
            List<ResourceVO> resourceVOList = roleService.findAllResource();
            Set<Long> resouceIdSet = roleResourceService.findResouceIdSetByRoleId(roleId);
            System.out.println(resouceIdSet);
            html.append("<select id=\"authorifyselect\" multiple=\"multiple\">");
            for (ResourceVO vo : resourceVOList) {
                if (resouceIdSet.contains(vo.getId())) {
                    html.append("<option  value=" + vo.getId() + " data-section=" + vo.getParentResourceName() + " data-description=" + vo.getResourceDescription() + " selected>" + vo.getResourceName() + "</option>");
                } else {
                    html.append("<option  value=" + vo.getId() + " data-section=" + vo.getParentResourceName() + " data-description=" + vo.getResourceDescription() + ">" + vo.getResourceName() + "</option>");
                }
            }
            html.append("</select>");
            System.out.println(html);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, html);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前角色不存在!", html);

    }


    protected ResultDTO<?> actFor400(BindingResult result) {
        return super.actFor400(result, "菜单编辑页面提交的数据有误");
    }


    @DeleteMapping
    public ResultDTO<?> dataRoleDelete(Long[] ids) {
        try {
            for (Long id : ids) {
                Role role = roleService.queryById(id);
                if(role.getName().contains("admin")||role.getName().contains("超级管理员")){
                    logger.error("超级管理员角色不允许删除!");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                            "超级管理员角色不允许删除", null);
                }
                this.roleService.batchRemove(Arrays.asList(id));
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "角色以及用户-角色关联已删除！", null);
        } catch (InvalidDataException e) {
            logger.error("批量删除菜单数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "加载需删除的数据失败，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("批量删除菜单数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }


    @GetMapping(value = "/{id}")
    public ResultDTO<Role> restRoleIdGet(@PathVariable(value = "id") Long id) {
        Role role = roleService.queryById(id);
        if (null == role) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,role);
        }
    }


    @PutMapping(value = "/{id}")
    public ResultDTO<?> restRoleIdPut(@Valid Role role, BindingResult result) {
        if (!result.hasErrors()) {
            roleService.update(role);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result);
        }
    }






    /*@PostMapping(value = "/validator/title")
    public ValidatorResultDTO restMenuValidatorTitlePost(@RequestParam Long id,@RequestParam String title){
        Boolean result = menuService.existsByTitleAndIdNot(title, id);
        return new ValidatorResultDTO(!result);
    }

    @DeleteMapping
    public ResultDTO<?> dataMenuDelete(Long[] ids) {
        try {
            for (Long id : ids) {
                Long childrenNumber = menuService.countByParentId(id);
                if (childrenNumber > 0L) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该父菜单下还有挂靠的子菜单,不能删除！", null);
                } else {
                    this.menuService.batchRemove(Arrays.asList(id));
                }
            }
             return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } catch (InvalidDataException e) {
            LOG.error("批量删除菜单数据发生错误");
            LOG.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "加载需删除的数据失败，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            LOG.error("批量删除菜单数据发生错误");
            LOG.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    @PostMapping
    public ResultDTO<?> restMenuPost(AppAdminMenuDTO menuDTO, BindingResult result) {
        if(!result.hasErrors()){
            menuService.add(menuDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,null);
        }else{
            return actFor400(result);
        }
    }



    @PutMapping(value = "/{id}")
    public ResultDTO<?> restMenuIdPut(@Valid AppAdminMenuDTO menuDTO, BindingResult result) {
        if (!result.hasErrors()) {
            menuService.update(menuDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result);
        }
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<AppAdminMenuVO> restMenuIdGet(@PathVariable(value = "id") Long id) {
        AppAdminMenuDO menuDO = menuService.queryById(id);
        if (null == menuDO) {
            LOG.warn("查找菜单失败：MenuDO(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            AppAdminMenuVO menuVO = AppAdminMenuVO.transform(menuDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,menuVO);
        }
    }*/
}
