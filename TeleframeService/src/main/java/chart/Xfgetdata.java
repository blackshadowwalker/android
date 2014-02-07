package chart;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.services.base.DatabaseTool;
import com.services.base.baseForm;

public class Xfgetdata extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

response.setCharacterEncoding("utf-8");
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs= null;
		StringBuffer sb = null;
		String type=request.getParameter("type");
		String time=request.getParameter("time");
		
		
		String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		String path = request.getContextPath();
		//String basePath = baseServer+path;
		//String smallImageEnd = baseForm.getSmallImageEnd();
		String itsImageRoot = baseForm.getItsImageRoot();
		if(itsImageRoot==null || itsImageRoot.isEmpty())
			itsImageRoot = path;
		
		String finalPath=baseServer + itsImageRoot;
		
		//finalPath="http://10.168.1.250:8888/kk/upload/ITS_upload/";
		String url=null;

		try {
			con = DatabaseTool.getConnection();
			sb = new StringBuffer();
			if(type.equals("chart")){
				pstm=con.prepareStatement("SELECT CASE EventAbbr WHEN 'fire' THEN '火' WHEN 'smoke' THEN '烟' WHEN 'block' THEN '阻塞' WHEN 'KXJC' THEN '绊线' END," +
						"COUNT(1) FROM af_event WHERE AlarmStartTime BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY) GROUP BY EventAbbr");
				//System.out.println("SELECT dir,COUNT(1) FROM kk_event WHERE absTime BETWEEN '"+time+"' AND DATE_ADD('"+time+"', INTERVAL 1 DAY) GROUP BY dir ORDER BY dir DESC");
				pstm.setString(1, time);
				pstm.setString(2, time);
				rs=pstm.executeQuery();
				while(rs!=null && rs.next()){
					sb.append(rs.getString(1)).append(",").append(rs.getString(2)).append("#");
				}
				//System.out.println(sb.toString());
				response.getWriter().write(sb.toString());
			}else if(type.equals("table")){
				pstm=con.prepareStatement("SELECT id,location,personInCharge,eventName,AlarmStartTime FROM af_event " +
						"WHERE AlarmStartTime BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY) ORDER BY AlarmStartTime DESC");
				pstm.setString(1, time);
				pstm.setString(2, time);
				rs=pstm.executeQuery();
				while(rs!=null && rs.next()){
					sb.append(rs.getString(1)).append(",").append(rs.getString(2)).append(",").append(rs.getString(3)).append(",").append(rs.getString(4)).append(",").append(rs.getString(5)).append("#");
				}
				//System.out.println(sb.toString());
				response.getWriter().write(sb.toString());
			}else{
				String id=request.getParameter("id");
				pstm=con.prepareStatement("SELECT location,personInCharge,eventName,AlarmStartTime,AlarmStartPic FROM af_event WHERE id=?");
				pstm.setString(1, id);
				rs=pstm.executeQuery();
				if(rs!=null && rs.next()){
					sb.append(rs.getString(1)).append(",").append(rs.getString(2)).append(",").append(rs.getString(3)).append(",").append(rs.getString(4)).append(",");
					url=rs.getString(5);
				}
				url=finalPath+url;
				sb.append(url);
				response.getWriter().write(sb.toString());
			}
			rs.close();
			pstm.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
