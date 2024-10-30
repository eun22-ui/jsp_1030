<%@page import="com.kh.notice.model.vo.Notice"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	// 스크립틀릿 영역
	// 조회해온 공지 목록 변수 처리
	ArrayList<Notice> list = (ArrayList<Notice>)request.getAttribute("list");
	


%>    
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	
	<div class="outer">
		<br>
		<h1 align="center">공지사항</h1>
		
		<!-- 공지사항 작성은 관리자만 할 수 있다
			 조건처리를 통해서 글 작성 버튼을 관리자일때만 보일 수 있도록 작업하기
		 -->
		 
		 <!-- 로그인 되어 있고 로그인 된 아이디가 admin일 경우에만 글 작성 버튼 보이도록 처리  -->
		 <%if(loginUser!=null && loginUser.getUserId().equals("admin")) { %>
		
		<div align="center">
			<br>
			<%-- <button onclick="location.href='경로'">글작성</button> --%>
			<a href="<%=contextPath %>/insert.no" class="btn btn-info">글작성</a>
		</div>
		
			<%} %>
		
			<br>
			
			
		<!-- 글 목록 보여줄 테이블 작성 -->
		<table border="1" align="center" class="list-area" >
			<thead>
				<tr>
					<th>글번호</th>
					<th width="400">글제목</th>
					<th width="100">작성자</th>
					<th>조회수</th>
					<th width="100">작성일</th>
				</tr>
			</thead>
			<tbody>
			
			<%-- 자바 반복문을 이용하여 목록에 있는 데이터 전부 tr로 만들어 주기 --%>
			<%-- 비어있다면 --%>
			<%if(list.isEmpty()) {%>
				<tr>
					<td colspan="5">조회된 공지사항이 없습니다.</td>
				</tr>
			<%}else { %>
			<!-- 게시글목록이 있다면 뽑아주기 -->
				<%for(Notice n :list) {%>
				<tr>
					<td><%=n.getNoticeNo() %></td>
					<td><%=n.getNoticeTitle() %></td>
					<td><%=n.getNoticeWriter() %></td>
					<td><%=n.getCount() %></td>
					<td><%=n.getCreateDate() %></td>
				</tr>
				<%} %>
			<%} %>	
				
			</tbody>
		</table>
		<br> <br>
		
		<script>
			// 특정글을 클릭했을 때 해당글의 글번호를 console.log로 찍어보세요
			
			$(function(){
				
				$(".list-area>tbody").on("click","tr",function(){
					
			
					// 수정시 f5..하기
					var nno = $(this).children().first().text();
					
					console.log($(this).val());
					console.log(nno);
					
					// 상세보기 요청시 추출한 글 번호를 전달하며 요청하기
					// 단순 조회방식이니 get방식으로 진행
					// get 방식은 url에 요청주소 및 전달값이 노출되는 형태
					// 매핑주소 뒤에 ? 기준으로 key=value 세트로 데이터 전달됨
					// 요청경로 : /jsp/detail.no?noo=글번호	
					// 요청 매핑 주소 뒤에 ? 기준내용들은 쿼리스트링이라고 부른다
					// 키=값&키=값&키=값 형태로 여러 데이터가 전달 될 수 있음
					// 이때 전달된 데이터는 parmeter영역에서 키값을 통해 추출할 수 있다
					// 	location.href = '/jsp/detail.no?nno='+nno;
					location.href = '<%=contextPath %>/detail.no?nno='+nno;
					
				});
				
				
			});
		
		
		</script>
			
	
	</div>
</body>
</html>