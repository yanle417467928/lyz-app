package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.core.constant.AppAdminMenuType;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard
 *         Created on 2017/5/6.
 */
@Getter
@Setter
@ToString
public class AppAdminMenuVO implements Serializable {

    private Long id;
    private String title;
    private AppAdminMenuType type;
    private String iconStyle;
    private String linkUri;
    private Long parentId;
    private String parentTitle;
    private String referenceTable;
    private Integer sortId;
    private List<AppAdminMenuVO> children;


    public final static AppAdminMenuVO transform(AppAdminMenuDO menuDO) {
        AppAdminMenuVO menuVO;
        if (null != menuDO) {
        menuVO = new AppAdminMenuVO();
        menuVO.setId(menuDO.getId());
        menuVO.setTitle(menuDO.getTitle());
        menuVO.setIconStyle(menuDO.getIconStyle());
        menuVO.setLinkUri(menuDO.getLinkUri());
        menuVO.setParentId(menuDO.getParentId());
        menuVO.setType(menuDO.getType());
        menuVO.setSortId(menuDO.getSortId());
        menuVO.setReferenceTable(menuDO.getReferenceTable());
        } else {
            menuVO = null;
        }
        return menuVO;
    }

    public final static List<AppAdminMenuVO> transform(List<AppAdminMenuDO> menuDOList) {
        List<AppAdminMenuVO> menuVOList = new ArrayList<>();
        if (null != menuDOList) {
            menuDOList.forEach(menuDO -> menuVOList.add(transform(menuDO)));
        }
        return menuVOList;
    }

}
