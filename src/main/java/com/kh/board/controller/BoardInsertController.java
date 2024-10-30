package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 카테고리 목록 조회해와서 위임하기 전에 담아주기
		// 카테고리용 vo 만들기 : Category
		// 메소드명 selectCategory()
		// 조회할 데이터는 번호,이름 둘다 조회해서 리스트에 담아오기
		
		ArrayList<Category> list = new BoardService().selectCategory();
		request.setAttribute("list", list);
		
		request.getRequestDispatcher("/views/board/boardEnrollForm.jsp").forward(request, response);
		for(Category c : list ) {
			System.out.println(c);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// request.setCharacterEncoding("UTF-8");
		
		// 유저번호,제목,카테고리,내용,첨부파일
//		String userNo = request.getParameter("userNo");
//		String title = request.getParameter("title");
//		String content = request.getParameter("content");
//		String category = request.getParameter("category");
//		
//		String uploadFile = request.getParameter("uploadFile");
		
		
		// form에서 multipart 형식으로 전송을 하게 되면 일반 request요청객체에서 데이터를 추출할 수 없다
		// multipart 형식으로 변환하여 추출해야함
		
		// 현재요청이 multipart형식인지 판별하기
		// ServletFileUpload.isMultipartContent(request) : 요청객체가 multipart형식인지 판별 메소드(맞으면 true 아니면 false)
		// ServletFileUpload 객체 사용하려면 commons-fileupload 라이브러리 추가
		
		// System.out.println(ServletFileUpload.isMultipartContent(request));
		
		if(ServletFileUpload.isMultipartContent(request)) {
			// request객체를 multipartRequest객체로 변환하여 데이터 추출하기
			// request객체를 이용하여 multipartRequest객체를 생성하면
			// 해당 시점 서버에 파일이 업로드 된다
			// 때문에 해당 파일 업로드시 필요한 준비물 준비하기
			
			// 1. 전송되는 파일을 처리할 작업내용(전송되는 파일의 용량제한, 파일 저장경로)
			// 1-1. 전송 파일 용량 제한(byte)
			// 10mb 
			// byte -> kbyte -> mbyte -> gbyte -> tbyte
			// 1kbyte == 1024byte
			int maxSize = 10 * 1024 * 1024;
			
			// 1-2. 전달받은 파일의 저장경로 알아내기
			// 실질적인 물리 경로를 알아와야한다
			// 현재 구동되고 있는 웹 애플리케이션을 기준으로 경로를 찾아줘야함
			// 웹 애플리케이션 객체 : request.getSession().getServletContext()
			// 물리적인 경로 반환 메소드 : .getRealPath();
			
			String savePath = request.getSession().getServletContext().getRealPath("/resources/uploadFiles/");
			System.out.println(savePath);
			
			// 1-3. 기존 request객체로는 파일 전달을 받을 수 없으니 MultipartRequset객체로 변환하는 작업을 수행한다
			// [표현법] MultipartRequset multipartRequest = new MultipartRequset(request,저장경로,파일최대사이즈,인코딩,피일명객체);
			// cos.jar에서 제공하는 기본파일명 변환객체 new DefaultFileRenamePolicy() (파일명에 숫자를 부여해줌)
			// 파일명이 중복되면 기존파일 덮어쓰기 되니 최대한 겹치지 않도록 직접만든 파일명 변환 객체 넣어주기
			MultipartRequest multiRequset = new MultipartRequest(request,savePath,maxSize,"UTF-8",new MyFileRenamePolicy());
			
			
			// DB에 저장할 데이터 추출하기
			// 게시글 정보는 Board에 담고 첨부파일 정보는 Attachment에 담아줄 것
			
			// multipart 인코딩방식으로 전달된 데이터는 MultipartRequest객체에서 추출해야한다
			String userNo = multiRequset.getParameter("userNo"); 
			String title = multiRequset.getParameter("title");
			String content = multiRequset.getParameter("content");
			String category = multiRequset.getParameter("category");
			
			Board b  = new Board();
			b.setBoardTitle(title);
			b.setBoardContent(content);
			b.setCategoryNo(category);
			b.setBoardWriter(userNo);
			
			// 게시글 정보는 Board 테이블에 insert
			// 첨부파일 정보는 Attachment 테이블에 insert
			
			// 첨부파일은 있는지 확인 후 있을때만 등록처리하기위한 조건달기
			Attachment at = null; // 첨부파일 정보 담을 객체변수 미리선언
			// 첨부파일이 있는지 확인하기
			// multiRequest의 getOriginalFileName("키값") : 원본파일명 반환 메소드 / 없으면 null을 반환한다
			
			System.out.println(multiRequset.getOriginalFileName("uploadFile"));
			
			// null이 아니면 (파일등록했다면)
			if(multiRequset.getOriginalFileName("uploadFile")!=null) {
				// 첨부파일 객체 생성하여 데이터 담아주기
				at = new Attachment();
				at.setOriginName(multiRequset.getOriginalFileName("uploadFile"));
				at.setChangeName(multiRequset.getFilesystemName("uploadFile"));
				at.setFilePath("/resources/uploadFiles");
			}
			
			// 게시글 정보와 첨부파일정보를 서비스에 전달하여 요청처리
			int result = new BoardService().insertBoard(b,at);
			
			// 결과값에 따라서 응답 뷰 지정하기
			HttpSession session = request.getSession();
			String alertMsg = "";
			
			if(result>0) {
				alertMsg ="게시글 등록 성공";
			}else {
				alertMsg ="게시글 등록 실패";
				// 첨부파일이 있었다면 서버에 업로드 되었을테니 필요없어진 파일 삭제처리하기
				if(at!=null) {
					// 삭제하고자 하는 파일 경로로 파일객체 생성 후 삭제 메소드 수행
					new File(savePath+at.getChangeName()).delete();
				}
			}
			session.setAttribute("alertMsg", "게시글 등록 성공");
			response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
		}
		
	}

}
