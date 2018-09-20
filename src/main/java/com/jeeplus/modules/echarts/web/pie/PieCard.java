/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */

package com.jeeplus.modules.echarts.web.pie;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.LineData;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Funnel;
import com.github.abel533.echarts.series.Pie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 复杂的时间轴效果
 *
 * @author jeeplus
 */
@Controller
@RequestMapping(value = "${adminPath}/echarts/pie/card")
public class PieCard {

	@RequestMapping(value = { "index", "" })
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("dataURL", "/echarts/pie/card/option");
		return "modules/common/echarts";
	}

	@ResponseBody
	@RequestMapping("option")
	public GsonOption getOption() {
		GsonOption option = new GsonOption();
		// 时间轴
		option.timeline().data("2017-01-01", "2017-02-01", "2017-03-01", "2017-04-01", "2017-05-01",
				new LineData("2017-06-01", "emptyStart6", 8), "2017-07-01", "2017-08-01", "2017-09-01", "2017-10-01",
				"2017-11-01", new LineData("2017-12-01", "star6", 8));
		option.timeline().autoPlay(true);
		// timeline变态的地方在于多个Option
		Option basic = new Option();
		basic.title().text("卡券生命周期占比").subtext("按月度统计");
		basic.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
		basic.legend().data("已购买", "已领用", "已核销", "已退款");
		basic.toolbox().show(true).feature(Tool.mark, Tool.dataView, Tool.restore, Tool.saveAsImage,
				new MagicType(Magic.pie, Magic.funnel).option(new MagicType.Option()
						.funnel(new Funnel().x("25%").width("50%").funnelAlign(X.left).max(1548))));

		int idx = 1;
		Pie pie = getPie(idx++).center("50%", "45%").radius("50%");
		pie.label().normal().show(true).formatter("{b}{c}({d}%)");
		basic.series(pie);
		// 加入
		option.options(basic);
		// 构造11个数据
		Option[] os = new Option[11];
		for (int i = 0; i < os.length; i++) {
			os[i] = new Option().series(getPie(idx++));
		}
		option.options(os);
		return option;
	}

	/**
	 * 获取饼图数据
	 *
	 * @param idx
	 * @return
	 */
	public Pie getPie(int idx) {
		return new Pie().name("卡券状态(内测)").data(new PieData("已核销", idx * 128 + 80), new PieData("已退款", idx * 64 + 160),
				new PieData("已领取", idx * 32 + 320), new PieData("已购买", idx * 16 + 640));
	}
}
