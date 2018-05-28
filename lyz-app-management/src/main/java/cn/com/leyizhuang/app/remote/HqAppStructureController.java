package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.foundation.service.StructureService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppStoreDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppStructureDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * HQ-APP同步组织
 * Created by caiyu on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/remote/structure")
public class HqAppStructureController {

    private static final Logger logger = LoggerFactory.getLogger(HqAppStructureController.class);

    @Resource
    private StructureService structureService;



    /**
     * 同步添加组织信息
     *
     * @param hqAppStructureDTO 组织信息
     * @return 成功或失败
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addStructure(@RequestBody HqAppStructureDTO hqAppStructureDTO) {
        logger.warn("addStore CALLED,同步添加组织信息，入参 HqAppStructureDTO:{}", hqAppStructureDTO);
        if (hqAppStructureDTO != null) {
            if (StringUtils.isBlank(hqAppStructureDTO.getCreatorType())) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 creatorType:{}", hqAppStructureDTO.getCreatorType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创建人类型不允许为空！", null);
            }
            if (null == hqAppStructureDTO.getCreateTime()) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 createTime:{}", hqAppStructureDTO.getCreateTime());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创建时间不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStructureDTO.getStructureTitle())) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 storeName:{}", hqAppStructureDTO.getStructureTitle());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织架构名称不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStructureDTO.getNumber())) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 storeCode:{}", hqAppStructureDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织架构编码不允许为空！", null);
            }
            if (null == hqAppStructureDTO.getStructureNumber()) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 isDefault:{}", hqAppStructureDTO.getStructureNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "全局编码不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStructureDTO.getType())) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 storeType:{}", hqAppStructureDTO.getType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织架构类型不允许为空！", null);
            }
            if (null ==hqAppStructureDTO.getParentId()) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 cityCode:{}", hqAppStructureDTO.getParentId());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "父节点ID不允许为空！", null);
            }
            if (null ==hqAppStructureDTO.getTier()) {
                logger.warn("addStore OUT,同步添加组织信息失败，出参 city:{}", hqAppStructureDTO.getTier());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "层级不允许为空！", null);
            }
            try {
             /*   AppStore store = appStoreService.findByStoreCode(hqAppStructureDTO.getNumber());
                City city = cityService.findByCityNumber(hqAppStructureDTO.getCityCode());*/
                Structure structure = structureService.findByStructureNumber(hqAppStructureDTO.getStructureNumber());
                if (null == structure) {
                    Structure maStructure = new Structure();
                    maStructure.setCreatTime(new Date());
                    maStructure.setEnable(Boolean.TRUE);
                    maStructure.setNumber(hqAppStructureDTO.getNumber());
                    maStructure.setParentId(hqAppStructureDTO.getParentId());
                    maStructure.setStructureName(hqAppStructureDTO.getStructureTitle());
                    maStructure.setStructureNumber(hqAppStructureDTO.getStructureNumber());
                    maStructure.setTier(hqAppStructureDTO.getTier());
                    maStructure.setType(hqAppStructureDTO.getType());
                    structureService.SaveStructure(maStructure);
                    logger.warn("addStore EXCEPTION,同步添加组织信息成功，出参 resultDTO:{}", maStructure);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else {
                    logger.warn("addStore OUT,该组织已存在,同步添加组织信息失败，出参 hqAppStoreDTO:{}", hqAppStructureDTO);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该组织已存在，同步添加组织信息失败！", null);
                }

            } catch (Exception e) {
                logger.warn("addStore EXCEPTION,同步添加组织信息失败，出参 resultDTO:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步添加组织信息失败！", null);
            }
        }else {
            logger.warn("addStore,组织信息为空，同步添加组织信息失败！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织信息为空，同步添加组织信息失败！", null);
        }
    }

    /**
     * 同步修改组织信息
     *
     * @param hqAppStructureDTO 修改的组织信息
     * @return 成功或失败
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updateStore(@RequestBody HqAppStructureDTO hqAppStructureDTO) {
        logger.warn("updateStore CALLED,同步修改组织信息，入参 hqAppStoreDTO:{}", hqAppStructureDTO);
        if (null != hqAppStructureDTO) {
            if (StringUtils.isBlank(hqAppStructureDTO.getModifierType())) {
                logger.warn("updateStore OUT,同步修改组织信息失败，出参 modifierType:{}", hqAppStructureDTO.getModifierType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "修改类型不允许为空！", null);
            }
            if (null == hqAppStructureDTO.getModifyTime()) {
                logger.warn("updateStore OUT,同步修改组织信息失败，出参 modifyTime:{}", hqAppStructureDTO.getModifyTime());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "修改时间不允许为空！", null);
            }
            try {
                Structure maStructure = new Structure();
                maStructure.setCreatTime(new Date());
                maStructure.setEnable(Boolean.TRUE);
                maStructure.setNumber(hqAppStructureDTO.getNumber());
                maStructure.setParentId(hqAppStructureDTO.getParentId());
                maStructure.setStructureName(hqAppStructureDTO.getStructureTitle());
                maStructure.setStructureNumber(hqAppStructureDTO.getStructureNumber());
                maStructure.setTier(hqAppStructureDTO.getTier());
                maStructure.setType(hqAppStructureDTO.getType());
                structureService.ModifyStructure(maStructure);
                logger.warn("同步修改组织信息成功！，出参 resultDTO:{}",maStructure);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } catch (Exception e) {
                logger.warn("updateStore EXCEPTION,同步修改组织信息失败，出参 resultDTO:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步修改组织信息失败！", null);
            }
        }else {
            logger.warn("组织信息为空，同步修改组织信息失败！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织信息为空，同步修改组织信息失败！", null);
        }
    }

    /**
     * 同步删除组织信息
     *
     * @param structureNumber 组织编码
     * @return 成功或者失败
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteStore(String structureNumber) {
        logger.warn("deleteStore CALLED,同步删除组织信息，入参 structureNumber:{}", structureNumber);
        if (StringUtils.isBlank(structureNumber)) {
            logger.warn("deleteStore OUT,同步删除组织信息失败，出参 storeCode:{}", structureNumber);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "组织编码不允许为空！", null);
        }
        try {
            structureService.delStructure(structureNumber);
            logger.warn("同步删除组织信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } catch (Exception e) {
            logger.warn("deleteStore EXCEPTION,同步删除组织信息失败，出参 resultDTO:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步删除组织信息失败！", null);
        }
    }
}
