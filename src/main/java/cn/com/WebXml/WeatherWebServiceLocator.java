/**
 * WeatherWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.WebXml;

public class WeatherWebServiceLocator extends org.apache.axis.client.Service implements cn.com.WebXml.WeatherWebService {

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

    public WeatherWebServiceLocator() {
    }


    public WeatherWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WeatherWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WeatherWebServiceSoap12
    private java.lang.String WeatherWebServiceSoap12_address = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";

    public java.lang.String getWeatherWebServiceSoap12Address() {
        return WeatherWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WeatherWebServiceSoap12WSDDServiceName = "WeatherWebServiceSoap12";

    public java.lang.String getWeatherWebServiceSoap12WSDDServiceName() {
        return WeatherWebServiceSoap12WSDDServiceName;
    }

    public void setWeatherWebServiceSoap12WSDDServiceName(java.lang.String name) {
        WeatherWebServiceSoap12WSDDServiceName = name;
    }

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WeatherWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWeatherWebServiceSoap12(endpoint);
    }

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.com.WebXml.WeatherWebServiceSoap12Stub _stub = new cn.com.WebXml.WeatherWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getWeatherWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWeatherWebServiceSoap12EndpointAddress(java.lang.String address) {
        WeatherWebServiceSoap12_address = address;
    }


    // Use to get a proxy class for WeatherWebServiceSoap
    private java.lang.String WeatherWebServiceSoap_address = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";

    public java.lang.String getWeatherWebServiceSoapAddress() {
        return WeatherWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WeatherWebServiceSoapWSDDServiceName = "WeatherWebServiceSoap";

    public java.lang.String getWeatherWebServiceSoapWSDDServiceName() {
        return WeatherWebServiceSoapWSDDServiceName;
    }

    public void setWeatherWebServiceSoapWSDDServiceName(java.lang.String name) {
        WeatherWebServiceSoapWSDDServiceName = name;
    }

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WeatherWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWeatherWebServiceSoap(endpoint);
    }

    public cn.com.WebXml.WeatherWebServiceSoap getWeatherWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.com.WebXml.WeatherWebServiceSoapStub _stub = new cn.com.WebXml.WeatherWebServiceSoapStub(portAddress, this);
            _stub.setPortName(getWeatherWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWeatherWebServiceSoapEndpointAddress(java.lang.String address) {
        WeatherWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.com.WebXml.WeatherWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.com.WebXml.WeatherWebServiceSoap12Stub _stub = new cn.com.WebXml.WeatherWebServiceSoap12Stub(new java.net.URL(WeatherWebServiceSoap12_address), this);
                _stub.setPortName(getWeatherWebServiceSoap12WSDDServiceName());
                return _stub;
            }
            if (cn.com.WebXml.WeatherWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.com.WebXml.WeatherWebServiceSoapStub _stub = new cn.com.WebXml.WeatherWebServiceSoapStub(new java.net.URL(WeatherWebServiceSoap_address), this);
                _stub.setPortName(getWeatherWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WeatherWebServiceSoap12".equals(inputPortName)) {
            return getWeatherWebServiceSoap12();
        }
        else if ("WeatherWebServiceSoap".equals(inputPortName)) {
            return getWeatherWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://WebXml.com.cn/", "WeatherWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://WebXml.com.cn/", "WeatherWebServiceSoap12"));
            ports.add(new javax.xml.namespace.QName("http://WebXml.com.cn/", "WeatherWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WeatherWebServiceSoap12".equals(portName)) {
            setWeatherWebServiceSoap12EndpointAddress(address);
        }
        else 
if ("WeatherWebServiceSoap".equals(portName)) {
            setWeatherWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
