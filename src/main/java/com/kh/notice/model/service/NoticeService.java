package com.kh.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.dao.NoticeDao;
import com.kh.notice.model.vo.Notice;

public class NoticeService {

	// 공지사항 목록메소드
	public ArrayList<Notice> selectNoticeList() {

		Connection conn = JDBCTemplate.getConnection();

		ArrayList<Notice> list = new NoticeDao().selectNoticeList(conn);

		// 조회는 트랜잭션 처리 X 자원반납만 하기
		JDBCTemplate.close(conn);

		return list;
	}

	// 공지사항 등록 메소드
	public int insertNotice(Notice n) {

		Connection conn = JDBCTemplate.getConnection();
		int result = new NoticeDao().insertNotice(conn, n);

		// dml구문이니 트랜잭션 처리하기
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		// 자원반납
		JDBCTemplate.close(conn);

		return result;
	}

	// 조회수 증가
	public int increaseCount(int nno) {

		Connection conn = JDBCTemplate.getConnection();
		// 처리된 행 수 돌려받기
		int numUp = new NoticeDao().increaseCount(conn, nno);

		// dml 트랜잭션처리
		if (numUp > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return numUp;
	}

	// 증가후 게시글 자료 조회
	public Notice selectNotice(int nno) {

		Connection conn = JDBCTemplate.getConnection();
		Notice n = new NoticeDao().selectNotice(conn, nno);

		// select - 트랜잭션 처리하지 않음(단순조회)
		JDBCTemplate.close(conn);

		return n;
	}

	// 게시글 수정하기
	public int updateNotice(String title, String content, int nno) {

		Connection conn = JDBCTemplate.getConnection();
		int result = new NoticeDao().updateNotice(conn, title, content, nno);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return result;
	}

	
	// 공지글 삭제 메소드
	public int delteNotice(int nno) {

		Connection conn = JDBCTemplate.getConnection();
		int result = new NoticeDao().delteNotice(conn, nno);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return result;
	}

}
