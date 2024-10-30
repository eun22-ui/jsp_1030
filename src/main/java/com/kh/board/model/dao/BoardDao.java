package com.kh.board.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.board.model.vo.Reply;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;

public class BoardDao {

	private Properties prop = new Properties();

	// 기본생성자 호출시 파일 읽어오기 처리
	public BoardDao() {

		String filePath = BoardDao.class.getResource("/resources/mappers/board-mapper.xml").getPath();

		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 총 게시글 개수 조회 메소드
	public int listCount(Connection conn) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		int listCount = 0;

		String sql = prop.getProperty("listCount");

		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				// 조회된 게시글 개수 추출하기
				listCount = rset.getInt("COUNT");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return listCount;
	}

	// 게시글 목록 조회 메소드
	public ArrayList<Board> selectList(Connection conn, PageInfo pi) {

		// select구문
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		// 빈 리스트 준비
		ArrayList<Board> list = new ArrayList<>();

		// 구문준비
		String sql = prop.getProperty("selectList");

		// 현재 페이지에 따라서 보여줘야하는 게시글 번호 계산하기
		// 1페이지 : 게시글 1~10까지 보여주기
		// 2페이지 : 게시글 11~20까지 보여주기
		// 3페이지 : 게시글 21~30까지 보여주기
		// 4페이지 : 게시글 41~50까지 보여주기
		// ...
		// currentPage : 현재페이지수 / boardLimit : 한페이지에 보여줄 게시글 개수

		// 시작번호 : (currentPage-1)*게시글 보여줄 수+1
		int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;

		// 끝번호 : 현재페이지 수 * 게시글 보여줄 수
		int endRow = pi.getCurrentPage() * pi.getBoardLimit();

		try {
			pstmt = conn.prepareStatement(sql);
			// 위치홀더 채워주기 (게시글 시작번호 / 끝번호)
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);

			rset = pstmt.executeQuery();

			// 목록조회
			while (rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO"), rset.getString("CATEGORY_NAME"),
						rset.getString("BOARD_TITLE"), rset.getString("USER_NAME"), rset.getInt("COUNT"),
						rset.getDate("CREATE_DATE")));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return list;
	}

	// 조회 수 증가메소드
	public int increaseCount(Connection conn, int bno) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("increaseCount");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	
	
	public Board selectBoard(Connection conn, int bno) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectBoard");
		Board b = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				b = new Board(rset.getInt("BOARD_NO"), rset.getString("CATEGORY_NAME"), rset.getString("BOARD_TITLE"),
						rset.getString("BOARD_CONTENT"), rset.getString("USER_ID"), rset.getDate("CREATE_DATE"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return b;
	}
	
	

	// 카테고리 목록조회
	public ArrayList<Category> selectCategory(Connection conn) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectCategory");
		ArrayList<Category> list = new ArrayList();
	
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Category(rset.getInt("CATEGORY_NO")
									,rset.getString("CATEGORY_NAME")
						));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list;
	}
	
	
	// 게시글 시퀀스 번호 추출용 메소드
	public int selectBoardNo(Connection conn) {
		// select 구문으로 시퀀스 발행해오기
		int boardNo = 0;
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("selectBoardNo");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				boardNo = rset.getInt("BNO");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
			JDBCTemplate.close(rset);
		}
		
		return boardNo;
	}
	
	
	// 게시글 정보 등록 메소드
	public int insertBoard(Connection conn, Board b) {
		//insert(DMl)
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertBoard");
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, b.getBoardNo());
			pstmt.setInt(2, Integer.parseInt(b.getCategoryNo())); // 담을땐 문자열로 담았으니 int로 형변환(Number)
			pstmt.setString(3, b.getBoardTitle());
			pstmt.setString(4, b.getBoardContent());
			// pstmt.setString(5, b.getBoardContent()); > 이렇게 해도 내부적으로 문자열 숫자를 숫자로 변환해주니 처리가능
			pstmt.setInt(5, Integer.parseInt(b.getBoardWriter()));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 첨부파일정보 등록 메소드
	public int insertAttachment(Connection conn, Attachment at) {
		
		//insert(DMl)
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());
			
			result = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	
	// 첨부파일 조회메소드
	public Attachment selectAttachment(Connection conn, int bno) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Attachment at = null; // 조회되면 담을 객체
		
		String sql = prop.getProperty("selectAttachment");
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			
			rset = pstmt.executeQuery();
			
			// 일반 게시글에 첨부파일은 하나만 등록될 수 있기 때문에 if문으로 처리
			if(rset.next()) {
				at = new Attachment(rset.getInt("FILE_NO")
									,rset.getString("ORIGIN_NAME")
									,rset.getString("CHANGE_NAME")
									,rset.getString("FILE_PATH"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return at;
	}
	
	
	// 게시글 수정
	public int updateBoard(Connection conn, Board b) {
		
		// update(dml)
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setInt(3, Integer.parseInt(b.getCategoryNo()));
			pstmt.setInt(4, b.getBoardNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 첨부파일 수정
	public int updateAttachment(Connection conn, Attachment at) {
		
		// update(dml)
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	

	public int deleteBoard(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 사진게시글 작성 메소드
	public int insertPhoto(Connection conn, Board b) {
		// dml (insert)
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertPhoto");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBoardNo());
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setInt(4, Integer.parseInt(b.getBoardWriter()));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 첨부파일 목록 추가
	public int insertAttachmentList(Connection conn, ArrayList<Attachment> atList, int boardNo) {
		
		// DML(insert)
		// 파일목록에 대해 모두 등록 처리하기
		int result = 1; // insert문을 여러번 사용하여 하나라도 0이면 0이 될 수 있도록 처리하기
		
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachmentList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			// atList에 추가되어있는 첨부파일 전부다 처리하기
			for(Attachment at : atList) {
				
				pstmt.setInt(1, boardNo);
				pstmt.setString(2, at.getOriginName());
				pstmt.setString(3, at.getChangeName());
				pstmt.setString(4, at.getFilePath());
				pstmt.setInt(5, at.getFileLevel());
				
				
				// 1로 초기화 해둔 result에 결과값을 곱셈복합대입연산으로 처리
				// 하나라도 잘못되었을때 0으로 값이 변경되어 실패 확인할 수 있다
				result *= pstmt.executeUpdate(); // 1 or 0 
			}
		
		
		} catch (SQLException e) {
			
			// 만약 처음 반복에서 예외가 발생하여 처리되지 않았다면 (처음에 1로 설정되어있었기 때문에 for문 수행전에 내려오면)
			// result를 0으로 변경하여 실패 판별처리 
			result = 0;
			
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 사진게시글 목록조회 메소드
	public ArrayList<Board> selectThumbnailList(Connection conn) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Board> list = new ArrayList<>();
		String sql = prop.getProperty("selectThumbnailList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				list.add(new Board (rset.getInt("BOARD_NO"),
									rset.getString("BOARD_TITLE"),
									rset.getInt("COUNT"),
									rset.getString("TITLEIMG")));
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		return list;
	}

	
	
	// 사진게시글 첨부파일 목록 조회 메소드
	public ArrayList<Attachment> selectAttachmentList(Connection conn, int boardNo) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Attachment> list = new ArrayList<>();
		
		String sql = prop.getProperty("selectAttachmentList");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			rset = pstmt.executeQuery();
			
			// 한 게시글에 여러 첨부파일 있을 수 있으니 반복문으로 전부 꺼내주기
			while(rset.next()) {
				
				Attachment at = new Attachment();
				at.setChangeName(rset.getString("CHANGE_NAME"));
				at.setFilePath(rset.getString("FILE_PATH"));
				
				list.add(at);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list;
	}

	
	
	
	
	// 댓글 작성 메소드
	public int insertReply(Connection conn, Reply r) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertReply");
		
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, r.getReplyContent());
			pstmt.setInt(2, r.getReplyBno());
			pstmt.setInt(3, Integer.parseInt(r.getReplyWriter()));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	
	// 댓글 목록조회
	public ArrayList<Reply> replyList(Connection conn, int refBno) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Reply> r = new ArrayList<>();
		String sql = prop.getProperty("replyList");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, refBno);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				r.add(new Reply(
									rset.getInt("REPLY_NO"),
									rset.getString("REPLY_CONTENT"),
									rset.getString("USER_ID"),
									rset.getDate("CREATE_DATE")
								));
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return r;
	}
	
	
	

}
