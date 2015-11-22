<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="app.jsp" %>
<html>
<head>
<meta charset="utf-8" />
	<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
	<link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

	<link href="${ctx}/css/animate.css" rel="stylesheet">
	<link href="${ctx}/css/style.css" rel="stylesheet">

	<!-- Data Tables -->
	<link href="${ctx}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/dataTables/dataTables.tableTools.min.css" rel="stylesheet">



	<!-- Mainly scripts -->
	<script src="${ctx}/js/jquery-2.1.1.js"></script>
	<script src="${ctx}/js/bootstrap.min.js"></script>
	<script src="${ctx}/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="${ctx}/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="${ctx}/js/plugins/jeditable/jquery.jeditable.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="${ctx}/js/inspinia.js"></script>
	<script src="${ctx}/js/plugins/pace/pace.min.js"></script>

	<!-- Sparkline demo data -->
	<script src="${ctx}/js/demo/sparkline-demo.js"></script>

	<!-- Sparkline -->
	<script src="${ctx}/js/plugins/sparkline/jquery.sparkline.min.js"></script>


	<script>
	function openwin(url){
		var win = window.open(url, 'newwindow', 'height=500, width=800, top=200, left=300, titlebar=no,toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no,status=no');
	}

	function makeChart(num,data){
		$("#"+num).sparkline(data, {
			type: 'bar',
			barWidth: 8,
			height: '100px',
			barColor: '#1ab394',
			negBarColor: '#c6c6c6'});
	}

	$(function(){

		var dataM = 	'${flows}';
		var jsonData = JSON.parse(dataM);
		$.each(jsonData,function(i,index){
			$.ajax(
				{
					url:"/getchart?num="+index.num,
					dataType:"text",
					success:function(data)
					{
						var jData = JSON.parse(data);
						var bar=[];
						$.each(jData,function(t,tt){
							bar.push(tt.mainnetmount/10000);
						})
						makeChart(index.num,bar)
					}
				});
		})
	})
	</script>

</head>
<body>
<div class="col-lg-12">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<div class="ibox-tools">
				<a class="collapse-link">
					<i class="fa fa-chevron-up"></i>
				</a>
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="fa fa-wrench"></i>
				</a>
				<ul class="dropdown-menu dropdown-user">
					<li><a href="#">Config option 1</a>
					</li>
					<li><a href="#">Config option 2</a>
					</li>
				</ul>
				<a class="close-link">
					<i class="fa fa-times"></i>
				</a>
			</div>
		</div>
		<div class="ibox-content">

			<table class=" table-striped table-bordered table-hover " id="editable"  width="60%">
			<thead>
				<tr>
					<th>名称</th>
					<th>代码</th>
					<th>涨幅(%)</th>
					<th>价格(元)</th>
					<th>主力净流入(万)</th>
					<th>图表</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="flow" items="${moneyflows}">
				<tr>
					<td>${flow.name}</td>
					<td>${flow.num}</td>
					<td>${flow.changeratio}</td>
					<td class="gradeA">${flow.trade}</td>
					<td>${flow.mainnetmount}</td>
					<td><span id="${flow.num}" class="spk"></span>
					</td>
					<td><button class="btn btn-info  dim" type="button" onclick="javascript:openwin('${ctx}/chart?num=${flow.num}')"></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>


		</div>
	</div>
</div>
</body>
</html>
