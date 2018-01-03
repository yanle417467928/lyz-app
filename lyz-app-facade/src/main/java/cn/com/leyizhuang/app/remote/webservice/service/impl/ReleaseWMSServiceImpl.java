package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.wms.WtaShippingOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.wms.WtaShippingOrderHeader;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace = "http://cn.com.leyizhuang.app.remote.webservice.service",
        endpointInterface = "cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService")
public class ReleaseWMSServiceImpl implements ReleaseWMSService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseWMSServiceImpl.class);

    @Resource
    private WmsToAppOrderService wmsToAppOrderService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private GoodsService goodsService;
    /**
     * 获取wms信息
     *
     * @param strTable 表
     * @param strType  类型
     * @param xml      内容主体
     * @return 结果
     */
    @Override
    public String GetWMSInfo(String strTable, String strType, String xml) {

        logger.info("GetWMSInfo CALLED,获取wms信息，入参 strTable:{},strType:{},xml:{}", strTable, strType, xml);

        if (StringUtils.isBlank(strTable) || "?".equals(strTable)) {
            logger.info("GetWMSInfo OUT,获取wms信息失败 出参 strTable:{}", strTable);
            return AppXmlUtil.resultStrXml(1, "STRTABLE参数错误");
        }

        if (StringUtils.isBlank(xml) || "?".equals(xml)) {
            logger.info("GetWMSInfo OUT,获取wms信息失败 出参 strTable:{}", xml);
            return AppXmlUtil.resultStrXml(1, "XML参数错误");
        }

        try {
            Document document = AppXmlUtil.parseStrXml(xml);

            if (null == document) {
                logger.info("GetWMSInfo OUT,获取wms信息失败");
                return AppXmlUtil.resultStrXml(1, "解密后XML数据为空");
            }

            NodeList nodeList = document.getElementsByTagName("TABLE");
            //出货订单头
            if ("tbw_send_task_m".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaShippingOrderHeader header = new WtaShippingOrderHeader();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        header = mapping(header, childNode);
                    }
                    if (StringUtils.isBlank(header.getDriver())) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,配送员不能为空,任务编号 出参 c_task_no:{}", header.getTaskNo());
                        return AppXmlUtil.resultStrXml(1, "配送员编号不能为空,任务编号" + header.getTaskNo() + "");
                    }
                    wmsToAppOrderService.saveWtaShippingOrderHeader(header);
                    //查询是否重复
                    List<OrderDeliveryInfoDetails> deliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(header.getOrderNo());
                    if (AssertUtil.isEmpty(deliveryInfoDetailsList)) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,该订单不存在 出参 order_no:{}", header.getOrderNo());
                        return AppXmlUtil.resultStrXml(1, "App没有找到该订单");
                    }
                    //保存物流信息
                    OrderDeliveryInfoDetails deliveryInfoDetails = OrderDeliveryInfoDetails.transform(header);
                    orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
            }
            //出货订单商品明细
            if ("tbw_send_task_d".equalsIgnoreCase(strTable)) {

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    WtaShippingOrderGoods goods = new WtaShippingOrderGoods();
                    for (int j = 0; j < childNodeList.getLength(); j++) {

                        Node childNode = childNodeList.item(j);
                        goods = mapping(goods, childNode);
                    }
                    GoodsDO goodsDO = goodsService.queryBySku(goods.getGCode());
                    if (goodsDO == null) {
                        logger.info("GetWMSInfo OUT,获取wms信息失败,商品不存在 出参 c_gcode:{}", goods.getGCode());
                        return AppXmlUtil.resultStrXml(1, "编码为" + goods.getGCode() + "的商品不存在");
                    }
                    wmsToAppOrderService.saveWtaShippingOrderGoods(goods);
                }
                logger.info("GetWMSInfo OUT,获取wms信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "");
            }

        } catch (ParserConfigurationException e) {
            logger.warn("GetWMSInfo EXCEPTION,解密后xml参数错误");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml参数错误");

        } catch (IOException | SAXException e) {
            logger.warn("GetWMSInfo EXCEPTION,解密后xml格式不对");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml格式不对");
        }
        return AppXmlUtil.resultStrXml(1, "不支持该表数据传输：" + strTable);
    }

    /**
     * 将wms结果集映射到实体上
     *
     * @param childNode
     * @return
     */
    private WtaShippingOrderHeader mapping(WtaShippingOrderHeader wtaShippingOrderHeader, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_task_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_begin_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_end_dt".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setEndDt(DateUtil.parseDate(childNode.getChildNodes().item(0).getNodeValue()));
                }
            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setWhNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOpStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_op_user".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_op_user = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_modified_userno".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOwnerNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_reserved1".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_Driver".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setDriver(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_company_id".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    cCompanyId = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
//                }
            } else if ("c_task_type".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    wtaShippingOrderHeader.setCTaskType(childNode.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return wtaShippingOrderHeader;
    }

    private WtaShippingOrderGoods mapping(WtaShippingOrderGoods shippingOrderGoods, Node childNode) {

        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            // 比较字段名
            if ("c_task_no".equalsIgnoreCase(childNode.getNodeName())) {
                // 有值
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setTaskNo(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_reserved1".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setOrderNo(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_begin_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_end_dt".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    shippingOrderGoods.set = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_wh_no".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    shippingOrderGoods.set = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_op_status".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setOpStatus(childNode.getChildNodes().item(0).getNodeValue());
                }
//            } else if ("c_op_user".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_op_user = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_modified_userno".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
//                }
//            } else if ("c_owner_no".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    c_owner_no = childNode.getChildNodes().item(0).getNodeValue();
//                }
            } else if ("c_gcode".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    shippingOrderGoods.setGCode(childNode.getChildNodes().item(0).getNodeValue());
                }
            } else if ("c_d_ack_qty".equalsIgnoreCase(childNode.getNodeName())) {
                if (null != childNode.getChildNodes().item(0)) {
                    Double parseDouble = Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue());
                    shippingOrderGoods.setDAckQty(parseDouble.intValue());
                }
//            } else if ("c_d_request_qty".equalsIgnoreCase(childNode.getNodeName())) {
//                if (null != childNode.getChildNodes().item(0)) {
//                    String string = childNode.getChildNodes().item(0).getNodeValue();
//                    c_d_request_qty = Double.parseDouble(string);
//                }
            } else if ("c_task_type".equalsIgnoreCase(childNode.getNodeName())) {
                shippingOrderGoods.setTaskType(childNode.getChildNodes().item(0).getNodeValue());
            } else if ("C_SOURCE_NO".equalsIgnoreCase(childNode.getNodeName())) {
                shippingOrderGoods.setSourceNo(childNode.getChildNodes().item(0).getNodeValue());
            }
        }
        return shippingOrderGoods;
    }

    //***************************下面是调用测试***********************************
    @Override
    public String getName(String userId) {
        return "liyd-" + userId;
    }

    @Override
    public String getUser(String userId) {
        TestUser testUser = new TestUser();
        testUser.setUsername("YL");
        testUser.setAge("20");
        testUser.setUserId("1");
        return JSON.toJSONString(testUser);
    }
}
