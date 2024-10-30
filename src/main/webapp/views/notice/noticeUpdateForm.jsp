<%@page import="com.kh.notice.model.vo.Notice"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#update-area>table{
		border : 1px soild white;
	}
	
	#update-area input,#update-area textarea{
		width: 100%;
		box-sizing : border-box;
	}

</style>

</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	<%
		// 공지글 정보 변수처리
		Notice n = (Notice)request.getAttribute("notice");
	%>

	<div class="outer">
		<br>
		<h2 align="center">공지사항 수정 페이지</h2>
		
	<form action="<%=contextPath%>/update.no" method="post">
		<input type="hidden" name="nno" value="<%=n.getNoticeNo() %>">
		
		<table id="update-area" align="center" border="1" >
			<tr>
				<td width="70">제목</td>
				<td width="350"> <input type="text" name="title" value="<%=n.getNoticeTitle() %>" >  </td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><%=n.getNoticeWriter() %></td>
			</tr>
			<tr>
				<td>조회수</td>
				<td><%=n.getNoticeNo() %></td>
			</tr>
			<tr>
				<td>작성일</td>
				<td><%=n.getCreateDate() %></td>
			</tr>
			<tr>
				<th>내용</th>
				<td conspan="5">
					<textarea name="content" rows="10" style="resize:none;"><%=n.getNoticeContent() %></textarea>
				</td>
			</tr>
		</table>
		<br> <br>
		
		<div align="center">
			<button type="submit" class="btn btn-primary" >수정하기</button>
			<a href="<%=contextPath %>/list.no" class="btn btn-secondary" >목록으로 이동</a>
		</div>
	</form>	
		
		
	</div>
	
</body>
</html>