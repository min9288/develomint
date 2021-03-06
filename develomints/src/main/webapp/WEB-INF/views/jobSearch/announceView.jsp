<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Develomint</title>
<link rel="shortcut icon" type="image/x-icon" href="/resources/img/favicon.ico"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<style>
	.contain{
		width: 1100px;
		margin: 0 auto;
		margin-top: 50px;
		margin-bottom: 50px;
	}
	.header{
		padding-left: 50px;
		padding-bottom: 40px;
		overflow: hidden;
		margin-top: 50px;
	}
	em{
		font-style: normal;
		font-weight: bold;
		font-size: 22px;
	}
	.logo{
		display: inline-block;
		margin-right: 30px;
	}
	.logo>img{
		width: 130px;
	}
	.announceTitle{
		margin-bottom: 0px;
		display: inline-block;
	}
	.announceTitle>p{
		margin-bottom: 0px;
		font-size: 25px;
		font-weight: bold;
		display: inline-block;
	}
	.companyName{
		color: #57595b;
		margin-left: 165px;
	}
	.applicationBtn{
		display: inline-block;
		float: right;
		margin-right: 50px;
	}
	.applicationBtn>button, .loginBtn{
		background-color: rgb(78, 205, 196);
		color: white;
		font-weight: bold;
		border-radius: 5px;
		width: 100px;
		text-align: center;
		padding: 15px 0px;
		border: none;
		outline: none;
	}
	.info{
		width: 1000px;
		margin: 0 auto;
		border: 1px solid #d9d9d9;
		padding: 50px;
	}
	.infoContent{
		margin-top: 20px;
	}
	.infoUl{
		list-style: none;
		margin-top: 25px;
		padding-left: 0px;
	}
	.infoUl>li{
		height: 30px;
	}
	.announceInfo{
		display: inline-block;
		width: 120px;
	}
	.infoDiv{
		margin-bottom: 80px;
	}
	.img>img{
		width: 18px;
		height: 21px;
	}
	.img{
		width: 25px;
		margin-right: 10px;
	}
	.comInfo{
		width: 800px;
		overflow: hidden;
		border: 1px solid #888;
		padding: 20px 10px;
		margin:0 auto;
	}
	.infoLogo{
		width: 260px;
		float: left;
		text-align: center;
		line-height: 100px;
	}
	.infoData{
		float: left;
		overflow: hidden;
	}
	.left{
		float: left;
		overflow: hidden;
		margin-right: 80px;
	}
	.left>div, .right>div{
		margin-bottom: 10px;
	}
	.right{
		float: left;
		overflow: hidden;
	}
	.text{
		float: left;
		margin-right: 18px;
		font-size: 14px;
	}
	.data{
		float: left;
		font-size: 14px;
	}
	.title{
		margin-bottom: 15px;
	}
	.title>a{
		font-size: 18px;
		text-decoration: none;
		color: #222222;
	}
	.announcement{
		margin-left: 60px;
		font-size: 20px;
		font-weight: bold;
		margin-top: 50px;
		margin-bottom: 10px;
	}
</style>
<body>
<script>
	$(function(){
		$("#noNormalMember").click(function(){
			alert("??????????????? ????????? ???????????????.");
		});
		
	});
	
		$(".companyEnrollBtn").click(function(){
			/* ?????? */
			var receiver = $(".receiver").eq(index).val();
			var dmContent = $(".dmContent").eq(index).val();
			var sender = $(".sender").eq(index).val();
			console.log(receiver);
			console.log(dmContent);
			console.log(sender);
			$.ajax({
				url : "/sendDm.do",
				data : {receiver:receiver,dmContent:dmContent,sender:sender},
				success : function(data) {
					if(data == 1){
						dmCount(receiver);
					}
				}
			});
			
		});
