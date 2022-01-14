package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;

@WebServlet("/guestbook")
public class GuestbookController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/guestbook");
		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		if ("add".equals(action)) {
			System.out.println("guestbook > add");

			// 파라미터 값 추출 --> vo만들기
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");

			// Dao 사용
			GuestbookDao guestbookDao = new GuestbookDao();
			GuestbookVo guestbookVo = new GuestbookVo(name, password, content);
			guestbookDao.insert(guestbookVo);

			// 리다이렉트
			WebUtil.redirect(request, response, "/mysite/guestbook");

		} else if ("deleteForm".equals(action)) {
			System.out.println("guestbook > deleteform");

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");

		} else if ("delete".equals(action)) {
			System.out.println("guestbook > delete");

			// 파라미터 값 추출 --> vo만들기
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");

			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);

			// Dao 사용
			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);

			// 리다이렉트
			WebUtil.redirect(request, response, "/mysite/guestbook");

		} else {// 리스트
			System.out.println("guestbook > default:list");

			// Dao 사용
			GuestbookDao dao = new GuestbookDao();
			List<GuestbookVo> gList = dao.getList();

			// 포워드 --> 데이터전달(요청문서의 바디(attributte))
			request.setAttribute("guestList", gList);
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}