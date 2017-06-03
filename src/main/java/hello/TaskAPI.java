package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import org.postgresql.ds.PGPoolingDataSource;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hello.DBUtil;
import hello.APIRequest;

@RestController
public class TaskAPI {

    /*API for listing tasks*/ 
	@RequestMapping(value="/toDOlist", method=RequestMethod.GET)
    public Map toDOlist(HttpServletRequest request,HttpServletResponse response) {
		List toRet = new ArrayList();
        Map ouput = new HashMap();
        Connection conn = DBUtil.getInstance().getDBConn();
        try{
            //Properties params = APIRequest.getInstance().getParams(request);
            String dateStr = request.getParameter("date");
            if(dateStr.equals("undefined") || dateStr == null){
                return ouput;
            }
            long date = Long.parseLong(dateStr);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM TODOLIST WHERE DATE="+date+";";
            ResultSet rs = stmt.executeQuery(sql); 
            while(rs.next()){
				Properties taskProps = new Properties();	
            	taskProps.put("ID",rs.getInt("ID"));
            	taskProps.put("taskName",rs.getString("TASK_NAME"));
            	taskProps.put("date",rs.getLong("DATE"));
            	taskProps.put("striked",rs.getBoolean("STRIKED"));
            	toRet.add(taskProps);
            }
            ouput.put("tasksData",toRet);
            int total = toRet.size();
            long percentage = 0;
            if(total > 0){
                String succSql = "SELECT COUNT(*) FROM TODOLIST WHERE DATE="+date+" AND STRIKED = "+true+";";
                rs = stmt.executeQuery(succSql);
                int s = 0;
                if(rs.next()){
                 s = rs.getInt("COUNT");
                }
                if(s > 0){
                    percentage = Math.round((s*100.00)/total);
                }
            }
            ouput.put("percentage",percentage);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        finally
        {
            if (conn != null)
            {
                try { conn.close(); } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		return ouput;
       
    }
    @RequestMapping(value="/addTask", method=RequestMethod.POST)
    public Properties addTask(HttpServletRequest request,HttpServletResponse response){
        Properties toRet = new Properties();
        Connection conn = DBUtil.getInstance().getDBConn();
        try{
            Properties params = APIRequest.getInstance().getParams(request);
            String task = (String)params.get("task");
            long date = (long)params.get("date");
            boolean striked = (boolean)params.get("striked");
            if(task != null)
            {
                if(conn == null)
                {
                    System.out.println("DB connection failed");
                    toRet.put("error","Unable to Connect to DB");
                    return toRet;
                }
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO TODOLIST(TASK_NAME,DATE,STRIKED) Values('"+task+"',"+date+","+false+");";
                stmt.execute(sql);
                toRet.put("result","Successfully inserted values in to DB");
                return toRet;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            toRet.put("error","Failed inserting values in to DB");
        }
        finally
        {
            if (conn != null)
            {
                try { conn.close(); } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    
        return toRet;
    }
        @RequestMapping(value="/strikeTask", method=RequestMethod.POST)
    public Properties strikeTask(HttpServletRequest request,HttpServletResponse response){
        Properties toRet = new Properties();
        Connection conn = DBUtil.getInstance().getDBConn();
        try{
            Properties params = APIRequest.getInstance().getParams(request);
            long taskID = (long)params.get("ID");
            long date = (long)params.get("date");
            boolean striked = !(boolean)params.get("striked");
            String sql = "Update TODOLIST SET STRIKED = "+striked+" WHERE ID = "+taskID+";";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            toRet.put("result","Successfully updated values in to DB");
            return toRet;
        }
        catch(Exception e){
            e.printStackTrace();
            toRet.put("error","Failed updated values in to DB");
        }
        finally
        {
            if (conn != null)
            {
                try { conn.close(); } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    
        return toRet;
    }
    @RequestMapping(value="/updateTask", method=RequestMethod.POST)
    public Properties updateTask(HttpServletRequest request,HttpServletResponse response){
        Properties toRet = new Properties();
        Properties params = APIRequest.getInstance().getParams(request);
        long taskID = (long)params.get("ID");
        String task = (String)params.get("taskName");
        Connection conn = DBUtil.getInstance().getDBConn();
        try
        {
            Statement stmt = conn.createStatement();
            String sql = "Update TODOLIST SET TASK_NAME =\'"+task+"\' WHERE ID = "+taskID+";";
            stmt.execute(sql);
            toRet.put("successMsg","Successfully updated values in to DB");
        }
        catch(Exception e){
            e.printStackTrace();
            toRet.put("errorMsg","Failed inserting values in to DB");
        }
        finally
        {
            if (conn != null)
            {
                try { conn.close(); } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    
        return toRet;
    }
}