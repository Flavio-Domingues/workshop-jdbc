package model.entities.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	private Connection conn;
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		try {
			String sql = "insert into department(name) VALUES (?)";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1,obj.getName());
			 int linhasAfetadas = ps.executeUpdate();
			 if(linhasAfetadas>0) {
				 ResultSet rs = ps.getGeneratedKeys();
				 if(rs.next()) {
					 int id = rs.getInt(1);
					 obj.setId(id);
				 }else {
					 throw new DbException(" Nenhuma linha inserida! ");
				 }
				 DB.closeResultSet(rs);
			 }
					      
		}catch (SQLException e) {
			throw new DbException(" erro inesperado! não inseriu "+e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
		
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE department SET Name=? where Id =?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,obj.getName());
			ps.setInt(2,obj.getId());
			ps.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException (" erro inesperado! não atualizou!"+e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM department where id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			
		}catch(Exception e) {
			throw new DbException(" deu erro:"+e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
		    st = conn.prepareStatement ("SELECT * FROM department where Id=?");
		    		
		    st.setInt(1, id);
		    rs = st.executeQuery();
		    if(rs.next()) {
		    	Department obj = new Department();
		    	obj.setId(rs.getInt("Id"));
		    	obj.setName(rs.getString("Name"));
		    	return obj;
		    	
		    }
		    return null;
		   
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Department> findALL() {
		PreparedStatement st = null;
		ResultSet rs = null;	
		try {
			st = conn.prepareStatement("SELECT *FROM department ORDER BY Name");
			rs = st.executeQuery();
			List<Department>list = new ArrayList<>();
			
			while(rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				list.add(obj);
				
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
     
	
}
