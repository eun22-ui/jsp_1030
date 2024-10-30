<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#enroll-form table {
	margin: auto
}

#enroll-form input {
	margin: 5px;
}
</style>

</head>
<body>
	<!-- 메뉴바 가져오기(절대경로방식) -->
	<%-- <%@ include file="/views/common/menubar.jsp" %> --%>

	<!-- 메뉴바 가져오기(상대경로방식) : 현재파일 위치 기준으로 찾기 
		.. / : 현재폴더로 부터 상위폴더로 한번이동
	-->
	<%@ include file="../common/menubar.jsp"%>

	<div class="outer">
		<br>
		<h2 align="center">회원가입</h2>

		<!-- 위에 menubar를 include해오고 해당 페이지에 선언되어있는 변수는 사용가능하다(ContextPath) -->
		<form action="<%=contextPath %>/insert.me" id="enroll-form"
			method="post" disabled>

			<!-- 아이디,비밀번호,이름,전화번호,이메일,주소,취미 -->
			<table>
				<tr>
					<td>* 아이디</td>
					<td><input type="text" name="userId" id="userId"
						maxlength="12" required></td>
					<td><button type="button" onclick="idCheck();">중복확인</button></td>
					<!-- ajax배우고 진행 -->
					<!-- 
					
						비동기 통신을 이용하여 버튼을 눌렀을때 아이디 입력란에 작성된 아이디를 
						서버에 전달 및 요청하여 아이디 중복확인을 하고 오기.
						매핑주소 : idCheck.me
						컨트롤러 : MemberIdCheckController
						메소드명 : idCheck()
						버튼을 눌렀을때 요청 후 응답데이터를 통해 사용가능하면 사용가능한 아이디입니다. 사용하시겠습니까?
						확인(사용하겠다)시에 input id 작성란을 readonly속성을 넣어 확정해주기
						또한 기존에 아이디 중복체크가 되지 않은 상황에서는 회원가입 버튼 비활성화(disabled)
						중복체크 후 활성화 하여 회원가입처리 가능하도록 구현하기
						
						응답데이터는 네이버처럼 사용가능 NNNNY / 사용불가 NNNNN으로 처리하기
					
					 -->

				</tr>
				<tr>
					<td>* 비밀번호</td>
					<td><input type="password" name="userPwd" id="pwd"
						maxlength="15" required></td>
					<td></td>
				</tr>
				<tr>
					<td>* 비밀번호 확인</td>
					<td><input type="password" maxlength="15" id="chkPwd" required></td>
					<!-- 단순비교 용도이기 때문에 name속성 필요없음 -->
					<td></td>
				</tr>
				<tr>
					<td>* 이름</td>
					<td><input type="text" name="userName" maxlength="6" required></td>
					<td></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;전화번호</td>
					<td><input type="text" name="phone" placeholder="-포함해서 입력"></td>
					<td></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;이메일</td>
					<td><input type="email" name="email"></td>
					<td></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;주소</td>
					<td><input type="text" name="address"></td>
					<td></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;관심분야</td>
					<td colspan="2"><input type="checkbox" name="interest"
						id="sports" value="운동"> <label for="sports">운동</label> <input
						type="checkbox" name="interest" id="hiking" value="등산"> <label
						for="hiking">등산</label> <input type="checkbox" name="interest"
						id="fishing" value="낚시"> <label for="fishing">낚시</label> <input
						type="checkbox" name="interest" id="cooking" value="요리"> <label
						for="cooking">요리</label> <input type="checkbox" name="interest"
						id="game" value="게임"> <label for="game">게임</label> <input
						type="checkbox" name="interest" id="movie" value="영화"> <label
						for="movie">영화</label></td>

				</tr>

			</table>


			<br>
			<br>

			<div align="center">
				<button type="submit" onclick="pwdCheck();">회원가입</button>
				<button type="reset">초기화</button>
			</div>

		</form>
	</div>

	<script>
	 
	 	function pwdCheck() {
			var userPwd = document.querySelector("#pwd");
			var checkPwd = document.querySelector("#chkPwd");
			
			console.log(userPwd.value);
			console.log(checkPwd.value);
			
			if(userPwd.value != checkPwd.value){
				alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
				userPwd.focus();
				return false;
			}
		}
	 
	 
	 	
	 /* 아이디 중복확인 ajax */
	 	function idCheck() {
			
		 	$.ajax({
		 		
		 		
		 		
		 		url : '/jsp/idCheck.me',
		 			// "${contextPath}/idCheck.me"도 동일
		 			
		 		data : {
		 					input : $("#userId").val()
		 		},
		 		
		 		
		 		success : function(result){
			 			console.log("통신성공");
			 			console.log(result);
			 			
			 			
				 	/* 		
				 			버튼을 눌렀을때 요청 후 응답데이터를 통해 사용가능하면 사용가능한 아이디입니다. 사용하시겠습니까?
									확인(사용하겠다)시에 input id 작성란을 readonly속성을 넣어 확정해주기
									또한 기존에 아이디 중복체크가 되지 않은 상황에서는 회원가입 버튼 비활성화(disabled) */
									
						if(result == "NNNNY"){
							
							//confirm("사용가능한 아이디입니다. 사용하시겠습니까?")
							
								if (confirm("사용가능한 아이디입니다. 사용하시겠습니까?")) {
									//document.getElementById("enroll-form").disabled = true; 
									$("#enroll-form :submit").removeAttr("disabled");
									document.getElementById("userId").readonly = true;
									//document.getElementById("userId").val().attr("readonly",true);
									
								} 
							
				 		}else if(result == "NNNNN"){ // 사용불가
				 			alert("이미 존재하는 아이디 입니다.");
				 			$("#userId").focus(); // 다시작성유도
						}
		 		
		 		},
		 		
		 	
		 	
		 		error : function(){
		 			console.log("통신실패");
		 		},
		 		
		 		complete : function(){
		 			console.log("성/실패 상관없이 수행됨");
		 		}
		 		
		 	});
		 
		}
	 	
	 	
	 </script>






</body>
</html>