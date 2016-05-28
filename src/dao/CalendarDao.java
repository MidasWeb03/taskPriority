package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import conn.conn2DB;
import dto.CalendarDto;
import dto.Dto;
import dto.MemberDto;
import dto.TaskDto;

public class CalendarDao implements Dao{
	private conn2DB c2db;
	private static CalendarDao calDao;
	public static Dao getInstance(){
		if(calDao == null){
			calDao = new CalendarDao();
		}
		return (Dao)calDao;
	}
	private CalendarDao() {
		// TODO Auto-generated constructor stub
		c2db = new conn2DB();
	}
	// create
	public boolean addCalendar(Dto dto, String cname, String[] fList){
		String sql1 = "insert into challenge_db.Calendar "
				+ " (cname) "
				+ " values(?) ";
		String sql2 = "select last_insert_id()";
		String sql3 = "insert into challenge_db.calChecked "
				+ " (Memail, cid)"
				+ " values(?, ?)";
		MemberDto memdto = (MemberDto)dto;
		Connection conn = null;
		PreparedStatement psmt1 = null,psmt2 = null,psmt3 = null;
		int cnt=0;
		ResultSet rs;
		try{
			conn = c2db.getConnection();
			psmt1 = conn.prepareStatement(sql1);
			psmt1.setString(1, cname);
			cnt = psmt1.executeUpdate();
			if(cnt==0) return false;
			psmt2 = conn.prepareStatement(sql2);
			rs = psmt2.executeQuery();
			if(rs.next()){
				psmt3 = conn.prepareStatement(sql3);
				System.out.println(memdto.getEmail());
				psmt3.setString(1, memdto.getEmail());
				psmt3.setInt(2, rs.getInt("last_insert_id()"));
				cnt = psmt3.executeUpdate();
				if(cnt==0) return false;
				if(fList != null){
					for(int i=0;i<fList.length;i++){
						psmt3.setString(1, fList[i]);
						cnt = psmt3.executeUpdate();
						if(cnt==0) return false;
					}
				}
			}
		} catch(Exception e) {
			log("an error from [CalendarDao.addCalendar()]", e);
		} finally {
			c2db.close(conn, psmt1, null);
			c2db.close(null, psmt2, null);
			c2db.close(null, psmt3, null);
		}
		return true;
	}
	public boolean addTuple(Dto dto){
		String sql = " insert into challenge_db.task "
				+ " (cid, taskName, startDate,endDate,description,priority, color) "
				+ " values(?, ?, ?, ?, ?, ?, ?) ";
		Connection conn = null;
		PreparedStatement psmt = null;
		TaskDto taskdto = (TaskDto)dto; 
		boolean result = false;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, taskdto.getCid());
			psmt.setString(2, taskdto.getTaskName());
			psmt.setString(3, taskdto.getStartDate());
			psmt.setString(4, taskdto.getEndDate());
			psmt.setString(5, taskdto.getDescription());
			psmt.setInt(6, taskdto.getPriority());
			psmt.setString(7, taskdto.getColor());
			result = psmt.execute();
		} catch (Exception e){
			log("an error from [CalendarDao.addTuple()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		if(result) 	return true;
		else		return false;
	}
	// delete
	public boolean deleteChecked(int cid){
		String sql = "delete from challenge_db.Calendar where cid=?";
		Connection conn = null;
		PreparedStatement psmt = null;
		boolean result;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, cid);
			result = psmt.execute();
			if(!result) return false; 
		} catch(Exception e) {
			log("an error from [CalendarDao.deleteChecked()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		return true;
	}
	public boolean deleteCalendar(CalendarDto caldto){
		String sql = "delete from challenge_db.Calendar where cid=?";
		Connection conn = null;
		PreparedStatement psmt = null;
		boolean result;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, caldto.getCid());
			result = psmt.execute();
			if(!result) return false; 
		} catch(Exception e) {
			log("an error from [CalendarDao.deleteCalendar()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		return true;
	}
	public boolean deleteTuple(Dto dto){
		String sql = "delete from challenge_db.task where tid = ?";
		Connection conn = null;
		PreparedStatement psmt = null;
		TaskDto taskdto = (TaskDto)dto;
		boolean result;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, taskdto.getTid());
			result = psmt.execute();
			if(!result) return false; 
		} catch(Exception e) {
			log("an error from [CalendarDao.deleteTuple()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		return true;
	}
	// update
	public boolean updateTuple(Dto dto){
		String sql = "update challenge_db.task set";
		int tid=0, priority=0;
		String tname=null, sdate=null, edate=null, color=null, description=null;
		Connection conn = null;
		PreparedStatement psmt = null;
		TaskDto taskdto = (TaskDto)dto;
		tid = taskdto.getTid();
		if(tid == 0) return false;
		if((priority=taskdto.getPriority()) != 0) sql += " priority=? ";
		if((tname=taskdto.getTaskName()) != null) sql += " taskname=? ";
		if((sdate=taskdto.getStartDate()) != null) sql += " startdate=? ";
		if((edate=taskdto.getEndDate()) != null) sql += " enddate=? ";
		if((color=taskdto.getColor()) != null) sql += "color=? ";
		if((description=taskdto.getDescription()) != null) sql += " description=? ";
		sql += " where tid=?";
		boolean result = false;
		int idx=1;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			if(priority != 0) psmt.setInt(idx++, priority);
			if(tname != null) psmt.setString(idx++, tname);
			if(sdate != null) psmt.setString(idx++, sdate);
			if(edate != null) psmt.setString(idx++, edate);
			if(color != null) psmt.setString(idx++, color);
			if(description != null) psmt.setString(idx++, description);
			psmt.setInt(idx, tid);
			result = psmt.execute();
		} catch(Exception e) {
			log("an error from [CalendarDao.updateTuple()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		if(result){
			return true;
		} else {
			return false;
		}
	}
	public Dto readTuple(Dto dto){
		String sql = "select * from challenge_db.task"
				+ " where tid = ?";
		Connection conn = null;
		PreparedStatement psmt = null;
		int tid=0, cid=0,priority=0;
		String tname=null, sdate=null, edate=null, wdate=null,color=null, description=null;
		TaskDto memdto = (TaskDto)dto;
		ResultSet rs;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, memdto.getTid());
			rs = psmt.executeQuery();
			if(rs.next()){
				tid = rs.getInt("tid");
				cid = rs.getInt("cid");
				priority = rs.getInt("priority");
				tname = rs.getString("taskname");
				sdate = rs.getString("startdate");
				edate = rs.getString("enddate");
				wdate = rs.getString("writedate");
				color = rs.getString("color");
				description = rs.getString("description");
				return (Dto)(new TaskDto(tid, cid, priority, tname,sdate, edate, wdate, color, description));
			}
		} catch(Exception e) {
			log("an error from [MemberDao.deleteTuple()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		return null;
	}
	// read
	public List<Dto> readTuples(Dto dto){
		String sql = "select * from challenge_db.task"
				+ " where cid = ?";
		Connection conn = null;
		PreparedStatement psmt = null;
		int tid=0, cid=0,priority=0;
		String tname=null, sdate=null, edate=null, wdate=null,color=null, description=null;
		CalendarDto caldto = (CalendarDto)dto;
		List<Dto> taskList = new ArrayList<Dto>();
		ResultSet rs;
		try{
			conn = c2db.getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, caldto.getCid());
			rs = psmt.executeQuery();
			while(rs.next()){
				tid = rs.getInt("tid");
				cid = rs.getInt("cid");
				priority = rs.getInt("priority");
				tname = rs.getString("taskname");
				sdate = rs.getString("startdate");
				edate = rs.getString("enddate");
				wdate = rs.getString("writedate");
				color = rs.getString("color");
				description = rs.getString("description");
				TaskDto taskdto =new TaskDto(tid, cid, priority, tname,sdate, edate, wdate, color, description);
				taskList.add((Dto)taskdto);
			}
		} catch(Exception e) {
			log("an error from [MemberDao.deleteTuple()]", e);
		} finally {
			c2db.close(conn, psmt, null);
		}
		return taskList;
	}
		// please use dto as MemberDto param
	public List<Dto> readAllTuple(Dto dto){
		List<Dto> dtoList = new ArrayList<Dto>();
		MemberDto memdto = (MemberDto)dto;
		ResultSet rs1, rs2;
		Connection conn = null;
		PreparedStatement psmt1 = null, psmt2 = null;
		int cid;
		String sql1 = "select cid, onOff "
				+ "from challenge_db.Member as m, challenge_db.calchecked as c "
				+ "where m.email = c.MEmail and m.email = ? order by cid";
		String sql2 = "select cname "
				+ "from challenge_db.calendar as c "
				+ "where c.cid = ?";
		try{
			conn = c2db.getConnection();
			psmt1 = conn.prepareStatement(sql1);
			psmt1.setString(1, memdto.getEmail());
			rs1 = psmt1.executeQuery();
			while(rs1.next()){
				psmt2 = conn.prepareStatement(sql2);
				psmt2.setInt(1, rs1.getInt("cid"));
				rs2 = psmt2.executeQuery();
				System.out.println("2");
				if(rs2.next()){
					System.out.println(3);
					CalendarDto caldto = 
							new CalendarDto(rs1.getInt("cid"), 1, rs2.getString("cname"), rs1.getBoolean("onOff"));
					dtoList.add((Dto)caldto);
				}
			}
		} catch (Exception e){
			log("an error from [MemberDao.readAllTuple()]", e);
		} finally {
			c2db.close(conn, psmt1, null);
			c2db.close(null, psmt2, null);
		}
		return dtoList;
	}
	public int getCurCid(List<Dto> dtolist){
		if(dtolist == null) return 0;
		for(int i=0; i<dtolist.size();i++){
			CalendarDto caldto = (CalendarDto)dtolist.get(i);
			if(caldto.getIsActive()) return caldto.getCid();
		}
		return 0;
	}
	// log
	public void log(String str){
		System.out.println(str);
	}
	public void log(String str, Exception e){
		System.out.print(str+" ");
		e.printStackTrace();
	}
}
