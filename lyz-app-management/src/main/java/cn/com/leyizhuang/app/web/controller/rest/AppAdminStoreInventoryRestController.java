package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CrazyApeDX
 * Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminStoreInventoryRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminStoreInventoryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/inventory";

    private final Logger LOG = LoggerFactory.getLogger(AppAdminStoreInventoryRestController.class);

    @Autowired
    private AppAdminStoreInventoryService storeInventoryService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<AppAdminStoreInventoryVO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = (offset / size) + 1;
        PageInfo<AppAdminStoreInventoryVO> storeInventoryPage = storeInventoryService.queryPage(page, size);
        return new GridDataVO<AppAdminStoreInventoryVO>().transform(storeInventoryPage.getList(), storeInventoryPage.getTotal());
    }


   /* @PostMapping(value = "/validator/title")
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

    protected ResultDTO<?> actFor400(BindingResult result) {
        return super.actFor400(result, "菜单编辑页面提交的数据有误");
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
