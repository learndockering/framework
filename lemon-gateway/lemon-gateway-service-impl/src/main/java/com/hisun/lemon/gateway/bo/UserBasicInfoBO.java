package com.hisun.lemon.gateway.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 
 * @author yuzhou
 * @date 2017年9月26日
 * @time 上午11:48:35
 *
 */
public class UserBasicInfoBO implements UserInfoBase, Serializable {
    private static final long serialVersionUID = 905689819211299573L;

    private String userId;

    private String mblNo;
    /**
     * @Fields usrSts 用户状态 0:开户 1:销户
     */
    private String usrSts;
    /**
     *  用户级别 0:普通用户 1:企业用户 2:个人商家 3：企业商家
     */
    private String usrLvl;
    /**
     * @Fields idChkFlg 实名标志 0：非实名 1：实名
     */
    private String idChkFlg;
    /**
     * @Fields idType 证件类型
     */
    private String idType;
    /**
     * @Fields idNo 证件号码
     */
    private String idNo;
    /**
     * @Fields idNoHid 脱敏证件号码
     */
    private String idNoHid;
    /**
     * @Fields usrNm 用户姓名
     */
    private String usrNm;
    /**
     * @Fields usrNmHid 脱敏用户姓名
     */
    private String usrNmHid;
    /**
     * @Fields usrGender 用户性别 M：男 F：女
     */
    private String usrGender;
    
    private String usrNation;
    /**
     * @Fields usrBirthDt 出生日期
     */
    private String usrBirthDt;
    /**
     * @Fields issuAuth 签发机关
     */
    private String issuAuth;
    /**
     * @Fields idEffDt 证件有效期起始
     */
    private String idEffDt;
    /**
     * @Fields idExpDt 证件有效期截止
     */
    private String idExpDt;
    /**
     * @Fields usrRegCnl 注册渠道
     */
    private String usrRegCnl;
    /**
     * @Fields usrRegIp 注册IP
     */
    private String usrRegIp;
    /**
     * @Fields usrRegDt 注册日期
     */
    private LocalDate usrRegDt;
    /**
     * @Fields usrRegTm 注册时间
     */
    private LocalTime usrRegTm;
    /**
     * @Fields usrClsDt 销户日期
     */
    private LocalDate usrClsDt;
    /**
     * @Fields usrClsTm 销户时间
     */
    private LocalTime usrClsTm;

    //商户扩展信息
    /**
     * @Fields mercName 商户名称
     */
    private String mercName;
    /**
     * @Fields mercShortName 商户简称
     */
    private String mercShortName;
    /**
     * @Fields cprRegNmCn 注册名称(中文)
     */
    private String cprRegNmCn;
    /**
     * @Fields cprOperNmCn 经营名称（中文）
     */
    private String cprOperNmCn;
    /**
     * @Fields prinNm 负责人名称
     */
    private String prinNm;
    /**
     * @Fields comercReg 工商注册号
     */
    private String comercReg;
    /**
     * @Fields socialCrdCd 社会信用代码
     */
    private String socialCrdCd;
    /**
     * @Fields orgCd 组织机构代码
     */
    private String orgCd;
    /**
     * @Fields busiLisc 营业执照
     */
    private String busiLisc;
    /**
     * @Fields taxCertId 税务证明
     */
    private String taxCertId;
    /**
     * @Fields webNm 网站名称
     */
    private String webNm;
    /**
     * @Fields webUrl 网站地址
     */
    private String webUrl;
    /**
     * @Fields merRegAddr 公司注册地址
     */
    private String merRegAddr;
    /**
     * @Fields merAddrLongitude 公司地址的所在经度
     */
    private BigDecimal merAddrLongitude;
    /**
     * @Fields merAddrLatitude 公司地址的所在纬度
     */
    private BigDecimal merAddrLatitude;
    /**
     * @Fields mgtScp 经营范围
     */
    private String mgtScp;
    /**
     * @Fields needInvFlg 是否开具发票 Y：需要；N：不需要；
     */
    private String needInvFlg;
    /**
     * @Fields invMod 开具发票方式 0 - 按季度开；1 - 按月开；2 –按年开；
     */
    private String invMod;
    /**
     * @Fields invTit 发票抬头
     */
    private String invTit;
    /**
     * @Fields invMailAddr 发票邮寄地址
     */
    private String invMailAddr;
    /**
     * @Fields invMailZip 发票邮寄邮编
     */
    private String invMailZip;
    /**
     * @Fields mercTrdCls 商户行业类别
     */
    private String mercTrdCls;
    /**
     * @Fields mercTrdDesc 商户行业描述
     */
    private String mercTrdDesc;
    /**
     * @Fields cprTyp 商户类别 01-国有，02-私有，03-外资，04-合资 08-个人，10-公司，11-个人独资
     */
    private String cprTyp;
    /**
     * @Fields csTelNo 商户客服电话
     */
    private String csTelNo;
    /**
     * @Fields mercHotLin 商户热线
     */
    private String mercHotLin;
    /**
     * @Fields cusMgr 客户经理编号
     */
    private String cusMgr;
    /**
     * @Fields cusMgrNm 客户经理名称
     */
    private String cusMgrNm;
    /**
     * @Fields rcvMagAmt 应收商户保证金
     */
    private BigDecimal rcvMagAmt;
    /**
     * displayNm 显示姓名
     */
    private String displayNm;

