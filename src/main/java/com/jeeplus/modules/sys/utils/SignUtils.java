package com.jeeplus.modules.sys.utils;

	import java.lang.reflect.Field;
	import java.util.Arrays;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import org.apache.commons.codec.digest.DigestUtils;
	import org.dom4j.Document;
	import org.dom4j.DocumentException;
	import org.dom4j.DocumentHelper;
	import org.dom4j.Element;

	/**
	 * 
	 * 微信支付签名算法,obj是待签名对象，是一个model类,或map,注意不要带sign
	 *  
	 * @author  沈乐鹏
	 * @version  [V1.00, 2015年6月28日]
	 * @see  [相关类/方法]
	 * @since V1.00
	 */
	public class SignUtils
	{
	    public static String genereateSign(Object obj, String apiKey)
	    {
	        StringBuffer sbf = new StringBuffer();
	        
	        if (obj instanceof Map)
	        {
	            // 传过来的是一个map
	            Map map = (Map)obj;
	            Object[] keyNames = map.keySet().toArray();
	            Arrays.sort(keyNames);
	            for (Object keyName : keyNames)
	            {
	                Object val = map.get(keyName);
	                if (val != null && !"".equals(val))
	                {
	                    sbf.append("&" + keyName + "=" + val);
	                }
	            }
	        }
	        else
	        {
	            Class clazz = obj.getClass();
	            Field[] fields = clazz.getDeclaredFields();
	            String[] fieldNames = new String[fields.length];
	            for (int i = 0; i < fields.length; i++)
	            {
	                fieldNames[i] = fields[i].getName();
	            }
	            Arrays.sort(fieldNames);
	            for (String fieldName : fieldNames)
	            {
	                try
	                {
	                    Field f = clazz.getDeclaredField(fieldName);
	                    f.setAccessible(true);
	                    Object val = f.get(obj);
	                    if (val != null && !"".equals(val))
	                    {
	                        sbf.append("&" + fieldName + "=" + val);
	                    }
	                }
	                catch (IllegalArgumentException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (SecurityException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (IllegalAccessException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (NoSuchFieldException e)
	                {
	                    e.printStackTrace();
	                }
	                
	            }
	        }
	        String stringA = sbf.toString().substring(1);
	        System.out.println("SignUtils 93 stringA:" + stringA);
	        String stringSignTemp = stringA + "&key=" + apiKey;
	        System.out.println("SignUtils 95 stringSignTemp:" + stringSignTemp);
	        String signValue = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
	        System.out.println("SignUtils 97 签名是:" + signValue);
	        return signValue;
	    }
	    
	    /**
	     * 
	     * @param obj
	     * @return
	     * @see [类、类#方法、类#成员]
	     */
	    public static String genereateXML(Object obj)
	    {
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("<xml>");
	        
	        if (obj instanceof Map)
	        {
	            // 传过来的是一个map
	            Map map = (Map)obj;
	            Object[] keyNames = map.keySet().toArray();
	            Arrays.sort(keyNames);
	            for (Object keyName : keyNames)
	            {
	                Object val = map.get(keyName);
	                if (val != null && !"".equals(val))
	                {
	                    sbf.append("<" + keyName + ">" + val +"</" + keyName + ">");
	                }
	            }
	        }
	        else
	        {
	            Class clazz = obj.getClass();
	            Field[] fields = clazz.getDeclaredFields();
	            String[] fieldNames = new String[fields.length];
	            for (int i = 0; i < fields.length; i++)
	            {
	                fieldNames[i] = fields[i].getName();
	            }
	            Arrays.sort(fieldNames);
	            for (String fieldName : fieldNames)
	            {
	                try
	                {
	                    Field f = clazz.getDeclaredField(fieldName);
	                    f.setAccessible(true);
	                    Object val = f.get(obj);
	                    if (val != null && !"".equals(val))
	                    {
	                        sbf.append("<" + fieldName + ">" + val +"</" + fieldName + ">");
	                    }
	                }
	                catch (IllegalArgumentException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (SecurityException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (IllegalAccessException e)
	                {
	                    e.printStackTrace();
	                }
	                catch (NoSuchFieldException e)
	                {
	                    e.printStackTrace();
	                }
	                
	            }
	        }
	        
	        sbf.append("</xml>");
	        
	        return sbf.toString();
	    }
	    
	    
	    
	    /**
	     * 校验xml的签名是否正确，注意：请在result_code和return_code都为SUCCESS的情况下调用
	     * Description: <br>
	     * 
	     * @param xml
	     * @return
	     * @throws DocumentException
	     * @see
	     */
	    public static boolean checkSign(String xml, String apiKey)
	        throws DocumentException
	    {
	        HashMap<String, String> xmlMap = new HashMap<String, String>();
	        Document doc = DocumentHelper.parseText(xml);
	        Element rootElement = doc.getRootElement();
	        List<Element> elements = rootElement.elements();
	        String xmlSign = null;
	        for (Element e : elements)
	        {
	            if ("sign".equals(e.getName()))
	            {
	                xmlSign = e.getText();
	                continue;
	            }
	            xmlMap.put(e.getName(), e.getText());
	        }
	        if (xmlSign == null) {
	        		// xml未包含sign默认成功
	        		return true;
	        }
	        String gSign = genereateSign(xmlMap, apiKey);
	        return gSign.equals(xmlSign);
	    }
	    
}
