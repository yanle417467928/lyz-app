package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author Jerry.Ren
 * Notes: 下料清单的加入类型
 * Created with IntelliJ IDEA.
 * Date: 2017/11/17.
 * Time: 11:47.
 */

public enum MaterialListType {

    NORMAL("正常"), AUDIT_TRANSFORM("料单审核转化"), COUPON_TRANSFORM("产品券转化"), PHOTO_ORDER("拍照订单转化"), EXCEL_IMPORT("后台excel导入");

    private String value;


    MaterialListType(String value) {
        this.value = value;
    }

    public static MaterialListType getMaterialListTypeByValue(String value) {
        for (MaterialListType materialList : MaterialListType.values()) {
            if (Objects.equals(value, materialList.getValue())) {
                return materialList;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