    /**
     * avatarPath 头像路径
     */
    private String avatarPath;
    
    @Override
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsrSts() {
        return usrSts;
    }
    public void setUsrSts(String usrSts) {
        this.usrSts = usrSts;
    }

    public String getUsrLvl() {
        return usrLvl;
    }

    public void setUsrLvl(String usrLvl) {
        this.usrLvl = usrLvl;
    }

    public String getIdChkFlg() {
        return idChkFlg;
    }
    public void setIdChkFlg(String idChkFlg) {
        this.idChkFlg = idChkFlg;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public String getIdNo() {
        return idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    public String getIdNoHid() {
        return idNoHid;
    }
    public void setIdNoHid(String idNoHid) {
        this.idNoHid = idNoHid;
    }
    public String getUsrNm() {
        return usrNm;
    }
    public void setUsrNm(String usrNm) {
        this.usrNm = usrNm;
    }
    public String getUsrNmHid() {
        return usrNmHid;
    }
    public void setUsrNmHid(String usrNmHid) {
        this.usrNmHid = usrNmHid;
    }
    public String getUsrGender() {
        return usrGender;
    }
    public void setUsrGender(String usrGender) {
        this.usrGender = usrGender;
    }
    public String getUsrNation() {
        return usrNation;
    }
    public void setUsrNation(String usrNation) {
        this.usrNation = usrNation;
    }
    public String getUsrBirthDt() {
        return usrBirthDt;
    }
    public void setUsrBirthDt(String usrBirthDt) {
        this.usrBirthDt = usrBirthDt;
    }
    public String getIssuAuth() {
        return issuAuth;
    }
    public void setIssuAuth(String issuAuth) {
        this.issuAuth = issuAuth;
    }
    public String getIdEffDt() {
        return idEffDt;
    }
    public void setIdEffDt(String idEffDt) {
        this.idEffDt = idEffDt;
    }
    public String getIdExpDt() {
        return idExpDt;
    }
    public void setIdExpDt(String idExpDt) {
        this.idExpDt = idExpDt;
    }
    public String getUsrRegCnl() {
        return usrRegCnl;
    }
    public void setUsrRegCnl(String usrRegCnl) {
        this.usrRegCnl = usrRegCnl;
    }
    public String getUsrRegIp() {
        return usrRegIp;
    }
    public void setUsrRegIp(String usrRegIp) {
        this.usrRegIp = usrRegIp;
    }
    public LocalDate getUsrRegDt() {
        return usrRegDt;
    }
    public void setUsrRegDt(LocalDate usrRegDt) {
        this.usrRegDt = usrRegDt;
    }
    public LocalTime getUsrRegTm() {
        return usrRegTm;
    }
    public void setUsrRegTm(LocalTime usrRegTm) {
        this.usrRegTm = usrRegTm;
    }
    public LocalDate getUsrClsDt() {
        return usrClsDt;
    }
    public void setUsrClsDt(LocalDate usrClsDt) {
        this.usrClsDt = usrClsDt;
    }
    public LocalTime getUsrClsTm() {
        return usrClsTm;
    }
    public void setUsrClsTm(LocalTime usrClsTm) {
        this.usrClsTm = usrClsTm;
    }
    public String getMercName() {
        return mercName;
    }
    public void setMercName(String mercName) {
        this.mercName = mercName;
    }
    public String getMercShortName() {
        return mercShortName;
    }
    public void setMercShortName(String mercShortName) {
        this.mercShortName = mercShortName;
    }
    public String getCprRegNmCn() {
        return cprRegNmCn;
    }
    public void setCprRegNmCn(String cprRegNmCn) {
        this.cprRegNmCn = cprRegNmCn;
    }
    public String getCprOperNmCn() {
        return cprOperNmCn;
    }
    public void setCprOperNmCn(String cprOperNmCn) {
        this.cprOperNmCn = cprOperNmCn;
    }
    public String getPrinNm() {
        return prinNm;
    }
    public void setPrinNm(String prinNm) {
        this.prinNm = prinNm;
    }
    public String getComercReg() {
        return comercReg;
    }
    public void setComercReg(String comercReg) {
        this.comercReg = comercReg;
    }
    public String getSocialCrdCd() {
        return socialCrdCd;
    }
    public void setSocialCrdCd(String socialCrdCd) {
        this.socialCrdCd = socialCrdCd;
    }
    public String getOrgCd() {
        return orgCd;
    }
    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }
    public String getBusiLisc() {
        return busiLisc;
    }
    public void setBusiLisc(String busiLisc) {
        this.busiLisc = busiLisc;
    }
    public String getTaxCertId() {
        return taxCertId;
    }
    public void setTaxCertId(String taxCertId) {
        this.taxCertId = taxCertId;
    }
    public String getWebNm() {
        return webNm;
    }
    public void setWebNm(String webNm) {
        this.webNm = webNm;
    }
    public String getWebUrl() {
        return webUrl;
    }
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    public String getMerRegAddr() {
        return merRegAddr;
    }
    public void setMerRegAddr(String merRegAddr) {
        this.merRegAddr = merRegAddr;
    }
    public BigDecimal getMerAddrLongitude() {
        return merAddrLongitude;
    }
    public void setMerAddrLongitude(BigDecimal merAddrLongitude) {
        this.merAddrLongitude = merAddrLongitude;
    }
    public BigDecimal getMerAddrLatitude() {
        return merAddrLatitude;
    }
    public void setMerAddrLatitude(BigDecimal merAddrLatitude) {
        this.merAddrLatitude = merAddrLatitude;
    }
    public String getMgtScp() {
        return mgtScp;
    }
    public void setMgtScp(String mgtScp) {
        this.mgtScp = mgtScp;
    }
    public String getNeedInvFlg() {
        return needInvFlg;
    }
    public void setNeedInvFlg(String needInvFlg) {
        this.needInvFlg = needInvFlg;
    }
    public String getInvMod() {
        return invMod;
    }
    public void setInvMod(String invMod) {
        this.invMod = invMod;
    }
    public String getInvTit() {
        return invTit;
    }
    public void setInvTit(String invTit) {
        this.invTit = invTit;
    }
    public String getInvMailAddr() {
        return invMailAddr;
    }
    public void setInvMailAddr(String invMailAddr) {
        this.invMailAddr = invMailAddr;
    }
    public String getInvMailZip() {
        return invMailZip;
    }
    public void setInvMailZip(String invMailZip) {
        this.invMailZip = invMailZip;
    }
    public String getMercTrdCls() {
        return mercTrdCls;
    }
    public void setMercTrdCls(String mercTrdCls) {
        this.mercTrdCls = mercTrdCls;
    }
    public String getMercTrdDesc() {
        return mercTrdDesc;
    }
    public void setMercTrdDesc(String mercTrdDesc) {
        this.mercTrdDesc = mercTrdDesc;
    }
    public String getCprTyp() {
        return cprTyp;
    }
    public void setCprTyp(String cprTyp) {
        this.cprTyp = cprTyp;
    }
    public String getCsTelNo() {
        return csTelNo;
    }
    public void setCsTelNo(String csTelNo) {
        this.csTelNo = csTelNo;
    }
    public String getMercHotLin() {
        return mercHotLin;
    }
    public void setMercHotLin(String mercHotLin) {
        this.mercHotLin = mercHotLin;
    }
    public String getCusMgr() {
        return cusMgr;
    }
    public void setCusMgr(String cusMgr) {
        this.cusMgr = cusMgr;
    }
    public String getCusMgrNm() {
        return cusMgrNm;
    }
    public void setCusMgrNm(String cusMgrNm) {
        this.cusMgrNm = cusMgrNm;
    }
    public BigDecimal getRcvMagAmt() {
        return rcvMagAmt;
    }
    public void setRcvMagAmt(BigDecimal rcvMagAmt) {
        this.rcvMagAmt = rcvMagAmt;
    }
    public String getMblNo() {
        return mblNo;
    }
    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }
    public String getDisplayNm() {
        return displayNm;
    }
    public void setDisplayNm(String displayNm) {
        this.displayNm = displayNm;
    }
    public String getAvatarPath() {
        return avatarPath;
    }
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
    
}
