package cn.com.leyizhuang.app.foundation.pojo;

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

    private Long id;
    @NotNull(message = "资源名称不允许为空！")
    private String name;
    @NotNull(message = "资源路径不允许为空！")
    private String url;
    private String description;
    @NotNull(message = "资源图标不允许为空！")
    private String icon;

    private Integer pid;

    private Integer seq;
    private Boolean status;
    @NotNull(message = "资源类型不允许为空！")
    private Integer resourceType;//0 :菜单，1:按钮
    private Date createTime;
}
