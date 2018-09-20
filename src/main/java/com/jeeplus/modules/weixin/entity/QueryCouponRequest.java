
package com.jeeplus.modules.weixin.entity;

/**
 * 微信统一下单接口请求参数model类
 * 
 */
public class QueryCouponRequest
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

    // 操作员
    private String op_user_id;

    // 设备号
    private String device_info;

    // 协议版本
    private String version;

    // 协议类型
    private String type;

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

    public String getOp_user_id()
    {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id)
    {
        this.op_user_id = op_user_id;
    }

    public String getDevice_info()
    {
        return device_info;
    }

    public void setDevice_info(String device_info)
    {
        this.device_info = device_info;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}