/**
 * GetWeatherbyCityNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.WebXml;

public class GetWeatherbyCityNameResponse  implements java.io.Serializable {
    private java.lang.String[] getWeatherbyCityNameResult;

    public GetWeatherbyCityNameResponse() {
    }

    public GetWeatherbyCityNameResponse(
           java.lang.String[] getWeatherbyCityNameResult) {
           this.getWeatherbyCityNameResult = getWeatherbyCityNameResult;
    }


    /**
     * Gets the getWeatherbyCityNameResult value for this GetWeatherbyCityNameResponse.
     * 
     * @return getWeatherbyCityNameResult
     */
    public java.lang.String[] getGetWeatherbyCityNameResult() {
        return getWeatherbyCityNameResult;
    }


    /**
     * Sets the getWeatherbyCityNameResult value for this GetWeatherbyCityNameResponse.
     * 
     * @param getWeatherbyCityNameResult
     */
    public void setGetWeatherbyCityNameResult(java.lang.String[] getWeatherbyCityNameResult) {
        this.getWeatherbyCityNameResult = getWeatherbyCityNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWeatherbyCityNameResponse)) return false;
        GetWeatherbyCityNameResponse other = (GetWeatherbyCityNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getWeatherbyCityNameResult==null && other.getGetWeatherbyCityNameResult()==null) || 
             (this.getWeatherbyCityNameResult!=null &&
              java.util.Arrays.equals(this.getWeatherbyCityNameResult, other.getGetWeatherbyCityNameResult())));
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
        if (getGetWeatherbyCityNameResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetWeatherbyCityNameResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetWeatherbyCityNameResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWeatherbyCityNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://WebXml.com.cn/", ">getWeatherbyCityNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWeatherbyCityNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://WebXml.com.cn/", "getWeatherbyCityNameResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://WebXml.com.cn/", "string"));
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
