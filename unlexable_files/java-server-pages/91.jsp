<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../Meeting_files/hm.js"></script>

<meta property="og:image"
	content="http://www.huichangjia.com/templets/hcj/Picture/logo.jpg">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>会议场地</title>
<meta name="keywords" content="会议场地,会场出租,会议酒店,酒店会场,会议室预定,酒店会议室预定">
<meta name="description"
	content="会议场地预订，会场家是一家在线酒店会场预订平台，汇集全国各地酒店会议室，让企业在寻找会议酒店不再那么复杂，从而轻松就可以完成酒店会议室预定。">
<link rel="stylesheet" href="../Meeting_files/style.css">
<link rel="canonical" href="#">
<link rel="stylesheet" href="../Meeting_files/reset_v1.css">
<link rel="stylesheet" href="../Meeting_files/login.css">
<link rel="stylesheet" href="../Meeting_files/head2.css">
<script charset="utf-8" src="../Meeting_files/lxb.js"></script>
<script charset="utf-8" src="../Meeting_files/s.js"></script>
<script charset="utf-8" src="../Meeting_files/lxb.js"></script>
<script charset="utf-8" src="../Meeting_files/s.js"></script>
<script charset="utf-8" src="../Meeting_files/v.js"></script>
<script type="text/javascript"
	src="../Meeting_files/jquery-1.8.2.min.js"></script>

<script type="text/javascript">
	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i
			.test(navigator.userAgent)) {
		window.location = '#';
	}
	
	/* window.onload=function(){
		var tel="${info }";
		if (tel==2){
			alert("抱歉！每人一次只能预约一次。")
		}
	}; */
	
	function del(){ 
		  if(!confirm("确认要删除？")){ 
		  window.event.returnValue = false; 
		  } 
		  };
</script>

<script type="text/javascript">
	var ct = 'hengyang';
	var imgUrl = [];
	var imgLink = [];
	var bgcolor = [];
	var imgUrl2 = [];
	imgUrl[0] = "";
	imgUrl[1] = "";
	imgUrl2[0] = "";
	imgUrl2[1] = "";
	imgLink[0] = "javascript:;";
	imgLink[1] = "javascript:;";

	$(function() {
		$(document.body).removeClass('channel-page');
	})
</script>
<style type="text/css">
.publish-demand:hover {
	color: #fff;
}
</style>


