package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import lombok.*;
/**
 * Created with IntelliJ IDEA.
 * 后台简化商品分类
 *
 * @author liuh
 * Date: 2017/12/24.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGoodsCategoryParam {
    private Long id;

    //分类名称
    private String categoryName;

}
