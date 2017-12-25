package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Created with IntelliJ IDEA.
 * 后台商品物理分类
 *
 * @author liuh
 * Date: 2017/11/23.
 * Time: 10:41.
 */
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
