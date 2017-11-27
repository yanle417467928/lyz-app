package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 物理分类
 * Created by caiyu on 2017/11/27.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalClassify {
    //自增id
    private Long id;
    //HQid
    private Long hqId;
    //分类名称
    private String physicalClassifyName;
    //父分类id
    private Long parentCategoryId;
}
