package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppAdminMenuType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * App后台管理导航菜单列表
 *
 * @author Richard
 * Created on 2017-05-08 10:27
 **/

@Getter
@Setter
@ToString(callSuper = true)
public class AppAdminMenuDO extends BaseDO {

    private static final long serialVersionUID = 6049661654540051301L;
    // 菜单标题
    private String title;
    // 小图片样式
    private String iconStyle;

    // 父节点id
    private Long parentId;

    //父节点名称
    private String parentTitle;

    // 链接地址
    private String linkUri;
    // 排序号
    private Integer sortId = 0;
    // 菜单类型
    private AppAdminMenuType type;
    // 相关数据表
    private String referenceTable;


}
