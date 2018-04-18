package cn.com.leyizhuang.app.core.constant;

/**
 * 装饰公司excel导入产品
 * 到下料清单错误类型
 *
 * @author Richard
 * Created on 2017/3/24.
 */
public enum FitExcelImportGoodsErrorType {

    GOODS_NOT_EXISTS("GOODS_NOT_EXISTS", "商品不存在"), INV_NOT_ENOUGH("INV_NOT_ENOUGH", "库存不足"), PRICE_NOT_EXISTS("PRICE_NOT_EXISTS", "没有价目表");


    private final String value;
    private final String description;

    FitExcelImportGoodsErrorType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static FitExcelImportGoodsErrorType getFitExcelImportGoodsErrorTypeByValue(String value) {
        for (FitExcelImportGoodsErrorType appIdentityType : FitExcelImportGoodsErrorType.values()) {
            if (value == appIdentityType.getValue()) {
                return appIdentityType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
