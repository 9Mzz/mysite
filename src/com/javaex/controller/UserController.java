package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/user");
		request.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action");
	
		if("joinForm".equals(action)) { //회원가입 폼
			System.out.println("user > joinForm");
					
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			
		}else if("join".equals(action)) { //회원가입
			System.out.println("user > join");
			
			//파라미터값 가져오기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//파라미터-->vo로 만들기
			UserVo userVo = new UserVo(id, password, name, gender);
			//System.out.println(userVo);
			
			//UserDao 의 insert()로 저장하기(회원가입하기)
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
				
		}else if("loginForm".equals(action)) { //로그인 폼
			System.out.println("user > loginForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		}else if("login".equals(action)) {
			System.out.println("user > login");
			
			//파라미터값 가져오기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			
			//UserDao 의 getUser()로 사용자 정보 가져오기
			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, password);
			//System.out.println(uservo);
			
			if(authVo == null) { //로그인실패
				System.out.println("로그인실패");
				
				//로그인 페이지로 리다이렉트 실패메세지 추가
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
			}else { //로그인성공
				System.out.println("로그인성공");
				
				//세션영역 로그인한 사용자 데이터 추가(로그인 기능)
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				
				//리다이렉트
				WebUtil.redirect(request, response, "/mysite/main");
			}
			
						
		}else if("logout".equals(action)) {//로그아웃 일때
			System.out.println("user > logout");
			
			//세션영역 데이터 제거(로그아웃 기능)
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/main");
			
		}else if("modifyForm".equals(action)) {
			System.out.println("user > modifyForm");
			
			//사용자 정보를 가져오기 위한 no값은 세션에서 가져온다.
			//세션의 정보 ==  로그인한 사용자 정보
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("authUser")).getNo();
			
			
			//UserDao 의 getUser()로 사용자 정보 가져오기
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(no);	
			
			System.out.println(userVo);
			//포워드 --> 데이터전달(요청문서의 바디(attributte))
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
			
		}else if("modify".equals(action)) {
			System.out.println("user > modify");
			
			//파라미터값 가져오기
			//   로그인한 사용자 정보는 세션에서 가져온다.
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("authUser")).getNo();
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			//    아래처럼 생성자 사용가능 --> 모양이 맞는 생성자 사용추천
			UserVo vo = new UserVo(no, "", password, name, gender);
			
			
			//Dao 사용
			UserDao userDao = new UserDao();
			userDao.update(vo);
			
			
			//세션값 업데이트(이름만 수정)
			UserVo sVo = (UserVo)session.getAttribute("authUser");
			sVo.setName(name);
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/main");
		}
		
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
