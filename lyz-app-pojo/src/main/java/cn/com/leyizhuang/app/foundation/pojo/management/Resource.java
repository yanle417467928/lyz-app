package cn.com.leyizhuang.app.foundation.pojo.management;

import cn.com.leyizhuang.app.core.constant.AppAdminResourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源
 *
 * @author Richard
 * Created on 2017-07-28 12:02
 **/
@Getter
@Setter
@ToString
public class Resource implements Serializable {

    private static final long serialVersionUID = -817211771313599629L;

    //资源id
    private Long rsId;

    //资源名称
    @NotNull(message = "资源名称不允许为空！")
    private String name;

    //资源路径
    @NotNull(message = "资源路径不允许为空！")
    private String url;

    //资源描述
    private String description;

    //资源图标
    @NotNull(message = "资源图标不允许为空！")
    private String icon;

    //父级资源id
    private Long pid;

    //排序id
    private Integer sortId;

    //状态：禁用，启用
    private Boolean status;

    //资源类型：菜单，按钮
    @NotNull(message = "资源类型不允许为空！")
    private AppAdminResourceType resourceType;//

    //创建时间
    private Date createTime;
}
