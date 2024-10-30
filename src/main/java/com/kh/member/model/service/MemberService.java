package com.kh.member.model.service;

import java.sql.Connection;
import java.util.HashMap;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.dao.MemberDao;
import com.kh.member.model.vo.Member;

// Connection 관련처리
public class MemberService {
	
	// 로그인 메소드
	public Member loginMember(String userId, String userPwd) {
		
		// 템플릿에 만들어 놓은 Connection 반환 메소드를 이용하여 객체 반환받기
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO에게 요청하기(커넥션과 전달받은 데이터 전달하며)
		Member m = new MemberDao().loginMember(conn,userId,userPwd);
		
		
		// Select는 조회구문이기 때문에 트랜잭션 처리 필요없음
		// 자원반납만 처리
		JDBCTemplate.close(conn); // 커넥션 반납
		
		return m;
		
	}

	// 회원가입 메소드
	public int insertMember(Member m) {

		// 연결객체
		Connection conn = JDBCTemplate.getConnection();
		
		//연결객체와 전달받은 데이터 함께 dao에게 요청 및 전달
		int result = new MemberDao().insertMember(conn,m);
		
		// DML(insert)처리된 결과 행수를 이용하여 트랜잭션 처리하기
		// 확정 또는 되돌리기
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		// 자원반납
		JDBCTemplate.close(conn);
		
		// 컨트롤러에게 결과값 반환하기
		return result;
		
	}
	
	// 정보수정 메소드
	public int UpdateMember(Member m) {
		
		Connection conn = JDBCTemplate.getConnection();
		int result = 0;
		
		// dao에 요청
		result = new MemberDao().updateMember(conn,m);
		
		// update는 dml구문이니 트랜잭션처리해야한다. (commit/rollback)
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		// 자원반납
		JDBCTemplate.close(conn);
		
		// 컨트롤러에게 결과값 반환
		return result;
		
	}

	
	// 비밀번호 수정 메소드
	public int updatePwd(HashMap<String, String> map) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		// 결과값 반환받기
		int result = new MemberDao().updatePwd(conn,map);
		
		// dml구문을 수행했으니 반환받은 결과값을 이용하여 트랜잭션 처리하기
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			// 자원반납
			JDBCTemplate.close(conn);
		}
		
		return result; // 컨트롤러에게 결과값 반환하기
		
	}

	
	// 멤버삭제
	public int deleteMember(String userId, String userPwd) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().deleteMember(conn,userId,userPwd);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	
	//  아이디 중복확인
	public String idCheck(String id) {
	
		Connection conn = JDBCTemplate.getConnection();
		
		String checkId = new MemberDao().idCheck(conn,id);
		
		
		JDBCTemplate.close(conn);
		
		return checkId;
	}
	
	

}
