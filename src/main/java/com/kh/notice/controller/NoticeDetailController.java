package com.kh.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeDetailController
 */
@WebServlet("/detail.no")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet
	(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// url로 전달한 nno를 추출해보기
		int nno = Integer.parseInt(request.getParameter("nno"));
		
		// 게시글 조회를 하기 전에 조회수 증가 메소드부터 처리하기
		// 추출한 글번호를 이용하여 해당 글 조회수를 1증가시키고 (update)
		// 해당 작업이 성공적으로 이루어졌다면 - 해당 글번호로 글 조회하여 noticeDetailView에 전달 및 위임해서 글 정보 보여주기
		// 해당 작업이 실패했다면 세션에 알림메시지(공지글 조회 실패) 담아주고 글 목록페이지 재요청하기
		
		// 조회수 증가 메소드명 : increaseCount()
		int numUp = new NoticeService().increaseCount(nno);
		// HttpSession session = request.getSession();
		
		// 결과값에 따라서 응답처리하기
		if(numUp>0) {
			// 글번호로 DB에 있는 글정보 추출해오기
			// 공지글 조회 메소드명 : selectNotice()
			
//			NOTICE_NO
//   		NOTICE_TITLE
//			USER_NAME ***
//			COUNT
//			CREATE_DATE
//			NOTICE_CONTENT
			
			Notice n = new NoticeService().selectNotice(nno);
			
			// 조회해온 글정보 request에 담아서 위임
			request.setAttribute("notice", n);
			
			// System.out.println(session.setAttribute("notice"));
			// 상세페이지로 위임
			request.getRequestDispatcher("views/notice/noticeDetailView.jsp").forward(request, response);
		}else {
			// session.setAttribute("errorMsg", "공지글 조회 실패");
			request.getSession().setAttribute("alertMsg", "공지글 조회 실패");
			response.sendRedirect(request.getContextPath()+"/list.no");
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
