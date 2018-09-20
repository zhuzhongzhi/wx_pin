package cn.com.WebXml;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class TestWeather {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeatherWebServiceLocator locator = new WeatherWebServiceLocator();
		WeatherWebServiceSoap12Stub service = null;
		try {
			service = (WeatherWebServiceSoap12Stub) locator.getPort(WeatherWebServiceSoapStub.class);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			invokeGetSupportProvince(service);
			System.out.println("...................");
			invokeGetSupportCity(service);
			invokeGetWeatherByOneCity(service);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// 调用获取支持的省份、州接口
	public static void invokeGetSupportProvince(WeatherWebServiceSoap12Stub service) throws RemoteException {
		String[] provices = service.getSupportProvince();
		System.out.println("总共" + provices.length + "个");
		int count = 0;
		for (String str : provices) {
			if (0 != count && count % 5 == 0) {
				System.out.println();
			}
			System.out.print(str + "\t");
			count++;
		}
	}
	
	// 调用获取支持查询某个省份内的城市接口
	public static void invokeGetSupportCity(WeatherWebServiceSoap12Stub service)throws RemoteException {
		String provinceName = "江苏";
		String[] cities = service.getSupportCity(provinceName);
		System.out.println("总共" + cities.length + "个市");
		for (int i = 0; i < cities.length; i++) {
			if (0 != i && i % 5 == 0) {
				System.out.println();
			}
			System.out.print(cities[i] + "\t");
		}
	}
	
	// 调用查询某个城市天气的接口
	public static void invokeGetWeatherByOneCity(WeatherWebServiceSoap12Stub service) throws RemoteException {
		String cityName = "南京";
		String[] weatherInfo = service.getWeatherbyCityName(cityName);
		for (String str : weatherInfo) {
			System.out.println(str);
		}
	}
}
