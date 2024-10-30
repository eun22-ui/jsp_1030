package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class MemberIdCheckController
 */
@WebServlet("/idCheck.me")
public class MemberIdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberIdCheckController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String id = request.getParameter("input");
		String responseMsg ="";
		// System.out.println(id);
		
		// 서블릿까지 왔다는건 서버에 요청가능하다는 소리 > 서비스에 전달 및 요청하기
		String checkId = new MemberService().idCheck(id); 
		
		// System.out.println(checkId);
		
		if(checkId==null) {
			responseMsg = "NNNNY";
		}else {
			responseMsg ="NNNNN";
		}
		
		response.setContentType("text/html;charset=UTF-8"); // 인코딩설정
		response.getWriter().print(responseMsg);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
