package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.service.AppPreDepositWithdrawService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * Created by panjie on 2018/2/5.
 */
@Service
public class AppPreDepositWithdrawServiceImpl implements AppPreDepositWithdrawService {

    private AppCustomerServiceImpl appCustomerService;

    public void cusApplyWithdraw(Long cusId,Double amount){

        // 获取提现人信息


        CusPreDepositWithdraw cusPreDepositWithdraw = new CusPreDepositWithdraw();
        cusPreDepositWithdraw.setApplyNo(this.createCode("2121","CUS"));
        cusPreDepositWithdraw.setCreateTime(new Date());


    }

    public void stApplyWithdraw(Long stId,Double amount){

    }

    private String createCode(String cityCode,String type){
        String code = "";

        if (cityCode.equals("2121")){
            code = "CD";
        }else if (cityCode.equals("2033")){
            code = "ZZ";
        }else if (cityCode.equals("2044")){
            code = "CQ";
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String now = LocalDateTime.now().format(format);
        Random random = new Random();
        String suiji = random.nextInt(900)+100+"";
        return  code+"_"+type+"_"+now+suiji;
    }

}
