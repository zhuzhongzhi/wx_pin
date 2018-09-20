/**
 * GetWeatherbyCityNamePro.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.WebXml;

public class GetWeatherbyCityNamePro  implements java.io.Serializable {
    private java.lang.String theCityName;

    private java.lang.String theUserID;

    public GetWeatherbyCityNamePro() {
    }

    public GetWeatherbyCityNamePro(
           java.lang.String theCityName,
           java.lang.String theUserID) {
           this.theCityName = theCityName;
           this.theUserID = theUserID;
    }


    /**
     * Gets the theCityName value for this GetWeatherbyCityNamePro.
     * 
     * @return theCityName
     */
    public java.lang.String getTheCityName() {
        return theCityName;
    }


    /**
     * Sets the theCityName value for this GetWeatherbyCityNamePro.
     * 
     * @param theCityName
     */
    public void setTheCityName(java.lang.String theCityName) {
        this.theCityName = theCityName;
    }


    /**
     * Gets the theUserID value for this GetWeatherbyCityNamePro.
     * 
     * @return theUserID
     */
    public java.lang.String getTheUserID() {
        return theUserID;
    }


    /**
     * Sets the theUserID value for this GetWeatherbyCityNamePro.
     * 
     * @param theUserID
     */
    public void setTheUserID(java.lang.String theUserID) {
        this.theUserID = theUserID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWeatherbyCityNamePro)) return false;
        GetWeatherbyCityNamePro other = (GetWeatherbyCityNamePro) obj;
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
              this.theCityName.equals(other.getTheCityName()))) &&
            ((this.theUserID==null && other.getTheUserID()==null) || 
             (this.theUserID!=null &&
              this.theUserID.equals(other.getTheUserID())));
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
        if (getTheUserID() != null) {
            _hashCode += getTheUserID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWeatherbyCityNamePro.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://WebXml.com.cn/", ">getWeatherbyCityNamePro"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("theCityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://WebXml.com.cn/", "theCityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("theUserID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://WebXml.com.cn/", "theUserID"));
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
