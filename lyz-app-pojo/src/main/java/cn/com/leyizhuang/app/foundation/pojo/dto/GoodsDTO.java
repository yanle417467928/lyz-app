package cn.com.leyizhuang.app.foundation.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author GenerationRoad
 * @date 2017/9/8
 */
@Getter
@Setter
@ToString
public class GoodsDTO implements Serializable{

    // 自增主键
    private Long id;
    //商品名称
    @Length(min = 2, max = 50, message = "'商品名称'的长度必须在2~50位之间")
    @NotNull(message = "'商品名称'不能为空")
    private String goodsName;
    //商品编码
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5]+$",
            message = "'商品编码'不能输入中文")
    @Length(min = 2, max = 50, message = "'商品编码'的长度必须在2~50位之间")
    @NotNull(message = "'商品编码'不能为空")
    private String goodsCode;
    //是否为小辅料
    private Boolean isGift;
    //商品标题
    @Length(min = 2, max = 50, message = "'商品简称'的长度必须在2~50位之间")
    @NotNull(message = "'商品简称'不能为空")
    private String title;
    //副标题
    @Length(min = 2, max = 50, message = "'商品全称'的长度必须在2~50位之间")
    @NotNull(message = "'商品全称'不能为空")
    private String subTitle;
    //封面图片
    private String coverImageUri;
    //轮播展示图片，多张图片以,隔开
    private String showPictures;
    //是否上架
    private Boolean isOnSale;
    //商品类型
    private Long categoryId;
    //商品类型名称
    private String categoryTitle;
    //库存数量
    @Pattern(regexp = "^[0-9]+$",
            message = "'库存数量'不能输入中文")
    @Length(min = 0, max = 10, message = "'库存数量'的长度必须在0~10位之间")
    private String leftNumber;
    //上架时间
    private String onSaleTime;
    //品牌
    private String brandTitle;
    //品牌ID
    private Long brandId;
    //是否是调色包
    private Boolean isColorPackage;
    //是否调色产品
    private Boolean isColorful;
}
