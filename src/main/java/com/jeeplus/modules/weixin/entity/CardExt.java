
package com.jeeplus.modules.weixin.entity;

/**
 * 微信统一下单接口请求参数model类
 * 
 */
public class CardExt
{

    // 指定领取者的openid
    private String openid;

    //时间戳 单位为秒
    private String timestamp;

    //随机字符串，不长于32位
    private String nonce_str;

    //signature
    private String signature;
    
    //code
    private String code;
    
    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }
    
    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    public String getNonce_str()
    {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str)
    {
        this.nonce_str = nonce_str;
    }
    
    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}