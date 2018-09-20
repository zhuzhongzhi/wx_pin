/**
 * GetWeatherbyCityName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.WebXml;

public class GetWeatherbyCityName  implements java.io.Serializable {
    private java.lang.String theCityName;

    public GetWeatherbyCityName() {
    }

    public GetWeatherbyCityName(
           java.lang.String theCityName) {
           this.theCityName = theCityName;
    }


    /**
     * Gets the theCityName value for this GetWeatherbyCityName.
     * 
     * @return theCityName
     */
    public java.lang.String getTheCityName() {
        return theCityName;
    }


    /**
     * Sets the theCityName value for this GetWeatherbyCityName.
     * 
     * @param theCityName
     */
    public void setTheCityName(java.lang.String theCityName) {
        this.theCityName = theCityName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWeatherbyCityName)) return false;
        GetWeatherbyCityName other = (GetWeatherbyCityName) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.theCityName==null && other.getTheCityName()==null) || 
             (this.theCityName!=null &&
              this.theCityName.equals(other.getTheCityName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTheCityName() != null) {
            _hashCode += getTheCityName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWeatherbyCityName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://WebXml.com.cn/", ">getWeatherbyCityName"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("theCityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://WebXml.com.cn/", "theCityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
