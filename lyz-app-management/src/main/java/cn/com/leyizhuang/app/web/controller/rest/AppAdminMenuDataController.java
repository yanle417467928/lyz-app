package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.vo.TableDataVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@RestController
@RequestMapping(value = AppAdminMenuDataController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminMenuDataController {

    protected final static String PRE_URL = "/rest/menu";

    private final Logger LOG = LoggerFactory.getLogger(AppAdminMenuDataController.class);

    @Autowired
    private AppAdminMenuService menuService;

    @GetMapping(value = "/page/grid")
    public TableDataVO<AppAdminMenuDO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {

        // 根据偏移量计算当前页数
        Integer page = (offset / size) + 1;
        PageInfo<AppAdminMenuDO> menuDOPage = menuService.queryPage(page, size);
        return new TableDataVO<AppAdminMenuDO>().transform(menuDOPage);
    }

    @DeleteMapping
    public ResultDTO<?> dataMenuDelete(Long[] ids) {
        try {
            this.menuService.batchRemove(Arrays.asList(ids));
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (InvalidDataException e){
            LOG.error("批量删除菜单数据发生错误");
            LOG.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "加载需删除的数据失败，请稍后重试或联系管理员", null);
        }catch (Exception e){
            LOG.error("批量删除菜单数据发生错误");
            LOG.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    @PutMapping
    public ResultDTO<?> dataMenuPut(Long[] ids) {
        System.err.println("PUT");
        System.err.println(Arrays.toString(ids));
        return new ResultDTO<>(200, null, null);
    }
}
