package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.constant.StoreInventoryRealQtyChangeType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.EbsGoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.EbsPriceInfo;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInfLog;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService;
import cn.com.leyizhuang.app.remote.webservice.utils.AppXmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuh
 * Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace = "cn.com.leyizhuang.app.remote.webservice.service",
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
    @Resource
    private GoodsPriceService goodsPriceService;

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
                EtaReturnAndRequireGoodsInfLog etaReturnAndRequireGoodsInfLog = new EtaReturnAndRequireGoodsInfLog();
                etaReturnAndRequireGoodsInfLog.setMsg("解密后XML数据为空");
                etaReturnAndRequireGoodsInfLog.setCreatDate(new Date());
                diySiteInventoryEbsService.saveReturnAndRequireGoodsInfLog(etaReturnAndRequireGoodsInfLog);
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
                    EtaReturnAndRequireGoodsInfLog etaReturnAndRequireGoodsInfLog = new EtaReturnAndRequireGoodsInfLog();
                    etaReturnAndRequireGoodsInfLog.setCreatDate(new Date());
                    etaReturnAndRequireGoodsInfLog.setTransId(transId);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        //判断门店是否存在
                        AppStore appStore = appStoreService.findByStoreCode(diySiteCode);
                        if (null == appStore) {
                            etaReturnAndRequireGoodsInfLog.setMsg("门店编码为：" + diySiteCode + " 的门店不存在或者不可用");
                            return AppXmlUtil.generateResultXmlToEbs(1, "门店编码为：" + diySiteCode + " 的门店不存在或者不可用");
                        }
                        if ("JX".equals(appStore.getStoreType().getValue())) {
                            etaReturnAndRequireGoodsInfLog.setMsg("门店编码为：" + diySiteCode + " 的门店类型错误");
                            return AppXmlUtil.generateResultXmlToEbs(1, "门店编码为：" + diySiteCode + " 的门店类型错误");
                        }
                        //判断商品是否存在
                        GoodsDO goodsDO = goodsService.queryBySku(itemCode);
                        if (null == goodsDO) {
                            etaReturnAndRequireGoodsInfLog.setMsg("商品编码为：" + itemCode + " 的商品不存");
                            return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + " 的商品不存");
                        }

                        //根据门店编码和商品sku查询门店库存
                        StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsSku(diySiteCode, itemCode);

                        Date date = new Date();
                        Integer goodsQtyAfterChange;
                        Integer goodsAvailableItyAfterChange;
                        if (null == storeInventory) {
                            goodsQtyAfterChange = quantity.intValue();
                            goodsAvailableItyAfterChange = quantity.intValue();
                        } else {
                            goodsQtyAfterChange = storeInventory.getRealIty() + quantity.intValue();
                            goodsAvailableItyAfterChange = storeInventory.getAvailableIty() + quantity.intValue();
                        }
                        if (goodsQtyAfterChange < 0) {
                            etaReturnAndRequireGoodsInfLog.setMsg("商品编码为：" + itemCode + "的商品库存不足");
                            return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + "的商品库存不足");
                        }
                        if (goodsAvailableItyAfterChange < 0) {
                            etaReturnAndRequireGoodsInfLog.setMsg("商品编码为：" + itemCode + "的商品可用量不足");
                            return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为：" + itemCode + "的商品可用量不足");
                        }

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
                        } else {
                            etaReturnAndRequireGoodsInfLog.setMsg("事物编码重复：" + transId);
                            return AppXmlUtil.generateResultXmlToEbs(1, "事物编码重复：" + transId);
                        }

                        StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                        iLog.setAfterChangeQty(goodsAvailableItyAfterChange);
                        iLog.setChangeQty(quantity.intValue());
                        iLog.setChangeTime(new Date());
                        if ("门店要货".equals(transType)) {
                            iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_IMPORT_GOODS);
                            iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_IMPORT_GOODS.getDescription());
                        } else if ("门店退货".equals(transType)) {
                            iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS);
                            iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS.getDescription());
                        } else if ("盘点入库".equals(transType)) {
                            iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_INVENTORY_INBOUND);
                            iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_INVENTORY_INBOUND.getDescription());
                        } else if ("盘点出库".equals(transType)) {
                            iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_INVENTORY_OUTBOUND);
                            iLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_INVENTORY_OUTBOUND.getDescription());
                        }
                        iLog.setCityId(appStore.getCityId());
                        iLog.setCityName(appStore.getCity());
                        iLog.setStoreId(appStore.getStoreId());
                        iLog.setStoreCode(appStore.getStoreCode());
                        iLog.setStoreName(appStore.getStoreName());
                        iLog.setGid(goodsDO.getGid());
                        iLog.setSku(itemCode);
                        iLog.setSkuName(goodsDO.getSkuName());
                        iLog.setReferenceNumber(transNumber);

                        //设置门店真实库存日志数据
                        MaStoreRealInventoryChange maStoreRealInventoryChange = new MaStoreRealInventoryChange();
                        maStoreRealInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
                        maStoreRealInventoryChange.setChangeQty(quantity.intValue());
                        maStoreRealInventoryChange.setChangeTime(new Date());
                        if ("门店要货".equals(transType)) {
                            maStoreRealInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_IMPORT_GOODS);
                            maStoreRealInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_IMPORT_GOODS.getDescription());
                        } else if ("门店退货".equals(transType)) {
                            maStoreRealInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_EXPORT_GOODS);
                            maStoreRealInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_EXPORT_GOODS.getDescription());
                        } else if ("盘点入库".equals(transType)) {
                            maStoreRealInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_INVENTORY_INBOUND);
                            maStoreRealInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_INVENTORY_INBOUND.getDescription());
                        } else if ("盘点出库".equals(transType)) {
                            maStoreRealInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_INVENTORY_OUTBOUND);
                            maStoreRealInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_INVENTORY_OUTBOUND.getDescription());
                        }
                        maStoreRealInventoryChange.setCityId(appStore.getCityId());
                        maStoreRealInventoryChange.setCityName(appStore.getCity());
                        maStoreRealInventoryChange.setStoreId(appStore.getStoreId());
                        maStoreRealInventoryChange.setStoreCode(appStore.getStoreCode());
                        maStoreRealInventoryChange.setStoreName(appStore.getStoreName());
                        maStoreRealInventoryChange.setGid(goodsDO.getGid());
                        maStoreRealInventoryChange.setSku(itemCode);
                        maStoreRealInventoryChange.setSkuName(goodsDO.getSkuName());
                        maStoreRealInventoryChange.setReferenceNumber(transNumber);
                        //更改门店库存和可用量
                        if (null == storeInventory) {
                            MaStoreInventory maStoreInventory = new MaStoreInventory();
                            maStoreInventory.setCityId(appStore.getCityId());
                            maStoreInventory.setCityCode(appStore.getCityCode());
                            maStoreInventory.setStoreName(appStore.getStoreName());
                            maStoreInventory.setSku(goodsDO.getSku());
                            maStoreInventory.setStoreId(appStore.getStoreId());
                            maStoreInventory.setStoreCode(appStore.getStoreCode());
                            maStoreInventory.setCreateTime(date);
                            maStoreInventory.setGid(goodsDO.getGid());
                            maStoreInventory.setSkuName(goodsDO.getSkuName());
                            maStoreInventory.setLastUpdateTime(new Timestamp(date.getTime()));
                            maStoreInventory.setRealIty(goodsQtyAfterChange);
                            maStoreInventory.setAvailableIty(goodsAvailableItyAfterChange);
                            maStoreInventoryService.saveStoreInventory(maStoreInventory);
                            appStoreService.addStoreInventoryAvailableQtyChangeLog(iLog);
                            maStoreInventoryService.addRealInventoryChangeLog(maStoreRealInventoryChange);

                        } else {
                            for (int j = 1; j <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; j++) {
                                Integer affectLine = maStoreInventoryService.updateStoreInventoryAndAvailableIty(storeInventory.getStoreId(), goodsDO.getGid(), goodsQtyAfterChange, goodsAvailableItyAfterChange, storeInventory.getLastUpdateTime());
                                if (affectLine > 0) {
                                    //新增门店库存变更日志
                                    appStoreService.addStoreInventoryAvailableQtyChangeLog(iLog);
                                    maStoreInventoryService.addRealInventoryChangeLog(maStoreRealInventoryChange);
                                    break;
                                } else {
                                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                        etaReturnAndRequireGoodsInfLog.setMsg("直营要货退货失败,系统繁忙!");
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        return AppXmlUtil.resultStrXml(1, "直营要货退货失败,系统繁忙!");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.info("GetEBSInfo OUT,直营要货退货发生未知异常 出参 e:{}", e);
                        etaReturnAndRequireGoodsInfLog.setMsg("直营要货退货发生未知异常");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return AppXmlUtil.resultStrXml(1, "直营要货退货失败!");
                    } finally {
                        diySiteInventoryEbsService.saveReturnAndRequireGoodsInfLog(etaReturnAndRequireGoodsInfLog);
                    }
                }
                logger.info("GetEBSInfo OUT,获取ebs信息成功 出参 code=0");
                return AppXmlUtil.resultStrXml(0, "NORMAL");
            } else if ("CUXAPP_INV_GOODS_TRANS_OUT".equalsIgnoreCase(strTable)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    //商品名称
                    String skuName = null;
                    //商品编码
                    String sku = null;
                    //单位单位
                    String goodsUnit = null;
                    //商品公司标识
                    String companyFlag = null;
                    //产品条码
                    String materialsCode = null;
                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            if ("ITEM_CODE".equalsIgnoreCase(childNode.getNodeName())) {
                                // 有值
                                if (null != childNode.getChildNodes().item(0)) {
                                    sku = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ITEM_DESC".equalsIgnoreCase(childNode.getNodeName())) {
                                // 有值
                                if (null != childNode.getChildNodes().item(0)) {
                                    skuName = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ITEM_MEASURE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    goodsUnit = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ITEM_IDENTIFY".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    companyFlag = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("ITEM_BARCODE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    materialsCode = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            }
                        }
                    }
                    GoodsDO goodsDO = new GoodsDO();
                    try {
                        if (null == sku) {
                            logger.info("产品编码为空");
                            return AppXmlUtil.generateResultXmlToEbs(1, "商品编码为空");
                        }
                        if (null == skuName) {
                            logger.info("产品名称为空");
                            return AppXmlUtil.generateResultXmlToEbs(1, "产品名称为空");
                        }
                        if (null == companyFlag) {
                            logger.info("产品标识为空");
                            return AppXmlUtil.generateResultXmlToEbs(1, "产品标识为空");
                        }
                        if (null == goodsUnit) {
                            logger.info("产品单位为空");
                            return AppXmlUtil.generateResultXmlToEbs(1, "产品单位为空");
                        }

                        goodsDO.setSku(sku);
                        goodsDO.setSkuName(skuName);
                        goodsDO.setGoodsUnit(goodsUnit);
                        goodsDO.setCreateTime(new Date());
                        goodsDO.setCompanyFlag(companyFlag);
                        if (null != materialsCode) {
                            goodsDO.setMaterialsCode(materialsCode);
                        }

                        //判断sku是否存在
                        GoodsDO goodsDOExist = goodsService.queryBySku(sku);
                        if (null != goodsDOExist) {
                            goodsService.modifySynchronize(goodsDO);
                        } else {
                            goodsService.saveSynchronize(goodsDO);
                        }
                        return AppXmlUtil.resultStrXml(0, "同步EbsToApp商品成功!");
                    } catch (Exception e) {
                        logger.info("GetEBSInfo OUT,同步EbsToApp商品失败 出参 e:{}", e);
                        return AppXmlUtil.resultStrXml(1, "同步EbsToApp商品失败!");
                    }
                }
            } else if ("".equalsIgnoreCase(strTable)) {

                StringBuffer sb = new StringBuffer();
                GoodsPrice goodsPrice = new GoodsPrice();
                DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int cs = 1;
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    //门店编码
                    String storeCode = null;
                    //sku
                    String sku = null;
                    //会员价
                    String VIPPrice = null;
                    //零售价
                    String retailPrice = null;
                    //经销价
                    String wholesalePrice = null;
                    //开始时间
                    String startTime = null;
                    //结束时间
                    String endTime = null;
                    //价目表类型
                    String priceType = null;
                    //行id
                    String lineId = null;
                    //价目表名称
                    String priceListName = null;

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        if (childNode.getNodeType() == 1) {
                            if ("STORE_CODE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    storeCode = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("SKU".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    sku = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("VIP_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    VIPPrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("RETAIL_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    retailPrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("WHOLESALE_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    wholesalePrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("START_TIME".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    startTime = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("END_TIME".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    endTime = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("PRICE_TYPE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    priceType = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("LINE_ID".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    lineId = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("PRICE_LIST_NAME".equalsIgnoreCase(childNode.getNodeName()) && null != childNode.getChildNodes().item(0)) {
                                priceListName = childNode.getChildNodes().item(0).getNodeValue();
                            }
                        }
                    }
                    try {
                        int a = sb.length();
                        if (null == priceListName) {
                            logger.info("价目表名称为空");
                            sb.append("|行ID为" + lineId + "的价目表名称为空,请检查,价目表名称:" + priceListName + "| ");
                        }

                        if (null == sku) {
                            logger.info("产品编码为空");
                            sb.append("|行ID为" + lineId + "的产品编码为空,请检查,价目表名称:" + priceListName + "| ");
                        }

                        if (null == priceType) {
                            logger.info("价目表类型为空");
                            sb.append("|行ID为" + lineId + "的价目表类型为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "| ");
                        } else if (!priceType.equals("COMMON") && !priceType.equals("A") && !priceType.equals("C")) {
                            logger.info("价目表类型错误");
                            sb.append("|行ID为" + lineId + "的价目表类型错误,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "| ");
                        }

                        if (null == storeCode) {
                            logger.info("门店编码为空");
                            sb.append("|行ID为" + lineId + "的门店编码为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "价目表类型:" + priceType + "| ");
                        }

                        if (null == VIPPrice || 0.0D == Double.parseDouble(VIPPrice)) {
                            logger.info("会员价为空");
                            sb.append("|行ID为" + lineId + "的会员价等于0或为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "价目表类型:" + priceType + "| ");
                        }

                        if (null == retailPrice || 0.0D == Double.parseDouble(retailPrice)) {
                            logger.info("零售价为空");
                            sb.append("|行ID为" + lineId + "的零售价等于0或为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "价目表类型:" + priceType + "| ");
                        }

                        if (null == wholesalePrice || 0.0D == Double.parseDouble(wholesalePrice)) {
                            logger.info("经销价为空");
                            sb.append("|行ID为" + lineId + "的经销价等于0或为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + "价目表类型:" + priceType + "| ");
                        }

                        if (null == startTime) {
                            logger.info("生效时间为空");
                            sb.append("|行ID为" + lineId + "的生效时间为空,请检查,价目表名称:" + priceListName + "产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                        }

                        if (sb.length() > a) {
                            continue;
                        }

                        Date date = new Date();

                        if (sb.length() <= a) {
                            startTime = startTime + " 00:00:00";
                            if (null != endTime) {
                                endTime = endTime + " 23:59:59";
                                Date endT = bf.parse(endTime);
                                Date startT = bf.parse(startTime);
                                if (endT.before(startT)) {
                                    sb.append("|行ID为" + lineId + "的失效时间小于生效时间,请检查,价目表名称:" + priceListName + " 产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                                }
                                if (endT.before(date)) {
                                    sb.append("|行ID为" + lineId + "的价目表已失效,请检查,价目表名称:" + priceListName + " 产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                                }
                            }

                            if (Double.parseDouble(retailPrice) < Double.parseDouble(VIPPrice) || Double.parseDouble(VIPPrice) < Double.parseDouble(wholesalePrice) || Double.parseDouble(retailPrice) < Double.parseDouble(wholesalePrice)) {
                                logger.info("价目表数据错误");
                                sb.append("|行ID为" + lineId + "的价目表价格错误,价格必须为零售价>=会员价>=经销价,请检查价格,价目表名称:" + priceListName + " 产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                            }

                            AppStore appStore = this.appStoreService.findByStoreCode(storeCode);
                            if (null == appStore) {
                                logger.info("门店编码错误");
                                sb.append("|行ID为" + lineId + ",编码为" + storeCode + "的门店不存在,请检查,价目表名称:" + priceListName + " 产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                            }

                            GoodsDO goodsDO = this.goodsService.findBySku(sku);
                            if (null == goodsDO) {
                                logger.info("商品sku错误");
                                sb.append("|行ID为" + lineId + ",sku为" + sku + "的商品不存在,请检查,价目表名称:" + priceListName + " 产品编码：" + sku + " 价目表类型:" + priceType + "| ");
                            }

                            if (sb.length() > a) {
                                continue;
                            }

                            if (sb.length() <= 0) {
                                if(1== cs){
                                    //goodsPriceService.delGoodsPriceListByStoreIdAndSkuAndpriceType(appStore.getStoreId(),priceType ,sku);
                                    List<GoodsPrice> goodsPriceList = goodsPriceService.findGoodsPriceListByStoreIdAndSkuAndpriceType(appStore.getStoreId(),priceType ,sku );
                                    for (GoodsPrice goodsPricel : goodsPriceList) {
                                        //goodsPriceService.saveBackupsGoodsPrice(goodsPricel);
                                    }

                                    //goodsPriceService.saveBackupsGoodsPrice(goodsPrice);
                                    cs ++;
                                }
                                goodsPrice.setStartTime(LocalDateTime.parse(startTime, df));
                                goodsPrice.setGid(goodsDO.getGid());
                                goodsPrice.setSku(sku);
                                goodsPrice.setPriceType(priceType);
                                goodsPrice.setRetailPrice(Double.parseDouble(retailPrice));
                                goodsPrice.setVIPPrice(Double.parseDouble(VIPPrice));
                                goodsPrice.setWholesalePrice(Double.parseDouble(wholesalePrice));
                                goodsPrice.setStoreId(appStore.getStoreId());
                                goodsPrice.setPriceLineId(Long.parseLong(lineId));
                            }
                        }
                    } catch (Exception e) {
                        logger.info("GetEBSInfo OUT,同步EbsToApp价目表同步失败 出参 e:{}", e);
                        return AppXmlUtil.resultStrXml(Integer.valueOf(1), "同步EbsToApp价目表同步失败!,价目表名称:" + priceListName + "| ");
                    }
                }
                if (sb.length() > 0) {
                    return AppXmlUtil.resultStrXml(Integer.valueOf(1), sb.toString());
                }
                return AppXmlUtil.resultStrXml(Integer.valueOf(0), "同步EbsToApp价目表同步成功");
            }
        } catch (ParserConfigurationException pe) {
            logger.warn("GetEBSInfo EXCEPTION,解密后xml参数错误");
            logger.warn("{}", pe);
            return AppXmlUtil.resultStrXml(Integer.valueOf(1), "解密后xml参数错误");
        } catch (SAXException | IOException se) {
            logger.warn("GetEBSInfo EXCEPTION,解密后xml格式不对");
            logger.warn("{}", se);
            return AppXmlUtil.resultStrXml(Integer.valueOf(1), "解密后xml格式不对");
        } catch (Exception e) {
            logger.warn("{}", e);
            return AppXmlUtil.resultStrXml(Integer.valueOf(1), e.getMessage());
        }
        logger.info("GetEBSInfo OUT,获取ebs信息成功 出参 code=0");
        return AppXmlUtil.resultStrXml(Integer.valueOf(0), "NORMAL");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String GetEBSPriceInfo(String table, String STOREID, List<String> xml) {
        StringBuffer sb = new StringBuffer();
        try {
            DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Document document = null;
            List<String> priceList = new ArrayList<>();
            // List<EbsGoodsPrice> priceList = ebsPriceInfo.getERP();
         /*   if (null == priceList || priceList.size() == 0) {
                return AppXmlUtil.resultStrXml(Integer.valueOf(1), "同步EbsToApp价目表同步失败 ");
            }*/

          /* // List<GoodsPrice> goodsPriceList = goodsPriceService.findGoodsPriceListByStoreId(Long.parseLong(storeId));
            for (GoodsPrice goodsPricel : goodsPriceList) {
                goodsPriceService.saveBackupsGoodsPrice(goodsPricel);
            }*/
            //goodsPriceService.delGoodsPriceListByStoreId(Long.parseLong(storeId));

            for (String stringXml : xml) {
                if(sb.length()>2000){
                    return AppXmlUtil.resultStrXml(Integer.valueOf(1), sb.toString());
                }
                logger.info("GetEBSPriceInfo CALLED,获取ebs信息，入参xml:{}", xml);

                document = AppXmlUtil.parseStrXml(stringXml);
                NodeList nodeList = document.getElementsByTagName("TABLE");

                for (int i = 0; i < nodeList.getLength(); i++) {

                    String storeCode = null;
                    //sku
                    String sku = null;
                    //会员价
                    String VIPPrice = null;
                    //零售价
                    String retailPrice = null;
                    //经销价
                    String wholesalePrice = null;
                    //开始时间
                    String startTime = null;
                    //结束时间
                    String endTime = null;
                    //价目表类型
                    String priceType = null;
                    //行id
                    String lineId = null;
                    //价目表名称
                    String priceListName = null;

                    Node node = nodeList.item(i);
                    NodeList childNodeList = node.getChildNodes();
                    for (int idx = 0; idx < childNodeList.getLength(); idx++) {
                        Node childNode = childNodeList.item(idx);
                        if (childNode.getNodeType() == 1) {
                            if ("STORE_CODE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    storeCode = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("SKU".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    sku = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("VIP_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    VIPPrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("RETAIL_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    retailPrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("WHOLESALE_PRICE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    wholesalePrice = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("START_TIME".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    startTime = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("END_TIME".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    endTime = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("PRICE_TYPE".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    priceType = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("LINE_ID".equalsIgnoreCase(childNode.getNodeName())) {
                                if (null != childNode.getChildNodes().item(0)) {
                                    lineId = childNode.getChildNodes().item(0).getNodeValue();
                                }
                            } else if ("PRICE_LIST_NAME".equalsIgnoreCase(childNode.getNodeName()) && null != childNode.getChildNodes().item(0)) {
                                priceListName = childNode.getChildNodes().item(0).getNodeValue();
                            }
                        }
                    }
                    try {
                        int a = sb.length();
                        if (null == priceListName) {
                            logger.info("价目表名称为空");
                            sb.append("|行ID为" + lineId + "的价目表名称为空,请检查"+"| ");
                        }

                        if (null == sku) {
                            logger.info("产品编码为空");
                            sb.append("|行ID为" + lineId + "的产品编码为空,请检查,价目表名称:" + priceListName + "| ");
                        }

                        if (null == priceType) {
                            logger.info("价目表类型为空");
                            sb.append("|行ID为" + lineId + "的价目表类型为空,请检查,产品编码：" + sku + "| ");
                        } else if (!priceType.equals("COMMON") && !priceType.equals("A") && !priceType.equals("C")) {
                            logger.info("价目表类型错误");
                            sb.append("|行ID为" + lineId + "的价目表类型错误,请检查,产品编码：" + sku + "| ");
                        }

                        if (null == storeCode) {
                            logger.info("门店编码为空");
                            sb.append("|行ID为" + lineId + "的门店编码为空,请检查,产品编码：" + sku+ "| ");
                        }

                        if (null == VIPPrice || 0.0D == Double.parseDouble(VIPPrice)) {
                            logger.info("会员价为空");
                            sb.append("|行ID为" + lineId + "的会员价等于0或为空,请检查,产品编码：" + sku + "| ");
                        }

                        if (null == retailPrice || 0.0D == Double.parseDouble(retailPrice)) {
                            logger.info("零售价为空");
                            sb.append("|行ID为" + lineId + "的零售价等于0或为空,请检查,产品编码：" + sku + "| ");
                        }

                        if (null == wholesalePrice || 0.0D == Double.parseDouble(wholesalePrice)) {
                            logger.info("经销价为空");
                            sb.append("|行ID为" + lineId + "的经销价等于0或为空,请检查,产品编码：" + sku +  "| ");
                        }

                        if (null == startTime && !"".equals(startTime)) {
                            logger.info("生效时间为空");
                            sb.append("|行ID为" + lineId + "的生效时间为空,请检查,产品编码：" + sku +  "| ");
                        }

                        if (sb.length() > a) {
                            sb = sb.insert(0,"价目表名称:"+priceListName+"|");
                            continue;
                        }

                        if (sb.length() <= a) {
                            startTime = startTime + " 00:00:00";
                            if (null != endTime && !"".equals(endTime)) {
                                endTime = endTime + " 23:59:59";
                                Date endT = bf.parse(endTime);
                                Date startT = bf.parse(startTime);
                                if (endT.before(startT)) {
                                    sb.append("|行ID为" + lineId + "的失效时间小于生效时间,请检查,产品编码：" + sku + "| ");
                                }
                            }

                            if (Double.parseDouble(retailPrice) < Double.parseDouble(VIPPrice) || Double.parseDouble(VIPPrice) < Double.parseDouble(wholesalePrice) || Double.parseDouble(retailPrice) < Double.parseDouble(wholesalePrice)) {
                                logger.info("价目表数据错误");
                                sb.append("|行ID为" + lineId + "的价目表价格错误,价格必须为零售价>=会员价>=经销价,请检查价格,产品编码：" + sku +  "| ");
                            }

                            AppStore appStore = this.appStoreService.findByStoreCode(storeCode);
                            if (null == appStore) {
                                logger.info("门店编码错误");
                                sb.append("|行ID为" + lineId + ",编码为" + storeCode + "的门店不存在,请检查,产品编码：" + sku + "| ");
                            }

                            GoodsDO goodsDO = this.goodsService.findBySku(sku);
                            if (null == goodsDO) {
                                logger.info("商品sku错误");
                                sb.append("|行ID为" + lineId + ",sku为" + sku + "的商品不存在,请检查,产品编码：" + sku +  "| ");
                            }

                            if (sb.length() > a) {
                                sb = sb.insert(0,"价目表名称:"+priceListName+"|");
                                continue;
                            }

                            if (sb.length() <= a) {
                                GoodsPrice goodsPrice = new GoodsPrice();
                                    List<GoodsPrice> goodsPriceList = goodsPriceService.findGoodsPriceListByStoreIdAndSkuAndpriceType(appStore.getStoreId(),priceType ,sku );
                                    String pdpPrice =appStore.getStoreId()+priceType+sku;
                                    if(!priceList.contains(pdpPrice)){
                                        for (GoodsPrice goodsPricel : goodsPriceList) {
                                            goodsPriceService.saveBackupsGoodsPrice(goodsPricel);
                                        }
                                        goodsPriceService.delGoodsPriceListByStoreIdAndSkuAndpriceType(appStore.getStoreId(),priceType ,sku);
                                        priceList.add(pdpPrice);
                                    }
                                if (null != endTime && !"".equals(endTime)) {
                                    goodsPrice.setEndTime(LocalDateTime.parse(endTime, df));
                                }
                                goodsPrice.setStartTime(LocalDateTime.parse(startTime, df));
                                goodsPrice.setGid(goodsDO.getGid());
                                goodsPrice.setSku(sku);
                                goodsPrice.setPriceType(priceType);
                                goodsPrice.setRetailPrice(Double.parseDouble(retailPrice));
                                goodsPrice.setVIPPrice(Double.parseDouble(VIPPrice));
                                goodsPrice.setWholesalePrice(Double.parseDouble(wholesalePrice));
                                goodsPrice.setStoreId(appStore.getStoreId());
                                goodsPrice.setPriceLineId(Long.parseLong(lineId));
                                this.goodsPriceService.save(goodsPrice);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("GetEBSInfo OUT,同步EbsToApp价目表同步失败 出参 e:{}", e);
                        return AppXmlUtil.resultStrXml(Integer.valueOf(1), "同步EbsToApp价目表同步失败!,价目表名称:" + priceListName + "| 商品sku:"+sku);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("GetEBSInfo OUT,同步EbsToApp价目表失败 出参 e:{}", e);
            return AppXmlUtil.resultStrXml(1, "同步EbsToApp商品价目表失败!");
        }
        if (sb.length() > 0) {
            return AppXmlUtil.resultStrXml(Integer.valueOf(1), sb.toString());
        }
        return AppXmlUtil.resultStrXml(Integer.valueOf(0), "同步EbsToApp价目表同步成功!"  );
    }
}

