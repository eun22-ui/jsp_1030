package com.kh.notice.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.vo.Notice;

	
public class NoticeDao {
	
	// 생성자 구문에서 mapper파일 읽어오기
	
	private Properties prop = new Properties();
	
	public NoticeDao() {
		
		// notice-mapper.xml 파일경로 알아오기
		String filePath = NoticeDao.class.getResource("/resources/mappers/notice-mapper.xml").getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	// 공지사항 정보 가져오기
	public ArrayList<Notice> selectNoticeList(Connection conn) {
		
		// ArrayList<Notice> list = null;
		ArrayList<Notice> list = new ArrayList<>();
		Statement stmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectNoticeList");
		
		try {
			
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			// 조회결과가 여러개 일 수 있으니 모든 결과를 추출하기 위해 반복문 필요
			while(rset.next()) { 
				
				list.add(new Notice( rset.getInt("NOTICE_NO")
									   ,rset.getString("NOTICE_TITLE")
									   ,rset.getString("NOTICE_WRITER")
									   ,rset.getInt("COUNT")
									   ,rset.getDate("CREATE_DATE")));
				
				
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		
		return list;
	}

	
	// 공지사항 등록 메소드
	public int insertNotice(Connection conn, Notice n) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("inserNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			// noticeWriter는 String 자료형이고, Notice테이블의 notice_writer컬럼은 number 타입이니 형변환으로 맞춰주기
			pstmt.setInt(3, Integer.parseInt(n.getNoticeWriter()));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	
	// 조회수 증가
	public int increaseCount(Connection conn, int nno) {
		
		PreparedStatement pstmt = null;
		int numUp = 0;
		String sql = prop.getProperty("increaseCount");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			
			numUp = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		
		return numUp;
	}

	
	
	// 증가 후 게시글 자료 조회
	public Notice selectNotice(Connection conn, int nno) {
		
		// select구문 준비
		PreparedStatement pstmt = null;
		ResultSet rset = null; // 조회구문시 결과집합 담을 객체선언
		String sql = prop.getProperty("selectNotice");
		
		// 조회된 글 정보가 있다면 객체 생성하여 담아줄 객체변수 준비
		Notice n = null;
		
		try {
			// 미완성 sql구문 전달하며 객체 생성
			pstmt = conn.prepareStatement(sql);
			// 위치홀더 채우기
			pstmt.setInt(1, nno);
			// 완성된 구문 실행 및 결과 받기
			rset = pstmt.executeQuery(); //select구문은 executeQuery()메소드
			
			// 조회조건이 식별자를 이용한 조건이기 때문에 나온다면 하나의 행만 나올 수 있음
			// 조건처리를 있는지 없는지만 하면 된다
			if(rset.next()) {
				
				// 조회된 행이 있다면 해당데이터 추출하여 자바 VO에 담아주기
			n = new Notice (rset.getInt("NOTICE_NO")
							 ,rset.getString("NOTICE_TITLE")
							 ,rset.getString("USER_NAME")
							 ,rset.getInt("COUNT")
							 ,rset.getDate("CREATE_DATE")
							 ,rset.getString("NOTICE_CONTENT"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		return n; // 조회되었으면 객체 / 안되었으면 null 반환
	}

	
	// 게시글 수정 업데이트
	public int updateNotice(Connection conn, String title, String content, int nno) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, nno);
			
			//완성되면 수행 및 결과받기 
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		return result;
	}

	
	// 공지글 삭제 메소드
	public int delteNotice(Connection conn, int nno) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("delteNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		return result;
	}

}
