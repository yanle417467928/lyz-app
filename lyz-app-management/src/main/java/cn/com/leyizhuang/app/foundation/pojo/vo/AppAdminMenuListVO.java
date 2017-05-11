package cn.com.leyizhuang.app.foundation.pojo.vo;

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
public class AppAdminMenuListVO implements Serializable {

    private Long id;
    private String title;
    private String iconStyle;
    private String linkUri;
    private Long parentId;
    private Integer sortId;
    private List<AppAdminMenuListVO> children;

    public final static AppAdminMenuListVO transform(AppAdminMenuDO menuDO) {
        AppAdminMenuListVO menuVO;
        if (null != menuDO) {
        menuVO = new AppAdminMenuListVO();
        menuVO.setId(menuDO.getId());
        menuVO.setTitle(menuDO.getTitle());
        menuVO.setIconStyle(menuDO.getIconStyle());
        menuVO.setLinkUri(menuDO.getLinkUri());
        menuVO.setParentId(menuDO.getParent().getId());
        menuVO.setSortId(menuDO.getSortId());
        } else {
            menuVO = null;
        }
        return menuVO;
    }

    public final static List<AppAdminMenuListVO> transform(List<AppAdminMenuDO> menuDOList) {
        List<AppAdminMenuListVO> menuVOList = new ArrayList<>();
        if (null != menuDOList) {
            menuDOList.forEach(menuDO -> menuVOList.add(transform(menuDO)));
        }
        return menuVOList;
    }

}
