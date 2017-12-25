package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaCityDeliveryTimeDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDeliveryTimeVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MaCityDeliveryTimeServiceImpl implements MaCityDeliveryTimeService {

    @Autowired
    private MaCityDeliveryTimeDAO macityDeliveryTimeDAO;
    @Override
    public PageInfo<CityDeliveryTime> queryPage(Integer page, Integer size, Long id){
        PageHelper.startPage(page, size);
        List<CityDeliveryTime> cityDeliveryTimeList = macityDeliveryTimeDAO.queryPage(id);
        return  new PageInfo<>(cityDeliveryTimeList);
    }

    @Override
    public  Boolean judgmentTime(String startTime,String endTime,Long cityId,Long id){
        List<CityDeliveryTime> cityDeliveryTime = macityDeliveryTimeDAO.judgmentTime(cityId,id);
        List<CityDeliveryTimeVO> cityDeliveryTimeVOList =CityDeliveryTime.transform(cityDeliveryTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for(int i=0;i<cityDeliveryTimeVOList.size();i++){
          String getStartTime = cityDeliveryTimeVOList.get(i).getStartTime();
          String  getEndTime  =  cityDeliveryTimeVOList.get(i).getEndTime();
          try {
              Date startTimeCompare = sdf.parse(startTime);
              Date endTimeCompare = sdf.parse(endTime);
              Date getStartTimeCompare =  sdf.parse(getStartTime);
              Date getEndTimeCompare  = sdf.parse(getEndTime);
              if((!startTimeCompare.before(getStartTimeCompare)&&getEndTimeCompare.after(startTimeCompare))||(getStartTimeCompare.before(endTimeCompare)&&!endTimeCompare.after(getEndTimeCompare))||(!startTimeCompare.after(getStartTimeCompare)&&!endTimeCompare.before(getEndTimeCompare))){
                  return false;
              }
            } catch (Exception e) {
              System.out.println(e);
            }
        }
        return true;
    }

    @Override
    public  Boolean judgmentTime(String startTime,String endTime,Long cityId){
        List<CityDeliveryTime> cityDeliveryTime = macityDeliveryTimeDAO.queryPage(cityId);
        List<CityDeliveryTimeVO> cityDeliveryTimeVOList =CityDeliveryTime.transform(cityDeliveryTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for(int i=0;i<cityDeliveryTimeVOList.size();i++){
            String getStartTime = cityDeliveryTimeVOList.get(i).getStartTime();
            String  getEndTime  =  cityDeliveryTimeVOList.get(i).getEndTime();
            try {
                Date startTimeCompare = sdf.parse(startTime);
                Date endTimeCompare = sdf.parse(endTime);
                Date getStartTimeCompare =  sdf.parse(getStartTime);
                Date getEndTimeCompare  = sdf.parse(getEndTime);
                if((!startTimeCompare.before(getStartTimeCompare)&&getEndTimeCompare.after(startTimeCompare))||(getStartTimeCompare.before(endTimeCompare)&&!endTimeCompare.after(getEndTimeCompare))||(!startTimeCompare.after(getStartTimeCompare)&&!endTimeCompare.before(getEndTimeCompare))){
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return true;
    }


    @Override
    public void save(CityDeliveryTimeVO cityDeliveryTimeVO){
        if(1 == cityDeliveryTimeVO.getCityId()){
            cityDeliveryTimeVO.setCityName("成都市");
        }else if(2 == cityDeliveryTimeVO.getCityId()){
            cityDeliveryTimeVO.setCityName("郑州市");
        }else if(3 == cityDeliveryTimeVO.getCityId()){
            cityDeliveryTimeVO.setCityName("重庆市");
        }
        CityDeliveryTime  cityDeliveryTime =  CityDeliveryTimeVO.transform(cityDeliveryTimeVO);
         macityDeliveryTimeDAO.save(cityDeliveryTime);
    }

    @Override
    public CityDeliveryTimeVO queryById(Long id){
        CityDeliveryTime cityDeliveryTime = macityDeliveryTimeDAO.queryById(id);
        CityDeliveryTimeVO cityDeliveryTimeVO = CityDeliveryTime.transform(cityDeliveryTime);
        if(null==cityDeliveryTime){
            return  null;
        }
        return cityDeliveryTimeVO;
    }

    @Override
    public void update(CityDeliveryTimeVO cityDeliveryTimeVO){
        CityDeliveryTime  cityDeliveryTime =  CityDeliveryTimeVO.transform(cityDeliveryTimeVO);
        macityDeliveryTimeDAO.update(cityDeliveryTime);
    }
}
