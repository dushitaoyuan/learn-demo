package com.ncs.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author dushitaoyuan
 * @desc 系统编号工具类
 * @date 2019/8/19
 */
public class SystemCodeUtil {
    /**
     * prefix 编号前缀
     * paddingStr 16进制编码不够长时补充字符
     * dataLength 16进制数据最大长度
     * dateFormat 日期格式 默认 yyyymmdd
     * 系统编号生成工具类,规则前缀+日期字符+dataLength位16位进制 不够补paddingStr
     */
    private String prefix;
    private String fullPaddingStr;
    private Integer dataLength;
    private String dateFormat;
    private    Pattern systemCodePattern;

    private Integer codeFullLength;
    public SystemCodeUtil(String prefix, String paddingStr, String dateFormat, Integer dataLength) {
        this.prefix = prefix;
        this.fullPaddingStr = repeat(paddingStr,dataLength);
        this.dataLength = dataLength;
        this.dateFormat = dateFormat;
        //计算系统编号正则
        if(paddingStr.matches("[ABCDEF0-9]")){
            paddingStr="";
        }
        int dateLength=getDateFormat(null).length();
        this.systemCodePattern=Pattern.compile(prefix+"\\d{"+ dateLength+"}[ABCDEF0-9"+paddingStr+"]{"+dataLength+"}");
        //计算系统编号长度
        codeFullLength=prefix.length()+dateLength+dataLength;

    }

    public SystemCodeUtil(String prefix, Integer dataLength) {
        this(prefix,"0","yyyyMMddHHmmss",dataLength);
    }




    /**
     * @param date   日期
     * @param dataId 数据id
     * @return 系统编号
     */

    public String genSystemcode(Date date, Long dataId) {
        String dateStr = getDateFormat(date);
        //根据id号,生成n位16进制,不够补paddingStr
        String hexString = Long.toHexString(dataId);
        int len = hexString.length();
        String suffix = hexString;
        if (len != dataLength) {
            suffix = fullPaddingStr.substring(0, dataLength - len) + hexString;
        }
        return prefix + dateStr + suffix.toUpperCase();

    }


    public boolean isSystemCode(String systemCode) {
        if (null == systemCode || systemCode.isEmpty()) {
            return false;
        }
        if(systemCode.length()!=codeFullLength){
            return false;
        }
        return systemCodePattern.matcher(systemCode).matches();
    }

    private String getDateFormat(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    private String repeat(String str,Integer num){
        for (int i=0;i<num;i++){
            str+=str;
        }
        return str;
    }
    public Pattern getSystemCodePattern(){
        return  systemCodePattern;
    }
    public Integer getSystemCodeLength(){
        return  codeFullLength;
    }









}