<script type="application/javascript">
	
	function GetQueryString(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	
	function showxuqiu(){
		var xuqiu = GetQueryString('xuqiu');
		if(null!=xuqiu && xuqiu == 1){
			$('#faxuqiu').attr('data-on','false');
			$('#faxuqiu dt').addClass('on');
			$('#faxuqiu .icon-r-arrow').hide();
			$('#faxuqiu dd').show();
			
		}
	}
	showxuqiu();
	

</script>
<script type="text/javascript" src="../Meeting_files/api"></script>
<script type="text/javascript" src="../Meeting_files/getscript"></script>
<link rel="stylesheet" href="../Meeting_files/news.css">
<link rel="stylesheet" href="../Meeting_files/zzsc.css">
<script src="../Meeting_files/news.js"></script>
<script src="../Meeting_files/layer.js"></script>
<link rel="stylesheet" href="../Meeting_files/layer.css"
	id="layui_layer_skinlayercss">


<link type="text/css" rel="stylesheet"
	href="../Meeting_files/laydate.css">
<link type="text/css" rel="stylesheet"
	href="../Meeting_files/laydate(1).css" id="LayDateSkin">
<script src="../Meeting_files/share.js"></script>
<link rel="stylesheet" href="../Meeting_files/share_style0_16.css">
</head>

<body class="">




	<script type="text/javascript">
		$(function() {
			//				场地供应商入驻 == 修改新增
			$(".joinUs").hover(function() {
				$(".joinUs").css({
					"border" : "1px solid #e5e5e5",
					"border-top" : "none",
					"height" : "52px"
				});
				$(".cdJoin").css({
					"height" : "auto"
				});
				$(this).prev(".line").hide();
				$(this).next(".line").hide()
			}, function() {
				$(".joinUs").css({
					"border" : "none",
					"height" : "24px"
				});
				$(".cdJoin").css({
					"height" : "24px"
				})
				$(this).prev(".line").show();
				$(this).next(".line").show()
			});
			$("#submit").click(
					function() {
						$val = $("#mainName").val();

						document.location.href = "/venue/22441-0-0-0-0-" + $val
								+ ".html"
					});

			$('#mainName').keydown(
					function(e) {
						if (e.keyCode == 13) {
							$val = $("#mainName").val();

							document.location.href = "/venue/22441-0-0-0-0-"
									+ $val + ".html";
							return false;

						}
					});
		})
	</script>


	<div class="hd clearfix">

		<div class="headBalaner hide">
			<a href="javascript:"><img src="../Meeting_files/headbanner.jpg"></a>
		</div>
		<div class="container clearfix">

			<a class="site-logo" href="#" title="会场家"><img
				src="../Meeting_files/logo.png" alt="会场家"></a>
			<div class="change-city">
				<p class="blk">
					<span class="city">合肥</span> <em></em>
				</p>
				<p class="tip">一站式服务</p>
				<p class="tip hide">专业顾问</p>
				<p class="tip hide">海量会场</p>
				<p class="tip hide">优质套餐</p>
				<ul class="select-pop-city">
					<li><a href="#">广州</a></li>
					<li><a href="#">深圳</a></li>
					<li><a href="#">北京</a></li>
					<li><a href="#">上海</a></li>
					<li><a href="#">武汉</a></li>
					<li><a href="#">成都</a></li>
					<li><a href="#">杭州</a></li>
					<li><a href="#">天津</a></li>
					<li><a href="#">西安</a></li>
					<li><a href="#">南京</a></li>
					<li><a href="#">重庆</a></li>
					<li><a href="#">苏州</a></li>
					<li><a href="#">青岛</a></li>
					<li><a href="#">厦门</a></li>
					<li><a href="#">三亚</a></li>
					<li><a href="#">昆明</a></li>
				</ul>
			</div>
			<div class="link-publish">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
					href="login.jsp">登录</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/logOut">退出</a>
			</div>
			<div class="search">
				<form action="${pageContext.request.contextPath }/meeting/Search" method="post">
					<!-- <input type="text"  name="search"
						maxlength="64"  value=""
						placeholder="搜索城市">
						 <input type="submit"
						value="搜索" class="submit"> -->
						<input type="text"  name="search" style="height: 34px;"
						maxlength="64"  value=""
						placeholder="搜索城市">
						 <input type="submit"
						value="搜索" class="submit">
					<div class="search-result hide" id="focus-result">
						<dl class="clearfix">
							<dt>热门场地</dt>
							<dd>
								<a href="#">四星级酒店</a>
							</dd>
							<dd>
								<a href="#">三星级酒店</a>
							</dd>
							<dd>
								<a href="#">餐厅</a>
							</dd>
							<dd>
								<a href="#">咖啡厅</a>
							</dd>
							<dd>
								<a href="#">度假村</a>
							</dd>
							<dd>
								<a href="#">会议中心</a>
							</dd>
						</dl>
					</div>
					<div id="search-result" class="search-result hide"></div>
					<div class="search-tip">
						<a href="#">五星级酒店</a> <a href="#">会议场地</a> <a href="#">酒店会议室</a>
						<a href="#">艺术中心</a> <a href="#">培训场地</a> <a href="#">会所</a> <a
							href="#">展馆</a> <a href="#">小区社区</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="nav clearfix isMain">
		<div class="nav-in container clearfix">

			<ul class="menus">
				<li class="first">会场家为您服务</li>
				<li id="main"><a href="../index.jsp">首页</a></li>
				<%-- <li class="drop"><a
					href="${pageContext.request.contextPath}/meeting/example"> 热门会场<i
						class="hotNews1"></i>
				</a></li> --%>
				<li class="w65"><a href="${pageContext.request.contextPath}/meeting/adminUser">用户管理</a></li>
				<li class="w65"><a
					href="${pageContext.request.contextPath}/meeting/adminRoom">会议室<i class="hotNews"></i></a></li>
						
				<li><a href="${pageContext.request.contextPath}/meeting/admin">管理员</a></li>
				<li class="last">服务热线：<img src="../Meeting_files/phone400.jpg"
					alt=""></li>
			</ul>
		</div>
		<div class="line-top hide"></div>
	</div>


	<!-- 首屏 -->
	<!-- slider begin -->
	<script type="text/javascript" src="../Meeting_files/moment.min.js"></script>
	<script type="text/javascript"
		src="../Meeting_files/fullcalendar.min.js"></script>
	<script type="text/javascript" src="../Meeting_files/zh-cn.js"></script>
	<!-- <div class="feature-focus" id="feature-focus"> -->
		<div style="width:80%;margin: 0 auto;">
			<c:if test="${empty requestScope.lists }">
				<h2>没有任何会议室信息！</h2>
			</c:if>
			<c:if test="${!empty requestScope.lists }">
				<table border="1" cellpadding="5" align="center" width="100%">

					<tr>
						<th>地点</th>
						<th>图片</th>
						<th>类型</th>
						<th>可用时间</th>
						<th>预算(￥)</th>
						<th>容纳人数</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>备注</th>
						<th>操作</th>
						
					</tr>
					<c:forEach items="${requestScope.lists }" var="emp">
						<tr>
							<td>${emp.city }</td>
							<td><c:if test="${emp.pic!=null}">
									<a href="#"><img src="/image/${emp.pic}" width="60" height="40" /></a>
								</c:if></td>
							<td>${emp.type }</td>
							<td>${emp.time}</td>
							<td>${emp.money }</td>
							<td>${emp.num }</td>
							<td>${emp.state }</td>
							<td>${emp.date}</td>
							<td>${emp.info }</td>
							 
							    
            <td><a href="${pageContext.request.contextPath }/meeting/adminUpdate/${emp.id }">编辑</a></td>
             <td><a href="${pageContext.request.contextPath }/meeting/adminDelete/${emp.id }" onclick="return del()">删除</a></td>
           
						</tr>
                       <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>

					</c:forEach>

				</table>
				

			</c:if>





		</div>
	<!-- </div> -->
	<!-- slider end -->

	<div class="container">




		<br>


		<!-- 广播台 begin -->


		<!-- 广播台 end -->

	</div>

	<!--分类浏览 -->

	<!-- 分类浏览 begin 场地类型-->
	<style>
.head_ico li {
	float: left;
	margin-right: 60px;
}

.head_ico li im {
	float: left;
}

.head_ico li div {
	margin-top: 10px;
	float: right;
}
</style>

	<!-- 分类浏览 end -->
	<div style="clear: both;"></div>
	<!-- 友情链接 begin -->

	<script>
		$(function() {
			$('.more').on(
					'click',
					function() {
						$(this).parent().parent().find('.city-list')
								.removeClass('hide');
						$(this).parent().remove();
					});
		});
	</script>
	<!-- 友情链接 end -->

	<script type="text/javascript" src="../Meeting_files/laydate.js"></script>

	<!-- <script type="text/javascript" src="http://www.huichangjia.com/templets/hcj/Scripts/slider.js"></script> -->
	<script src="../Meeting_files/home.js"></script>
	<script type="text/javascript" src="../Meeting_files/index.js"></script>
	<script type="text/javascript"
		src="../Meeting_files/jquery.sochange.js"></script>
	<script type="text/javascript">
		function loadImg() {
			$('.feature-focus .a_bigImg').soChange({
				thumbObj : '.ul_change li span',
				thumbNowClass : 'on',
				btnPrev : '.feature-focus .a_last',
				btnNext : '.feature-focus .a_next',
				changeTime : 3000
			});

		}
		$(function() {

			$('.nav').addClass('isMain');
			$('.line-top').addClass('hide');
		});
	</script>


	<script type="text/javascript" src="../Meeting_files/base64.js"></script>
	<script type="text/javascript" src="../Meeting_files/jquery.cookie.js"></script>
	<script>
		$(function() {
			// 悬浮窗口
			$(".yb_conct").hover(function() {
				$(".yb_conct").css("right", "5px");
				$(".yb_bar .yb_ercode").css('height', '200px');
			}, function() {
				$(".yb_conct").css("right", "-127px");
				$(".yb_bar .yb_ercode").css('height', '53px');
			});

		});
	</script>
	<!-- footer begin -->
	<div class="thc-footer">
		<div class="container">
			<div class="footer-l fl">
				<p class="nav-link">
					<a rel="nofollow" href="#">关于我们</a> <span>|</span> <a
						rel="nofollow" href="#">联系方式</a> <span>|</span> <a rel="nofollow"
						href="#">服务条款</a> <span>|</span> <a rel="nofollow" href="#">隐私政策</a>

					<a href="#">百度地图</a> <span>|</span> <a href="#">谷歌地图</a>
				</p>

				<p class="text">
					<span>Copyright © 2002-2016 版权所有&nbsp;&nbsp;&nbsp;



						&nbsp;&nbsp; 
				</p>

			</div>





		</div>
	</div>




	<!--<div class="yb_conct">
  <div class="yb_bar">
    <ul>
      <li class="yb_phone">400-838-6118</li>
      <li class="yb_QQ">
      	<a  href="http://wpa.qq.com/msgrd?v=3&uin=1557373031&site=qq&menu=yes" title="即刻发送您的需求">在线咨询</a>
      </li>
      <li class="yb_ercode" style="height:53px;">微信二维码 <br>
        <img class="hd_qr" src="/templets/hcj/Picture/wx.jpg" width="125" alt="关注你附近"> </li>
    </ul>
  </div>
</div>-->

	<!-- 返回顶部 begin -->
	<div id="top-box">
		<a id="go-top" href="javascript:void(0)"></a>
	</div>
	<!-- 返回顶部 end -->

	<div id="tbox" style="right: 40px; bottom: 40px;">
		<a id="gotop" href="javascript:void(0)"></a>
	</div>

	<div id="dialog-overlay" class="dialog-overlay hide"></div>

	<script src="../Meeting_files/pop-dialog.js"></script>
	<script>
		var currentCitySp = "hengyang";
		var currentCity = "hengyang";
	</script>
	<div id="overlay-layer" class="overlay-layer hide"></div>
	<script type="text/javascript" src="../Meeting_files/commons.js"></script>
	<script type="text/javascript" src="../Meeting_files/jquery.md5.js"></script>
	<script charset="utf-8" src="../Meeting_files/wpa.js"></script>

	<script>
		var isLogin = "";
		$(function() {
			$('.btn-collection').on(
					'click',
					function() {
						if (isLogin) {
							$.post('/member/createCollection', 'businessId='
									+ $(this).data('id') + '&type='
									+ $(this).data('type'), function(result) {
								if (result.code == "0") {
									alert('成功');
									if ($('.nav').hasClass('hasCollection')) {
										location.reload();
									}
								}
							});
						} else {
							$('.nav').attr('data', $(this).attr('data'));
							$('.nav').addClass('hasCollection');
							$('.header').addClass('hasCollection');
							$('.login-pop-dialog').removeClass('hide');
							$('.order-pop-dialog').addClass('hide');
							$('[data-id=login-pop-dialog]').trigger('click');
						}
					});
			if (!$('.nav').hasClass('isMain')) {
				$('.nav .nav-side').remove();
			}
		});
	</script>
	<script type="text/javascript">
		$(function() {
			//输入框提示		
			$(".inputBox").click(function() {
				$(this).css("border-color", "red")
				$(this).find(".inp").focus();
				$(this).find(".inpTip").hide();
			});

			$(".inputBox .inp").blur(function() {
				$(this).parent(".inputBox").css("border-color", "#cfcfcf");
				if ($(this).val() == "") {
					$(this).prev(".inpTip").show();
				} else {
					$(this).prev(".inpTip").hide();
				}
			});

			//关闭弹窗
			$(".closePop").click(function() {
				$(".overlay-layer").addClass('hide');
				$(".loginPop").addClass('hide');
			});
		});
	</script>
	<script>
		var cityId = 22441;

		var currentCityName = "衡阳";
		var cityType = "hengyang";
		var venueType = "";
		var actType = "";
		var wzjt = ""
		var wzjtValue = "";
		var people = "";
		var peopleView = "";
		var area = "";
		var areaView = "";
		var storeType = "";
		var assorts = "";
		var features = "";
		var facilitys = "";
		var sortType = "";
		var sortValue = "";
		var fullShot = "";
		var isReserve = "";
	</script>

	<script type="text/javascript">
		var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
				: " http://");
		document
				.write(unescape("%3Cscript src='"
						+ _bdhmProtocol
						+ "hm.baidu.com/h.js%3F1a58c5afff225ed51a95254ea7227d8b' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script src="../Meeting_files/h.js" type="text/javascript"></script>


	<script src="../Meeting_files/venue-list.js"></script>
	<script type="text/javascript" src="../Meeting_files/pop-login.js"></script>
	<script type="text/javascript"
		src="../Meeting_files/jquery.validationengine-zh_cn.js"></script>
	<script type="text/javascript"
		src="../Meeting_files/jquery.validationengine.js"></script>
	<script src="../Meeting_files/datepicker_v1.js"></script>
	<script src="../Meeting_files/script_v1.js"></script>

</body>
</html>