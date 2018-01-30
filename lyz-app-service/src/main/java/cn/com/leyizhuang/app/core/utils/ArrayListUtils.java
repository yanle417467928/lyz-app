package cn.com.leyizhuang.app.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panjie on 2018/1/27.
 */
public class ArrayListUtils {

    /**
     * @author panjie
     * 2018.1.24
     * 将传入的List按照给定的size拆分成多个子List    <br>
     * 例如 list=[1, 2, 3, 4, 5] , per=3    <br>
     * 则会得到 : [[1, 2, 3],[4, 5]]    <br>
     * list=[1, 2, 3, 4, 5]  , per=2    <br>
     * 则会得到 : [[1, 2], [3, 4], [5]]    <br>
     * */
    public static <T> List<List<T>> splitList(List<T> list, int per){
        List<List<T>> returnList = new ArrayList<List<T>>();
        int count = list.size()/per;
        int yu = list.size() % per;
        for (int i = 0; i <= count; i++) {
            List<T> subList = new ArrayList<T>();
            if (i == count) {
                subList = list.subList(i * per, i * per + yu);
            } else {
                subList = list.subList(i * per, per * (i + 1));
            }
            returnList.add(subList);
        }
        return returnList;
    }
}
