package cn.com.leyizhuang.app.foundation.pojo.management.structure;

import lombok.*;

import java.util.Date;

/**
 *
 * Created by liuh on 2018/5/6.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Structure {

    private Long  id;
    /**
     * 数据创建时间
     */
    private Date creatTime;

    /**
     * 组织架构名称
     */
    private String structureName;

    /**
     * 组织架构编码
     */
    private String number;
    /**
     * 全局编码
     */
    private String structureNumber;
    /**
     * 组织架构类型
     */
    private String type;
    /**
     * 父节点ID
     */
    private Long parentId;
    /**
     * 层级
     */
    private Integer tier;

    /**
     * 是否生效
     */
    private Boolean enable;
    /**
     * 失效时间
     */
    private String enableFalseTime;


}
