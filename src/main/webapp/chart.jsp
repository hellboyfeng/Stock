<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="app.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>ECharts</title>
</head>

<body>
<!--Step:1 Prepare a dom for ECharts which (must) has size (width & hight)-->
<!--Step:1 为ECharts准备一个具备大小（宽高）的Dom-->
<div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>

<!--Step:2 Import echarts.js-->
<!--Step:2 引入echarts.js-->
<script src="${ctx}/js/echarts.js"></script>
<script src="${ctx}/js/jquery-2.1.1.js"></script>

<script type="text/javascript">

	$(function(){
		//dataX();
	})
	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r != null) return unescape(r[2]); return null; //返回参数值
	}

	function dataX(){
		$.ajax(
				{
					url:"/getx?num="+getUrlParam("num"),
					dataType:"text",
					success:function(data)
					{
						var rows=eval(data);
						//调用函数获取值，转换成数组模式
						for(var i=0;i<rows.length;i++)
						{
							arrX.push(rows[i].time);
						}
						return arrX;
					}
				});
	}

	// Step:3 conifg ECharts's path, link to echarts.js from current page.
	// Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
	require.config({
		paths: {
			echarts: './js'
		}
	});

	// Step:4 require echarts and use it in the callback.
	// Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
	require(
			[
				'echarts',
				'echarts/chart/bar',
				'echarts/chart/line',
				'echarts/chart/map'
			],
			function (ec) {
				//--- 折柱 ---
				var myChart = ec.init(document.getElementById('main'));
				myChart.setOption({
					tooltip : {
						trigger: 'axis'
					},
					legend: {
						data:['邮件营销']
					},
					toolbox: {
						show : true,
						feature : {
							mark : {show: true},
							dataView : {show: true, readOnly: false},
							magicType : {show: true, type: ['line']},
							restore : {show: true},
							saveAsImage : {show: true}
						}
					},
					calculable : true,
					xAxis : [
						{
							type : 'category',
							boundaryGap : false,
							data : (function(){
								var arrX=[];
								dataX();
								alert(arrX);
								return arrX;
							})
						}
					],
					yAxis : [
						{
							type : 'value'
						}
					],
					series : [
						{
							name:'邮件营销',
							type:'line',
							stack: '总量',
							data:[120, 132, 101]
						}
					]
				});

			}
	);
</script>
</body>
</html>