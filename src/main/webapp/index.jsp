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
	<link href="${ctx}/js/plugins/typeahead/typeahead.css" rel="stylesheet" />


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
	<script src="${ctx}/js/plugins/typeahead/handlebars.js"></script>
	<script src="${ctx}/js/plugins/typeahead/bloodhound.min.js"></script>
	<script src="${ctx}/js/plugins/typeahead/typeahead.bundle.min.js"></script>
	<script src="${ctx}/js/plugins/typeahead/typeahead.jquery.min.js"></script>


	<script>

	var path = "${ctx}";
	function openwin(url){
		var win = window.open(url, 'newwindow', 'height=500, width=800, top=200, left=300, titlebar=no,toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no,status=no');
	}

	//显示K线图
	function showk(th){
		var num = $(th).attr("id");
		var value = 'symbol='+num+'&amp;code=iddg64geja6fea4eafh9jbj7c5j4ie5d&amp;s=3';
		$("#kk").attr("value",value)
	}

	//显示图表
	function makeChart(num,data){
		$("#"+num).sparkline(data, {
			type: 'line',
			height:'50px',
			lineColor: '#17997f',
			fillColor: '#ffffff'});
	}

	$(function(){

		var jsonData = JSON.parse('${flows}');
		$.each(jsonData,function(i,index){
			$.ajax(
				{
					url:path+"/getchart?num="+index.num,
					dataType:"text",
					success:function(data)
					{
						var jData = JSON.parse(data);
						var bar=[];
						$.each(jData,function(t,tt){
							bar.push(tt.mainnetmount);
						})
						makeChart(index.num,bar)
					}
				});
		})



		var bestPictures = new Bloodhound({
			datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
			queryTokenizer: Bloodhound.tokenizers.whitespace,
			remote: {
				url: path+'/getstock?num=%QUERY',
				wildcard: '%QUERY'
			}
		});

		$('#num').typeahead(null, {
			name: 'best-pictures',
			display: 'name',
			limit: 66,
			highlight: true,
			source: bestPictures
		});

	})
	</script>

	<style>
		table.issue-tracker tbody tr td {
			vertical-align: middle;
			height: 50px;
		}
		.table {
			width: 100%;
			max-width: 100%;
			font-size: 85%;
			margin-bottom: 20px
		}

	</style>

</head>
<body>
<div class="col-lg-12">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
		</div>
		<div class="ibox-content">

			<div class="m-b-lg">

				<div class="input-group">
					<input type="text" placeholder="Search issue by name..." class=" form-control" id="num" style="width:600px;">
				</div>
			</div>


		<div class="ibox-content">

			<table  class="table table-hover issue-tracker"  width="60%">
			<thead>
				<tr>
					<th>名称</th>
					<th>代码</th>
					<th>涨幅(%)</th>
					<th>价格(元)</th>
					<th>主力净流入(万)</th>
					<th>图表</th>
					<th>K线图</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="flow" items="${moneyflows}">
				<tr>
					<td>${flow.name}</td>
					<td>${flow.num}</td>
					<td>${flow.changeratio}</td>
					<td>${flow.trade}</td>
					<td>${flow.mainnetmount} </td>
				    <td><span id="${flow.num}"></span></td>
					<td><a href="#modal" class="btn btn-sm btn-primary" id="${flow.num}" data-toggle="modal" onclick="showk(this)" > <i class="fa fa-pencil fa-fw"></i>查看K线图</a></td>
					<td><button class="btn btn-info  dim" value="查看图表" type="button" onclick="javascript:openwin('${ctx}/chart?num=${flow.num}')"></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>


		</div>
	</div>
</div>
<!-- #modal-alert -->
<div class="modal fade" id="modal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			</div>
			<div class="modal-body">
				<object type="application/x-shockwave-flash"  data="http://finance.sina.com.cn/flash/cn.swf?" width="560" height="490" id="flash" style="visibility: visible;"><param name="allowFullScreen" value="true"><param name="allowScriptAccess" value="always"><param name="wmode" value="transparent"><param name="flashvars" id="kk" value=""></object>
			</div>
			<div class="modal-footer">
				<a href="javascript:;" class="btn btn-sm btn-white" data-dismiss="modal">关闭</a>
			</div>
		</div>
	</div>
</div>
</body>
</html>
