package com.hx.framework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.jws.soap.InitParam;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Servlet implementation class mydispatcher
 */
@WebServlet(value="/",initParams={@WebInitParam(name = "xmlconfig", value = "/myservlet.xml"),
		                          @WebInitParam(name= "viewPrefix", value= "/WEB-INF/view/"),
		                          @WebInitParam(name= "viewSufix", value= ".jsp"),})
public class mydispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String viewPrefix;
	private String viewSufix;
     
	public void init() throws ServletException {
	      
		   //init ��ʱ�� ����ȡmyservlet.xml�ļ���λ��
		   String config=this.getInitParameter("xmlconfig");
		  
		   system_servlet ss=new system_servlet();
		   ss.reading(config);
		   System.out.println(system_servlet.map.keySet());
		   viewPrefix=this.getInitParameter("viewPrefix");
		   viewSufix=this.getInitParameter("viewSufix");
		   
	}
	
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mydispatcher() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	    //�䵱�ܿ�������servlet,��Ŀ���Ǵ�URL�н������û�Ҫ�����controller�Լ�action
		String uri=request.getRequestURI();
		String query_string=request.getQueryString();
		
		String action=uri.substring(uri.lastIndexOf("/")+1).equals("")?"index":uri.substring(uri.lastIndexOf("/")+1);
		
		String _controller=uri.substring(0,uri.lastIndexOf("/"));
		String controller=_controller.substring(_controller.lastIndexOf("/")+1);
		
		String _contextPath=request.getContextPath().substring(1,request.getContextPath().length());
		controller=controller.equalsIgnoreCase(_contextPath)?"index":controller;
		System.out.println(controller+"/"+action);
			//deptController c=new deptController();
			Class cl;
			try {
				cl = Class.forName(system_servlet.map.get(controller).getClazz());
				Object obj = cl.newInstance();
				Method m=cl.getMethod(action,HttpServletRequest.class,HttpServletResponse.class);
				Object o = m.invoke(obj,request,response);
				String result=(String)o;
				/*
				 * ""------ת��ȥĬ����ͼ
                   
                   "ajax:"------ʲô�ط�Ҳ����ת����Ϊ����������һ��ajax�첽����  
                   "same:"------����ͬһ����������һ����
                   "other:"------������һ�������еķ���
                   "jsp"------ֱ��ת��ȥһ��ָ����jsp   
 
				 * */
				
				//result�а���������Ҫ��תȥ�ĵط�,
				if(result.equals("")) {
					//������Լ��ת��ȥĬ����ͼ
					request.getRequestDispatcher(viewPrefix+controller+"/"+action+viewSufix).forward(request,response);
				}else if(result.equals("ajax:")){
					//ʲô�ط�Ҳ�����������첽ajax����
				}else if(result.startsWith("same:")) {
					action=result.substring(result.indexOf(":")+1);
					request.getRequestDispatcher("/"+controller+"/"+action).forward(request,response);
				}else if(result.startsWith("other:")) {
					//����һ���������ƣ��������ƽ�������
					String _url=result.substring(result.indexOf(":")+1);
					String[] _u=_url.split("/");
					request.getRequestDispatcher("/"+_u[0]+"/"+_u[1]).forward(request,response);
				}else if(result.startsWith("jsp:")) {
					String _url=result.substring(result.indexOf("jsp_dispatcher:")+1);
					request.getRequestDispatcher(viewPrefix+ _url).forward(request,response);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
