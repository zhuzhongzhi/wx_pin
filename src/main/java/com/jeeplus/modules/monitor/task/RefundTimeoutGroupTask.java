package com.jeeplus.modules.monitor.task;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.shiro.SecurityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.pin.entity.PinDeliverInfo;
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.entity.PinOrder;
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.service.PinDeliverInfoService;
import com.jeeplus.modules.pin.service.PinGroupService;
import com.jeeplus.modules.pin.service.PinOrderService;
import com.jeeplus.modules.pin.service.PinRefundService;

@DisallowConcurrentExecution
public class RefundTimeoutGroupTask extends Task {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    PinGroupService pinGroupService;
	
	@Autowired
    PinOrderService pinOrderService;
	
	@Autowired
    PinRefundService pinRefundService;
	
	@Autowired
	PinDeliverInfoService pinDeliverInfoService;
	
	@Autowired
	WeixinServiceUtil weixinServiceUtil;
	
	@Override
	public void run() {
		logger.info("RefundTimeoutGroupTask run begin===========================");
		
		/*String[] resource = new String[] {"/spring/spring-mvc.xml", "/spring/spring-context.xml", "/spring/spring-context-shiro.xml", "/spring/spring-context-activiti.xml"};  
        ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(resource);  
        org.apache.shiro.mgt.SecurityManager securityManager =   
            (org.apache.shiro.mgt.SecurityManager)appCtx.getBean("securityManager");  
        SecurityUtils.setSecurityManager(securityManager);*/
        
		ServletContext scontext = JobContext.getInstance().getContext();  
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(scontext);  
		
		org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager)ctx.getBean("securityManager");
		SecurityUtils.setSecurityManager(securityManager);
		
		pinGroupService = (PinGroupService)ctx.getBean("pinGroupService"); 
		pinOrderService = (PinOrderService)ctx.getBean("pinOrderService");
		pinRefundService = (PinRefundService)ctx.getBean("pinRefundService");
		pinDeliverInfoService = (PinDeliverInfoService)ctx.getBean("pinDeliverInfoService");
		weixinServiceUtil = (WeixinServiceUtil)ctx.getBean("weixinServiceUtil");
		
		// 查询所以过期的团
		PinGroup query = new PinGroup();
		query.setStatus("2");
		List<PinGroup> groups = pinGroupService.findList(query);
		for (PinGroup g:groups) {
			logger.info("RefundTimeoutGroupTask do refund for group:" + g);
			
			// 查询团所有订单
			PinOrder queryOrder = new PinOrder();
			queryOrder.setGroupId(g.getId());
			List<PinOrder> orderList = pinOrderService.findList(queryOrder);
			for (PinOrder o:orderList) {
				// 针对已经支付完成的订单进行退款
				if (o.getStatus().equals("1")) {
					PinRefund refund = new PinRefund();
					refund.setGroupId(g.getId());
					refund.setMemberId(o.getMemberId());
					refund.setOutTradeNo(o.getId()); //商户订单号
					refund.setTransactionId(o.getTradeNo());//微信订单号
					refund.setTotalFee(o.getFee()); //订单金额
					refund.setRefundFee(o.getFee()); //申请退款金额
					String outRefundNo = "refund" + o.getId(); //商户退款单号
					refund.setOutRefundNo(outRefundNo);
					refund.setRefundStatus("begin refund"); //退款状态
					
					// 保存退款信息
					pinRefundService.save(refund);
					
					// 进行退款处理
					Integer fee = Integer.valueOf(o.getFee());
					String res = weixinServiceUtil.refund(o.getId(), outRefundNo, fee, fee);
					logger.info("RefundTimeoutGroupTask refund result:" + res);
					// 订单状态设置为已经退款, 等待通知消息
					o.setStatus("4");
					pinOrderService.save(o);
				}
			}
			
			// 团状态设置为已经退款
			g.setStatus("3");
			pinGroupService.save(g);
			
			// 查询团所以递送信息
			PinDeliverInfo pinDeliverInfo = new PinDeliverInfo();
			pinDeliverInfo.setGroupId(g.getId());
			List<PinDeliverInfo> deliverList = pinDeliverInfoService.findList(pinDeliverInfo);
			if (deliverList != null) {
				for (PinDeliverInfo dv:deliverList) {
					dv.setStatus("3");
					pinDeliverInfoService.save(dv);
				}
			}
		}
		logger.info("RefundTimeoutGroupTask run end");
	}

}
