package com.royglobal.gameplatform.domain.member.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Member {
    private int mbrNo;
    private int grpNo;
    private String mbrId;
    private String mbrNm;
    private String mbrNknm;
    private String mbrSwd;
    private String mbrSogm;
    private String mbrTypeCd;
    private int mbrLvl;
    private String mbrClrCd;
    private int upMbrNo;
    private String signupCd;
    private String curCd;
    private String excnBankNm;
    private String excnActNo;
    private String excnDpstrNm;
    private String excnPswd;
    private String phoneNo;
    private String signupDomain;
    private Date crtDt;
    private Date mdfdt;
    private Date delDt;
    private String psnMony;
    private int psnPoint;
    private String unrgstYn;
    private String delYn;
    private Date lastLginDt;
    private Date lastBetgDt;
    private String lastCntnIp;
    private String lastCntnDomain;
    private int lastGameNo;

}