</script>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="contain">
		<div class="header">
			<div class="logo">
				<img alt="" src="/resources/upload/company/${a.filepath }">
			</div>
			<div class="announceTitle">
				<p>${a.announceTitle }</p>
			</div>
			<div class="applicationBtn">
				<c:choose>
					<c:when test="${empty sessionScope.m }">
						<a href="/loginFrm.do"><button class="loginBtn">????????????</button></a>
					</c:when>
					<c:when test="${sessionScope.m.memberType eq 3 or sessionScope.m.memberType eq 9 }">
						<button class="loginBtn" id="noNormalMember">????????????</button>
					</c:when>
					<c:otherwise>
						
						<c:if test="${ap eq 0 }">
							<button data-bs-toggle="modal" data-bs-target="#contestMember">????????????</button>						
						</c:if>
						<c:if test="${ap eq 1 }">
							<button class="btn btn-outline-info">????????????</button>
						</c:if>
						<!-- modal?????? ?????? -->
						<div class="modal fade" id="contestMember" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
								<div class="modal-dialog  modal-dialog-centered">
									<div class="modal-content">
										<div class="modal-body">
											<form action="/insertApplication.do" method="post">
												<p style="font-size: 15px; color: #444444; margin-bottom:5px;">${a.companyName } ????????????</p>
												<p style="font-size: 22px; font-weight: bold; text-overflow:ellipsis; margin-bottom: 0px;">${a.announceTitle }</p>
												<p style="font-size: 12px; color: #999999;">
													<c:if test="${a.career eq 1 }">
														??????
													</c:if> 
													<c:if test="${a.career eq 2 }">
														??????
													</c:if> 
													<c:if test="${a.career eq 3 }">
														??????/??????
													</c:if> 
													| 
													<c:if test="${a.school eq 1 }">
														????????????
													</c:if>
													<c:if test="${a.school eq 2 }">
														????????????
													</c:if>
													<c:if test="${a.school eq 3 }">
														????????????
													</c:if>
													<c:if test="${a.school eq 4 }">
														????????????
													</c:if>
													<c:if test="${a.school eq 5 }">
														????????????
													</c:if>
													|
													<c:if test="${a.workPlace eq 1 }">
														??????
													</c:if>
													<c:if test="${a.workPlace eq 2 }">		
								            			??????
									            	</c:if>
													<c:if test="${a.workPlace eq 3 }">		
									            		??????
									            	</c:if>
													<c:if test="${a.workPlace eq 4 }">		
														??????
									            	</c:if>
													<c:if test="${a.workPlace eq 5 }">		
									            		??????
									            	</c:if>
													<c:if test="${a.workPlace eq 6 }">		
									            		??????
									            	</c:if>
													<c:if test="${a.workPlace eq 7 }">		
														??????
									            	</c:if>
													<c:if test="${a.workPlace eq 8 }">		
														??????
									            	</c:if>
													<c:if test="${a.workPlace eq 9 }">		
														??????
									            	</c:if>
													<c:if test="${a.workPlace eq 10 }">		
									            		??????
									            	</c:if>
													<c:if test="${a.workPlace eq 11 }">		
									            		??????
									            	</c:if>
													<c:if test="${a.workPlace eq 12 }">		
									            		??????
									            	</c:if>	
												</p>
												<div class="selectResume" style="width: 466px; background-color: #f8f9fa; height: 60px; line-height: 60px; padding: 0px 30px; overflow: hidden; border-radius: 10px; position: relative; top: 20px; border: 1px solid #ebebeb;">
													<span style="font-size: 14px; color: #444;">????????? ?????????</span>
													<a href="/resumeManage.do?memberNo=${sessionScope.m.memberNo }&reqPage=1"><button data-v-6259d225="" fragment="103966058a9" type="button" class="btn" style="color: #444; float:right; font-size: 14px; width: 120px; height:40px; margin-top: 10px; background-color: white; border-radius: 10px;">????????? ?????? <svg data-v-6259d225="" viewBox="0 0 16 16" class="ic" style="height: 14px; margin-top: -3px;"><path d="M6 13.657L11.657 8 6 2.343" stroke="#444" fill="none" fill-rule="evenodd" stroke-linecap="round" stroke-linejoin="round"></path></svg></button></a>
												</div>
												<div style="border: 1px solid #d9d9d9; padding: 20px; padding-left: 25px; margin-bottom: 20px; border-top: none; border-radius: 10px; border: 1px solid #eaf0fa; padding-top: 35px; box-shadow: 0 2px 14px 0 rgb(0 0 0 / 10%);">
													<span style="font-size: 12px; color: #999999;">${r.writeDate }</span>
													<p><a href="/rView.do?resumeNo=${r.resumeNo }" target="_blank" style="font-weight: bold; text-decoration: none; color: black; text-overflow: ellipsis;">${r.resumeTitle }</a></p>
												</div>
												<p class="desc" style="font-size:12px; color: #999999;"> ??????????????? 90????????? ?????????????????? ???????????????.<br>????????? ????????? ???????????? [????????????] ????????? ????????? ?????????.<br> ???????????? ?????? ?????? ??????????????? ??????????????????.</p>
												<p class="desc" style="font-size:12px; color: #999999;"> ??????(?????? ?????????????????? 3???)??? ??? ??? ???????????? ?????? ??? ??????????????? ???????????????, ???????????? ??? ????????? ??? ??? ??????????????? ?????? ????????? ???????????? ??????????????? ?????? ?????? ??? ?????????????????? ????????? ??? ????????????.</p>
												<div style="text-align: right; ">
													<button type="submit" class="btn btn-primary companyEnrollBtn" style="width: 100px;">????????????</button>
													<button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="width: 100px;">??????</button>
												</div>
												<input type="hidden" name="announceNo" value="${a.announceNo }">
												<input type="hidden" name="resumeNo" value="${r.resumeNo }">
												<input type="hidden" name="memberNo" value="${sessionScope.m.memberNo }">
												<input type="hidden" name="status" value="1">
												
												 <!-- ?????? -->
												 <input type="hidden" class="receiver" value="${a.memberId }">
												 <input type="hidden" class="sender" value="admin">
												 <input type="hidden" class="dmContent" value="[????????????] ????????? ??????????????? ???????????? ????????????. ???????????? ?????????????????? ???????????? ????????? ???????????????.">
											</form>
										</div>
									</div>
								</div>
							</div>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="companyName">
				<small>${a.companyName }</small>
			</div>
		</div>
		<hr>
		<div class="announcement">????????????</div>
		<div class="info">
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ??????</em>
				<ul class="infoUl">
					<li>
						<span class="announceInfo">
							<i class="img">
								<img src="/resources/img/jobSearch/endDate.PNG">
							</i>
							?????????
						</span>
						<span>${a.endDate }</span>
					</li>
					<li>
						<span class="announceInfo">
							<i class="img">
								<img src="/resources/img/jobSearch/category.PNG">
							</i>
							 ??????
						</span>
						<span>
							<c:if test="${a.category eq 1}">
								IT ??????
							</c:if>
							<c:if test="${a.category eq 2}">
								??? ?????????
							</c:if>
							<c:if test="${a.category eq 3}">
								???????????? ??????
							</c:if>
						</span>
					</li>
					<li>
						<span class="announceInfo">
							<i class="img">
								<img src="/resources/img/jobSearch/career.PNG">
							</i> 
							??????
						</span>
						<span>
							<c:if test="${a.career eq 1 }">
								??????
							</c:if>
							<c:if test="${a.career eq 2 }">
								??????
							</c:if>
							<c:if test="${a.career eq 3 }">
								????????????
							</c:if>
						</span>
					</li>
					<li>
						<span class="announceInfo">
							<i class="img">
								<img src="/resources/img/jobSearch/workForm.PNG">
							</i> 
						 	????????????
						</span>
						<span>
							<c:if test="${a.workForm eq 1 }">
								?????????
							</c:if>
							<c:if test="${a.workForm eq 2 }">
								?????????
							</c:if>
							<c:if test="${a.workForm eq 3 }">
								???????????????
							</c:if>
							<c:if test="${a.workForm eq 4 }">
								??????
							</c:if>
							<c:if test="${a.workForm eq 5 }">
								????????????
							</c:if>
						</span>
					</li>
					<li>
						<span class="announceInfo">
							<i class="img">
								<img src="/resources/img/jobSearch/money.PNG">
							</i>
							 ??????
						</span>
						<span>
							<c:choose>
								<c:when test="${a.money eq 0 }">
									??????????????? ??????
								</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${a.money}" pattern="#,###"/> &#8361;
								</c:otherwise>
							</c:choose>
						</span>
					</li>
				</ul>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>	
				<div class="infoContent">
					<p>${a.content }</p>
				</div>
			
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>	
				<div class="infoContent">
					<p>${a.announceContentBr }</p>
				</div>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>	
				<div class="infoContent">
					<c:if test="${a.veterans eq 2 }">
						<p>???????????? ??????</p>
					</c:if>
					<c:if test="${a.military eq 1 }">
						<p>????????????</p>
					</c:if>
					<c:if test="${a.military eq 2 }">
						<p>????????????</p>
					</c:if>
				</div>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>		
				<div class="infoContent">
					<!-- <img src="/resources/img/company/saram.png"> -->
					<style>
						.reNum{
							list-style: none;
							overflow: hidden;
							text-align: center;
							margin-left: 20px; 
						}
						.reNum>li{
							float: left;
							text-align: center;
						}
						.r{
							padding: 0px 15px;
						}
						.reNum>li>p{
							margin-top: 10px;
							color: #3698c1;
							width: 136px;
						}
					</style>
					<ul class="reNum">
                            <li>
                                <img src="/resources/img/company/re_num01.png" alt=""> <img src="/resources/img/company/right.PNG" class="r">
                                <p>????????????</p>
                            </li>
                            <li>
                                <img src="/resources/img/company/re_num02.png" alt=""> <img src="/resources/img/company/right.PNG" class="r">
                                <p>1??? ????????????</p>
                            </li>
                            <li>
                                <img src="/resources/img/company/re_num03.png" alt=""> <img src="/resources/img/company/right.PNG" class="r">
                                <p>2??? ????????????</p>
                            </li>
                            <li>
                                <img src="/resources/img/company/re_num04.png" alt="">
                                <p>????????????</p>
                            </li>
                        </ul>
					
				</div>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ???????????? ??? ??????</em>	
				<div class="infoContent">
					<p style="margin-bottom: 13px;">??? ???????????? : <strong>${a.endDate }</strong> ??????</p>
					<p style="margin-bottom: 13px;">??? ????????????	: ??????????????? ????????????</p>
					<p style="margin-bottom: 13px;">??? ???????????????	:	??????????????? ????????? ?????????</p>
					
				</div>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>	
				<div class="infoContent">
					<p>??????????????? ????????? ??????????????? ????????? ??????, ???????????? ???????????? ????????? ????????? ??? ????????????.</p>
				</div>
			</div>
			
			<div class="infoDiv">
				<em><span style="color: rgb(78, 205, 196); font-size: 18px;">???</span> ????????????</em>	
				<div class="infoContent">
					<div class="comInfo">
						<div class="infoLogo">
							<img alt="" src="/resources/upload/company/${a.filepath }" style="width: 200px;">
						</div>
						<div class="infoData">
								<div class="title">
									<a href="/companyInfo.do?companyNo=${a.companyNo }">${a.companyName }</a>
									<a href="/companyInfo.do?companyNo=${a.companyNo }" class="aa" style="margin-left: 5px;"><strong style="font-size: 12px; color: #666666;">???????????? ></strong></a>
								</div>
							<div class="left">
								<div class="text">
									<p style="color: #888888; margin-bottom: 10px;">?????????</p>
									<p style="color: #888888; margin-bottom: 0px;">????????????</p>
								</div>
								<div class="data">
									<p style="color: #444444; margin-bottom: 10px;">${a.employee }???</p>
									<p style="color: #444444; margin-bottom: 0px;">${a.ceo }</p>
								</div>
							</div>
							<div class="right">
								<div class="text">
									<p style="color: #888888; margin-bottom: 10px;">??????</p>
									<p style="color: #888888; margin-bottom: 0px;">?????????</p>
								</div>
								<div class="data">
									<p style="color: #444444; margin-bottom: 10px;">${a.address }</p>
									<p style="color: #444444; margin-bottom: 0px;">${a.openDate }</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>