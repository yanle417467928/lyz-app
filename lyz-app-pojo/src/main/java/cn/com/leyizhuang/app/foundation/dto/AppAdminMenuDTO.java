package cn.com.leyizhuang.app.foundation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Richard
 *         Created on 2017/6/27.
 */
@Getter
@Setter
@ToString
public class AppAdminMenuDTO implements Serializable {
    // 菜单ID
    private Long id;
    // 上级菜单ID
    @NotNull(message = "必须选择'父级菜单'")
    private Long parentId;
    @NotNull(message = "'菜单标题'不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+$", message = "'菜单标题'只能输入中文")
    @Length(min = 2, max = 10, message = "'菜单标题'的长度必须在2~10位之间")
    private String title;
    @NotNull(message = "'链接地址'不能为空")
    @Length(min = 1, max = 100, message = "'链接地址'的长度在1~100之间")
    @Pattern(regexp = "^((\\/([a-zA-Z0-9]+))+)||(#)$", message = "请输入一个正确的不带参数的站内链接地址或者\"#\"")
    private String linkUri;
    @Length(min = 0, max = 50, message = "'图标样式'的长度为0~50位")
    private String iconStyle;
    @Length(min = 0, max = 50, message = "'相关数据表'的长度为0~50位")
    private String referenceTable;
    @Max(value = 99999, message = "'排序号'的范围为0~99999之间")
    @Min(value = 1, message = "'排序号'的范围为0~99999之间")
    private Integer sortId;
}
