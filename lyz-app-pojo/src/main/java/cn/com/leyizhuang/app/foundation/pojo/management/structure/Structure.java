package cn.com.leyizhuang.app.foundation.pojo.management.structure;

import lombok.*;

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
    /**
     * 组织架构名称
     */
    private String structureTitle;
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
     * 创建人类型
     */
    private String creatorType;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人类型
     */
    private String modifierType;
    /**
     * 修改时间
     */
    private String modifyTime;


}
