
package com.jeeplus.modules.weixin.entity;

/**
 * 微信统一下单接口请求参数model类
 * 
 */
public class CouponDetailRequest
{

    //微信分配的公众账号ID
    private String appid;

    //微信支付分配的商户号
    private String mch_id;

    //随机字符串，不长于32位
    private String nonce_str;

    //签名
    private String sign;

    //用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    private String openid;

    //代金券id
    private String coupon_id;
    
    //批次id
    private String stock_id;

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getMch_id()
    {
        return mch_id;
    }

    public void setMch_id(String mch_id)
    {
        this.mch_id = mch_id;
    }

    public String getNonce_str()
    {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str)
    {
        this.nonce_str = nonce_str;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }
    
    public String getCoupon_id()
    {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id)
    {
        this.coupon_id = coupon_id;
    }
    
    public String getStock_id()
    {
        return stock_id;
    }

    public void setStock_id(String stock_id)
    {
        this.stock_id = stock_id;
    }
}