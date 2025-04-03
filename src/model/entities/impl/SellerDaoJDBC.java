package model.entities.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		try {
			String sql = "insert into seller"
					+"(Name,Email,BirthDate,BaseSalary,"
					+ "DepartmentId) values(?,?,?,?,?)";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
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
			
		}catch(SQLException e) {
			throw new DbException (" erro inesperado! não inseriu"+e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE seller SET Name=?, Email=?,BirthDate=?, BaseSalary=?, DepartmentId=? where Id=? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
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
    	  String sql = "DELETE FROM seller where id=?";     
    	 ps = conn.prepareStatement(sql); 
    	 ps.setInt(1,id);
         ps.executeUpdate();
	  }catch(Exception e) {
		throw new DbException("deu erro:"+e.getMessage());
	}finally {
		DB.closeStatement(ps);
	}	
		
}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select seller.*,department.Name as depName from seller inner join department on (seller.DepartmentId=Department.id) where seller.Id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Department dep = instatiateDepartment(rs);
				Seller obj = intantiateSeller(rs, dep);
				obj.setDepartment(dep);

				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}

	}

	private Seller intantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		return obj;
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findALL() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select seller. *,department.Name as depName  from seller inner join department on(seller.DepartmentId=department.Id)order by seller.Name asc";
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instatiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = intantiateSeller(rs, dep);
				obj.setDepartment(dep);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select seller. *,department.Name as depName  from seller inner join department on(seller.DepartmentId=department.Id) where Department.Id=? order by seller.Name asc";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instatiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = intantiateSeller(rs, dep);
				obj.setDepartment(dep);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
