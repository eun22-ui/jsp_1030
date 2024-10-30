<%@page import="com.kh.member.model.dao.MemberDao"%>
<%@page import="com.kh.common.JDBCTemplate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<!-- 
		Create : (Insert)
		Read : (Select)
		Update : (Update)
		Delete : (Delete)
		
			회원서비스 :
			로그인 : Select
			회원가입 : Insert / 아이디 중복확인 : Select
			마이페이지 : Select 
			정보수정 : Update
			회원탈퇴 : Delete / Update(상태값으로 처리)
			
			공지사항 서비스 : 
			목록조회 : Select / 상세조회 : Select
			글 작성 : Insert
			글 수정 : Update
			글 삭제 : Delete / Update(상태값을 이용하여 삭제처리)
			
			일반 게시판 서비스 : 
			공지사항 서비스 + @
			+ 페이징처리 : 게시글 목록을 몇개씩 몇페이지로 보여줄 것인지 처리 (Select)
			+ 파일 업로드 : 게시글 작성시 파일 업로드 추가(Insert)
			+ 댓글 목록 조회 : Select 
			+ 댓글 작성 : Insert / 수정 : Update / 삭제 : Delete/Update
			
			사진게시판 서비스 :
			+ 다중 파일 업로드 : 일반 게시판에서는 파일 1개만 업로드 가능
							 사진 게시판에서는 여러개 업로드 처리해보기(Insert) 
	 -->
	 
	 <!-- 파일 경로 찾기  -->
	 <%-- <%=JDBCTemplate.getConnection() %> --%>
	 <%-- <% new MemberDao(); %> --%>
	 
	 
	 <%-- 첫 화면에 menubar 포함되도록 작업 --%>
	 <%-- 경로에서 / 로 시작하면 webapp 기준(절대경로) --%>
	 <%-- 경로에서 / 없이 하면 현재 작성파일 위치 기준으로(상대경로) --%>
	 <%@ include file="/views/common/menubar.jsp" %>
	 
	 <!-- 파일 잘 만들어줬는지 확인 -> 오류안나면 ok -->
	 <%new com.kh.notice.model.dao.NoticeDao(); %> 
	 <%new com.kh.board.model.dao.BoardDao(); %>
	 
	 
	 

</body>
</html>