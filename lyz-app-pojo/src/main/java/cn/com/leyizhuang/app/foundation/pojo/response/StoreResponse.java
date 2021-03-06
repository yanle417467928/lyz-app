package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.common.util.CharactersUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/3
 */
@Getter
@Setter
@ToString
public class StoreResponse {

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店地址
     */
    private String storeAddress;
    /**
     * 门店电话
     */
    private String storePhone;


    public static StoreResponse transform(AppStore store) {
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setStoreId(store.getStoreId());
        storeResponse.setStoreName(store.getStoreName());
        storeResponse.setStorePhone(store.getPhone());
        if (null != store.getDetailedAddress()) {
            String address = CharactersUtils.generateString(true,
                    store.getProvince(), store.getCity(), store.getArea(), store.getDetailedAddress());
            storeResponse.setStoreAddress(address);
        }
        return storeResponse;
    }

    public static List<StoreResponse> transform(List<AppStore> appStores) {
        List<StoreResponse> responseList;
        if (null != appStores && !appStores.isEmpty()) {
            responseList = new ArrayList<>(appStores.size());
            appStores.forEach(appStore -> responseList.add(transform(appStore)));
        } else {
            responseList = new ArrayList<>(0);
        }
        return responseList;
    }
}
