package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author CrazyApeDX
 * Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminStoreInventoryRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminStoreInventoryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/store/inventory";

    private final Logger logger = LoggerFactory.getLogger(AppAdminStoreInventoryRestController.class);

    @Autowired
    private AppAdminStoreInventoryService storeInventoryService;
    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<AppAdminStoreInventoryVO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {

        logger.info("dataMenuPageGridGet CREATE,门店库存可用量分页查询, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<AppAdminStoreInventoryVO> storeInventoryPage = storeInventoryService.queryPage(page, size, keywords,storeIds);
        return new GridDataVO<AppAdminStoreInventoryVO>().transform(storeInventoryPage.getList(), storeInventoryPage.getTotal());
    }


    @GetMapping(value = "/storeGrid/{storeId}")
    public GridDataVO<AppAdminStoreInventoryVO> dataMenuPageGridGetByStoreId(Integer offset, Integer size, String keywords,@PathVariable("storeId") Long storeId) {
        logger.info("dataMenuPageGridGetByStoreId CREATE,门店库存可用量分页查询, 入参 offset:{},size:{},keywords:{},storeId:{}", offset, size, keywords,storeId);
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<AppAdminStoreInventoryVO> storeInventoryPage = storeInventoryService.queryPageByStoreId(page, size, keywords,storeId);
        return new GridDataVO<AppAdminStoreInventoryVO>().transform(storeInventoryPage.getList(), storeInventoryPage.getTotal());
    }

    /**
     * 根据门店id和商品 查库存详情
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/infoGrid")
    public  GridDataVO<AppAdminStoreInventoryVO> getStoreInventoryByInfo(Integer offset, Integer size, String keywords,@RequestParam(value = "storeId") Long storeId,@RequestParam(value = "info") String info) {
        logger.info("dataMenuPageGridGetByStoreId CREATE,门店库存可用量分页查询, 入参 offset:{},size:{},keywords:{},storeId:{}", offset, size, keywords,storeId);
        // 根据偏移量计算当前页数
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<AppAdminStoreInventoryVO> storeInventoryPage = storeInventoryService.queryStoreInventoryByInfo(page, size, keywords,storeId,info);
        return new GridDataVO<AppAdminStoreInventoryVO>().transform(storeInventoryPage.getList(), storeInventoryPage.getTotal());
    }

    /**
     * 根据门店id 查库存详情
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/{storeId}")
    public ResultDTO<StoreInventory> getStoreInventory(@PathVariable(value = "storeId") Long storeId) {

        StoreInventory storeInventory = this.storeInventoryService.queryStoreInventoryById(storeId);
        if (null == storeInventory) {
            logger.warn("查找门店库存详情失败：Role(id = {}) == null", storeId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeInventory);
        }
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
