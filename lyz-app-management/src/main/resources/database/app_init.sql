/* 创建管理页面菜单信息表 */
CREATE TABLE IF NOT EXISTS admin_menu (

  ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改者ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改者类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',

  TITLE VARCHAR(10) NOT NULL UNIQUE COMMENT '菜单标题',
  ICON_STYLE VARCHAR(50) COMMENT '图标样式',
  PARENT_ID BIGINT(20) NOT NULL DEFAULT 0 COMMENT '父节点ID',
  PARENT_TITLE VARCHAR(50) COMMENT '父节点名称',
  LINK_URI VARCHAR(100) COMMENT '链接地址',
  SORT_ID INT(5) NOT NULL DEFAULT 0 COMMENT '排序号',
  TYPE VARCHAR(10) NOT NULL COMMENT '菜单类型',
  REFERENCE_TABLE VARCHAR(20) COMMENT '相关表名',

  INDEX(PARENT_ID) COMMENT '父节点ID索引'
) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/**
  创建APP 会员表
 */
CREATE TABLE IF NOT EXISTS member (
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  MEMBER_NAME VARCHAR(20) NOT NULL COMMENT '会员姓名',
  SEX VARCHAR(10) COMMENT '会员性别',
  BIRTHDAY DATETIME COMMENT '会员生日',
  STORE_ID BIGINT(20) NOT NULL COMMENT '归属门店ID',
  CONSULT_ID BIGINT(20) NOT NULL COMMENT '归属销售顾问ID',
  HEAD_IMAGE_URI  VARCHAR(100) COMMENT '会员头像路径',
  EFFECTIVE_CONSUMPTION BIGINT(20) COMMENT '有效消费额',
  EFFECTIVE_ORDER_COUNT INT(10) COMMENT '有效单量',
  LAST_LOGIN_TIME DATETIME COMMENT '上一次登录时间',
  IS_LOGIN BIT COMMENT '当前是否已登录',
  REGISTRATION_TIME DATETIME COMMENT '注册时间',
  REGISTRATION_TYPE VARCHAR(50) COMMENT '会员注册方式',
  IDENTITY_TYPE VARCHAR(50) COMMENT '会员性质'
) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/*会员资产信息表*/
CREATE TABLE IF NOT EXISTS member_wallet(
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  MEMBER_ID BIGINT(20) NOT NULL COMMENT '会员主键',
  BALANCE BIGINT(20) NOT NULL COMMENT '会员预存款',
  TREASURE BIGINT(20) NOT NULL COMMENT '会员乐易宝'

) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/*会员等级信息表*/
CREATE TABLE IF NOT EXISTS member_level(
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  TITLE VARCHAR(20) NOT NULL COMMENT '会员等级名称',
  ICON_URI VARCHAR(100) NOT NULL COMMENT '会员图标地址路径',
  RANK INTEGER(10) COMMENT '会员级别（表示会员等级高低）'

) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/*会员鉴权信息表*/
CREATE TABLE IF NOT EXISTS member_auth(
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  MEMBER_ID BIGINT(20) NOT NULL COMMENT '会员主键',
  USER_NAME VARCHAR(20) NOT NULL COMMENT '会员名字',
  PASSWORD VARCHAR(50) NOT NULL COMMENT '会员密码',
  MOBILE VARCHAR (20) NOT NULL COMMENT '会员手机号码',
  EMAIL VARCHAR (20) COMMENT '会员电子邮箱',
  STATUS BIT NOT NULL COMMENT '会员账号是否启用',
  DISABLE_END_TIME DATETIME COMMENT '会员账号禁用结束时间'

) DEFAULT CHARSET = UTF8 ENGINE = INNODB;
/*会员特定属性表*/
CREATE TABLE IF NOT EXISTS member_specific_property(
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  MEMBER_ID BIGINT(20) NOT NULL COMMENT '会员主键',
  IS_LOGIN BIT NOT NULL COMMENT '会员当前是否已登录',
  LAST_VISIT_TIME DATETIME COMMENT '上一次操作时间',
  LOGIN_SESSION VARCHAR (50) COMMENT '登录session',
  IS_CASH_ON_DELIVERY BIT NOT NULL COMMENT '是否允许货到付款'

) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/*会员角色表*/
CREATE TABLE IF NOT EXISTS member_role (
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  TITLE VARCHAR(30) NOT NULL COMMENT '角色名称'

) DEFAULT CHARSET = UTF8 ENGINE = INNODB;

