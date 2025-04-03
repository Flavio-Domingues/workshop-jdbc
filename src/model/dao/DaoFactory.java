package model.dao;

import db.DB;
import model.entities.impl.DepartmentDaoJDBC;
import model.entities.impl.SellerDaoJDBC;

public class DaoFactory {
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
		
	}
   public static DepartmentDao createDepartmentDao() {
	   return new DepartmentDaoJDBC(DB.getConnection());
	   
   }
}
