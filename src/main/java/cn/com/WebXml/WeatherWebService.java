/**
 * WeatherWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.WebXml;

public interface WeatherWebService extends javax.xml.rpc.Service {

/**
 * <a href="http://www.webxml.com.cn/" target="_blank">WebXml.com.cn</a>
 * <strong>天气预报 Web 服务，数据每2.5小时左右自动更新一次，准确可靠。包括 340 多个中国主要城市和 60 多个国外主要城市三日内的天气预报数据。</br>此天气预报Web
 * Services请不要用于任何商业目的，若有需要请<a href="http://www.webxml.com.cn/zh_cn/contact_us.aspx"
 * target="_blank">联系我们</a>，欢迎技术交流。 QQ：8409035<br />使用本站 WEB 服务请注明或链接本站：http://www.webxml.com.cn/
 * 感谢大家的支持</strong>！<br /><span style="color:#999999;">通知：天气预报 WEB 服务如原来使用地址
 * http://www.onhap.com/WebServices/WeatherWebService.asmx 的，请改成现在使用的服务地址
 * http://www.webxml.com.cn/WebServices/WeatherWebService.asmx ，重新引用即可。</span><br
 * /><br />&nbsp;
 */
    public java.lang.String getWeatherWebServiceSoap12Address();

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getWeatherWebServiceSoapAddress();

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
