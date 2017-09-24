package cn.com.leyizhuang.app.foundation.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 权限页面VO
 *
 * @author Richard
 * Created on 2017-08-05 10:06
 **/
@Getter
@Setter
@ToString
public class ResourceVO {
    //资源id
    private Long id;
    //资源名称
    @NotNull(message = "资源名称不允许为空！")
    private String resourceName;
    //资源描述
    private String resourceDescription;
    //父级资源ID
    private Long parentResourceId;
    //父级资源名称
    private String parentResourceName;
    //父级资源描述
    private String parentResourceDesciption;
    //资源路径
    @NotNull(message = "资源路径不允许为空！")
    private String url;
    //资源排序号
    private Integer seq;
    //资源图标
    @NotNull(message = "资源图标不允许为空！")
    private String icon;
    //状态
    private Boolean status;
    //资源类型
    @NotNull(message = "资源类型不允许为空！")
    private Integer resourceType;//0 :菜单，1:按钮

    private List<ResourceVO> children;


}
