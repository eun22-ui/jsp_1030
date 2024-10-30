package com.kh.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class MemberDeleteController
 */
@WebServlet("/delete.me")
public class MemberDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		// 전달받은 데이터 추출
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		System.out.println(userId);
		System.out.println(userPwd);
		
		int result = new MemberService().deleteMember(userId,userPwd);
		
//		성공시 : 로그인 해제 후 세션에 그동안 사이트를 이용해주셔서 감사합니다. 메세지를 담고 (alert) 메인페이지로 돌아오기
//		실패시 : 회원탈퇴 실패 메시지를 세션에 담고 마이페이지로 돌아오기(재요청) 
		
		HttpSession session = request.getSession();
		
		// 결과값을 이용해서 응답페이지 지정
		
		if(result>0) {
			
			session.setAttribute("alertMsg", "그동안 사이트를 이용해주셔서 감사합니다");
			// 로그인 해제 될 수 있도록 로그인 정보 삭제
			session.removeAttribute("loginUser");
			// 메인페이지로 재요청
			response.sendRedirect(request.getContextPath());
			
		}else {
			
			session.setAttribute("alertMsg", "회원탈퇴 실패.");
			response.sendRedirect(request.getContextPath()+"/mypage.me");
		}
	
	}

}
