package cn.com.leyizhuang.app.foundation.pojo.goods;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PhysicalClassify {

    private Long id;
    //HQ物理分类表id
    private Long hqId;
    //物理分类名称
    private String physicalClassifyName;
    //父分类id
    private Long parentCategoryId;

}
