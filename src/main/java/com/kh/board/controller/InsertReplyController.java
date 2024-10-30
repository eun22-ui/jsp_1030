package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Reply;

/**
 * Servlet implementation class InsertReplyController
 */
@WebServlet("/insertReply.bo")
public class InsertReplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertReplyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		String replyContent = request.getParameter("replyContent");
		int replyBno = Integer.parseInt(request.getParameter("replyBno"));
		String replyWriter = request.getParameter("replyWriter");
	
		System.out.println(replyContent);
		System.out.println(replyBno);
		System.out.println(replyWriter);
		
		Reply r = new Reply(replyContent,replyBno,replyWriter);
		
		int result = new BoardService().insertReply(r);
		
		
		// 응답데이터 반환(응답화면구성은 뷰에서 진행하기 때문에 결과값만 반환한다. int만 보내니 타입변경도 안해도됨)
		response.getWriter().print(result);
		
		
	}

}
