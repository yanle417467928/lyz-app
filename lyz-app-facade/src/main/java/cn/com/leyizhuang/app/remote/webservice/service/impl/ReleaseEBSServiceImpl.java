package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInf;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.DiySiteInventoryEbsService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuh
 * Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace = "http://cn.com.leyizhuang.app.remote.webservice.service",
        endpointInterface = "cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService")
public class ReleaseEBSServiceImpl implements ReleaseEBSService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseEBSServiceImpl.class);

    @Resource
    private DiySiteInventoryEbsService diySiteInventoryEbsService;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private MaStoreInventoryService maStoreInventoryService;

    /**
     * 获取EBS信息
     *
     * @param strTable 表
     * @param strType  类型
     * @param xml      内容主体
     * @return 结果
     */
    @Override
    public String GetEBSInfo(String strTable, String strType, String xml) {
        logger.info("****************************************EBS开始调用二代APP Webservice接口**********************************************");


        logger.info("GetEBSInfo CALLED,获取ebs信息，入参 strTable:{},strType:{},xml:{}", strTable, strType, xml);

        if (StringUtils.isBlank(strTable) || "?".equals(strTable)) {
            logger.info("GetEBSInfo OUT,获取ebs信息失败 出参 strTable:{}", strTable);
            return AppXmlUtil.generateResultXmlToEbs(1, "STRTABLE参数错误");
        }

        if (StringUtils.isBlank(xml) || "?".equals(xml)) {
            logger.info("GetEBSInfo OUT,获取ebs信息失败 出参 strTable:{}", xml);
            return AppXmlUtil.generateResultXmlToEbs(1, "XML参数错误");
        }

        try {
            Document document = AppXmlUtil.parseStrXml(xml);

            if (null == document) {
                logger.info("GetEBSInfo OUT,获取ebs信息失败");
                return AppXmlUtil.generateResultXmlToEbs(1, "解密后XML数据为空");
            }

            NodeList nodeList = document.getElementsByTagName("TABLE");
            //直营要货退货
            if ("CUXAPP_INV_STORE_TRANS_OUT".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    // 分公司ID
                    Long sobId = null;
                    // 事务唯一ID
                    String transId = null;
                    // 事务类型 "出货单","退货单","盘点入库","盘点出库"
                    String transType = null;
                    // 门店事务编号
                    String transNumber = null;
                    // 门店客户ID
                    Long customerId = null;
                    // 门店客户编号
                    String customerNumber = null;
                    // 门店编号(门店仓库)
                    String diySiteCode = null;
                    // 事务时间
                    String shipDate = null;
                    // 物料编号,SKU
                    String itemCode = null;
                    // 数量 "正数"入库，"负数"出库
                    Long quantity = null;
                    String attribute1 = null;
                    String attribute2 = null;
                    String attribute3 = null;
                    String attribute4 = null;
                    String attribute5 = null;

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            if ("SOB_ID".equalsIgnoreCase(childNode.getNodeName())) {
                                // 有值
                                if (null != childNode.getChildNodes().item(0)) {
                                    sobId = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
                                }
                            } else if ("TRANS_ID".equalsIgnoreCase(childNode.getNodeName())) {
                                // 有值
                                if (null != childNode.getChildNodes().item(0)) {
                                    transId = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("TRANS_TYPE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    transType = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("TRANS_NUMBER".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    transNumber = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("CUSTOMER_ID".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    customerId = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
                                }
                            } else if ("CUSTOMER_NUMBER".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    customerNumber = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("DIY_SITE_CODE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    diySiteCode = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("SHIP_DATE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    shipDate = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ITEM_CODE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    itemCode = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("QUANTITY".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    quantity = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
                                }
                            } else if ("ATTRIBUTE1".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    attribute1 = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ATTRIBUTE2".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    attribute2 = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ATTRIBUTE3".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    attribute3 = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ATTRIBUTE4".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    attribute4 = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ATTRIBUTE5".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    attribute5 = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            }
                        }
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        //查询该信息是否发送过
                        EtaReturnAndRequireGoodsInf etaReturnAndRequireGoodsInf = diySiteInventoryEbsService.findByTransId(transId);
                        if (null == etaReturnAndRequireGoodsInf) {
                            etaReturnAndRequireGoodsInf = new EtaReturnAndRequireGoodsInf();
                            etaReturnAndRequireGoodsInf.setSobId(sobId);
                            etaReturnAndRequireGoodsInf.setTransId(transId);
                            etaReturnAndRequireGoodsInf.setTransType(transType);
                            etaReturnAndRequireGoodsInf.setTransNumber(transNumber);
                            etaReturnAndRequireGoodsInf.setCustomerId(customerId);
                            etaReturnAndRequireGoodsInf.setCustomerNumber(customerNumber);
                            etaReturnAndRequireGoodsInf.setDiySiteCode(diySiteCode);
                            if (null != shipDate) {
                                etaReturnAndRequireGoodsInf.setShipDate(sdf.parse(shipDate));
                            } else {
                                etaReturnAndRequireGoodsInf.setShipDate(null);
                            }
                            etaReturnAndRequireGoodsInf.setItemCode(itemCode);
                            etaReturnAndRequireGoodsInf.setQuantity(quantity);
                            etaReturnAndRequireGoodsInf.setAttribute1(attribute1);
                            etaReturnAndRequireGoodsInf.setAttribute2(attribute2);
                            etaReturnAndRequireGoodsInf.setAttribute3(attribute3);
                            etaReturnAndRequireGoodsInf.setAttribute4(attribute4);
                            etaReturnAndRequireGoodsInf.setAttribute5(attribute5);
                            diySiteInventoryEbsService.saveReturnAndRequireGoodsInf(etaReturnAndRequireGoodsInf);

                            //判断门店是否存在
                            AppStore appStore = appStoreService.findByStoreCode(diySiteCode);
                            if (appStore == null) {
                                return AppXmlUtil.generateResultXmlToEbs(1, "门店编码为：" + diySiteCode + " 的门店不存在或者不可用");
                            }

                            //根据门店编码和商品sku查询门店库存
                            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsSku(diySiteCode, itemCode);
                            if (null == storeInventory) {
                                return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + "的商品不存在或者不可用");
                            }
                            GoodsDO goodsDO = goodsService.queryBySku(itemCode);
                            //更改门店库存和可用量
                            for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                                Integer goodsQtyAfterChange = storeInventory.getRealIty() + quantity.intValue();
                                if (goodsQtyAfterChange < 0) {
                                    return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + "的商品库存不足");
                                }
                                Integer goodsAvailableItyAfterChange = storeInventory.getAvailableIty() + quantity.intValue();
                                if (goodsAvailableItyAfterChange < 0) {
                                    return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + "的商品可用量不足");
                                }
                                Integer affectLine = maStoreInventoryService.updateStoreInventoryAndAvailableIty(storeInventory.getStoreId(), goodsDO.getGid(), goodsQtyAfterChange, goodsAvailableItyAfterChange, storeInventory.getLastUpdateTime());
                                if (affectLine > 0) {
                                    //新增门店库存变更日志
                                    StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                                    iLog.setAfterChangeQty(goodsAvailableItyAfterChange);
                                    iLog.setChangeQty(quantity.intValue());
                                    iLog.setChangeTime(new Date());
                                    if (quantity > 0) {
                                        iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_IMPORT_GOODS);
                                        iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_IMPORT_GOODS.getDescription());
                                    } else {
                                        iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS);
                                        iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS.getDescription());
                                    }
                                    iLog.setCityId(storeInventory.getCityId());
                                    iLog.setCityName(storeInventory.getCityName());
                                    iLog.setStoreId(storeInventory.getStoreId());
                                    iLog.setStoreCode(storeInventory.getStoreCode());
                                    iLog.setStoreName(storeInventory.getStoreName());
                                    iLog.setGid(goodsDO.getGid());
                                    iLog.setSku(itemCode);
                                    iLog.setSkuName(goodsDO.getSkuName());
                                    iLog.setReferenceNumber(transNumber);
                                    appStoreService.addStoreInventoryAvailableQtyChangeLog(iLog);
                                    break;
                                } else {
                                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                                    }
                                }
                            }
                        } else {
                            return AppXmlUtil.generateResultXmlToEbs(1, "事物编码重复：" + transId);
                        }

                    } catch (Exception e) {
                        logger.info("GetEBSInfo OUT,直营要货退货发生未知异常 出参 e:{}", e);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return AppXmlUtil.resultStrXml(1, "直营要货退货失败!");
                    }
                }
                logger.info("GetEBSInfo OUT,获取ebs信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            }
        } catch (ParserConfigurationException e) {
            logger.warn("GetEBSInfo EXCEPTION,解密后xml参数错误");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml参数错误");

        } catch (IOException | SAXException e) {
            logger.warn("GetEBSInfo EXCEPTION,解密后xml格式不对");
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(1, "解密后xml格式不对");
        }
        return AppXmlUtil.resultStrXml(1, "不支持该表数据传输：" + strTable);
    }

}