/*门店库存表 */
CREATE TABLE IF NOT EXISTS store_inventory (
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  STORE_ID int(10) NOT NULL COMMENT '门店ID',
  GOODS_ID int(10) NOT NULL COMMENT '商品ID',
  REAL_INVENTORY int(10) NOT NULL COMMENT '真实库存',
  SOLD_INVENTORY int(10) NOT NULL COMMENT '可售库存'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*商品表*/
CREATE TABLE IF NOT EXISTS goods(
  ID BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  GOODS_NAME VARCHAR(100) NOT NULL COMMENT '商品名称',
  GOODS_CODE VARCHAR(50) NOT NULL COMMENT '商品编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*门店表*/

CREATE TABLE IF NOT EXISTS store(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  STORE_NAME VARCHAR(100) NOT NULL COMMENT '门店名称',
  STORE_CODE VARCHAR(50) NOT NULL COMMENT '门店编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*销售顾问表*/
CREATE TABLE IF NOT EXISTS sales_consult(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '数据创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '数据创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改人ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改人类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',
  CONSULT_NAME VARCHAR(100) NOT NULL COMMENT '销售顾问名称',
  CONSULT_MOBILE_PHONE VARCHAR(50) NOT NULL COMMENT '销售顾问电话',
  ASCRIPTION_STORE_ID INT (10) NOT NULL COMMENT '归属门店ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*管理员表*/
CREATE TABLE IF NOT EXISTS user(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  LOGIN_NAME VARCHAR(100) NOT NULL COMMENT '登录名',
  NAME VARCHAR(100) NOT NULL COMMENT '管理员姓名',
  PASSWORD VARCHAR(100) NOT NULL COMMENT '密码',
  SEX VARCHAR(10) COMMENT '管理员性别',
  AGE INT(10) COMMENT '年龄',
  PHONE VARCHAR(20) COMMENT '电话号码',
  USER_TYPE INT (10) COMMENT '管理员类别',
  STATUS BIT (1) COMMENT '状态',
  CREATE_TIME DATETIME NOT NULL COMMENT '管理员账号创建时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*角色表*/
CREATE TABLE IF NOT EXISTS role(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  NAME VARCHAR(100) NOT NULL COMMENT '角色名称',
  SEQ INT NOT NULL COMMENT '排序号',
  DESCRIPTION VARCHAR(100) NOT NULL COMMENT  '角色描述',
  STATUS INT NOT NULL COMMENT '状态'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*管理员角色对照表*/
CREATE TABLE IF NOT EXISTS user_role(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  USER_ID BIGINT NOT NULL COMMENT '用户ID',
  ROLE_ID BIGINT NOT NULL COMMENT '角色ID'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*资源表*/
CREATE TABLE IF NOT EXISTS resource(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  NAME VARCHAR(50) NOT NULL COMMENT '资源名称',
  URL VARCHAR(100) NOT NULL COMMENT '资源URL',
  DESCRIPTION VARCHAR(100) NOT NULL COMMENT '资源描述',
  ICON VARCHAR(50) NOT NULL COMMENT '资源图标',
  STATUS BIT NOT NULL COMMENT '状态',
  RESOURCE_TYPE INT NOT NULL COMMENT '资源类型',
  SEQ INT NOT NULL COMMENT '排序号',
  PID INT NOT NULL COMMENT '父级资源ID',
  CREATE_TIME DATETIME NOT NULL COMMENT '资源创建时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*角色资源对照表*/
CREATE TABLE IF NOT EXISTS role_resource(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
  ROLE_ID BIGINT NOT NULL COMMENT '角色ID',
  RESOURCE_ID BIGINT NOT NULL COMMENT '资源ID',
  foreign key(ROLE_ID) references ROLE(ID) ON DELETE CASCADE ON UPDATE CASCADE ,
  foreign key(RESOURCE_ID) references RESOURCE(ID) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 创建商品表 */
CREATE TABLE IF NOT EXISTS GOODS (
  ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
  CREATOR_ID BIGINT(20) NOT NULL COMMENT '创建者ID',
  CREATOR_TYPE VARCHAR(100) NOT NULL COMMENT '创建者类型',
  CREATE_TIME DATETIME NOT NULL COMMENT '数据创建时间',
  MODIFIER_ID BIGINT(20) NOT NULL COMMENT '上一次修改者ID',
  MODIFIER_TYPE VARCHAR(100) NOT NULL COMMENT '上一次修改者类型',
  MODIFY_TIME DATETIME NOT NULL COMMENT '上一次修改时间',

  GOODS_NAME VARCHAR(50) NOT NULL UNIQUE COMMENT '商品名称',
  GOODS_CODE VARCHAR(20) NOT NULL UNIQUE COMMENT '商品编码',
  IS_GIFT BIT(1) NOT NULL DEFAULT FALSE COMMENT '是否为小辅料',
  TITLE VARCHAR(50) NOT NULL COMMENT '商品标题',
  SUB_TITLE VARCHAR(50) NOT NULL COMMENT '副标题',
  COVER_IMAGE_URI VARCHAR(50) COMMENT '封面图片',
  SHOW_PICTURES VARCHAR(50) COMMENT '轮播展示图片，多张图片以,隔开',
  DETAIL VARCHAR(50) COMMENT '商品详情',
  IS_ON_SALE BIT(1) NOT NULL DEFAULT TRUE COMMENT '是否上架',
  IS_RECOMMEND_INDEX BIT(1) COMMENT '是否首页推荐',
  IS_RECOMMEND_TYPE BIT(1) COMMENT '是否分类推荐',
  IS_HOT BIT(1) COMMENT '是否热销',
  IS_NEW BIT(1) COMMENT '是否新品',
  IS_SPECIAL_PRICE BIT(1) COMMENT '是否特价',
  CATEGORY_ID BIGINT(5) COMMENT '商品类型',
  CATEGORY_TITLE VARCHAR(50) COMMENT '商品类型名称',
  CATEGORY_ID_TREE VARCHAR(50) COMMENT '商品所有类型',
  MARKET_PRICE DOUBLE(20,2) COMMENT '市场价',
  SALE_PRICE DOUBLE(20,2) COMMENT '销售价',
  LEFT_NUMBER BIGINT(10) COMMENT '库存数量',
  PRICE_UNIT VARCHAR(50) COMMENT '商品价格单位',
  ONSALE_TIME DATETIME NOT NULL COMMENT '上架时间',
  SORT_ID BIGINT(2) COMMENT '排序号',
  BRAND_TITLE VARCHAR(5) NOT NULL COMMENT '品牌',
  BRAND_ID BIGINT(10) NOT NULL COMMENT '品牌ID',
  PRODUCT_FLAG VARCHAR(5) NOT NULL COMMENT '品牌FLAG',
  RETURN_PRICE DOUBLE(20,2) COMMENT '商品返现金额',
  IS_COLOR_PACKAGE BIT(1) COMMENT '是否是调色包',
  IS_COLORFUL BIT(1) COMMENT '是否调色产品',
  COLOR_PACKAGE_SKU VARCHAR(5) COMMENT '可调色的调色包SKU编号（多个调色包以","分割）',
  INVENTORY_ITEM_ID BIGINT(10) NOT NULL COMMENT '物料ID（唯一标识）',
  INV_CATEGORY_ID BIGINT(10) COMMENT '库存分类ID',
  ITEM_TYPE_NAME VARCHAR(25) COMMENT '物料类型名称',
  ITEM_TYPE_CODE VARCHAR(10) COMMENT '物料类型CODE',
  INVENTORY_ITEM_STATUS BIT(1) COMMENT '物料状态 0 失效，1 有效',
  ITEM_BARCODE VARCHAR(20) COMMENT '产品条码',
  UNIT_NAME VARCHAR(5) COMMENT '单位名称',

  INDEX(GOODS_NAME) COMMENT '商品名称',
  INDEX(GOODS_CODE) COMMENT '商品编码',
  INDEX(CATEGORY_TITLE) COMMENT '商品类型名称',
  INDEX(ONSALE_TIME) COMMENT '上架时间'
) DEFAULT CHARSET = UTF8 ENGINE = INNODB;
