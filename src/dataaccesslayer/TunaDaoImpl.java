package dataaccesslayer;
/* File: TunaDaoImpl.java
 * Author: Stanley Pieda
 * Date: Sept, 2017
 * References:
 * Ram N. (2013). Data Access Object Design Pattern or DAO Pattern [blog] Retrieved from
 * http://ramj2ee.blogspot.in/2013/08/data-access-object-design-pattern-or.html
 */ 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datatransfer.Tuna;

/**
 * Partially complete implementation for DAO design pattern.
 * Has one insert method, and one find-by-UUID method.
 * @author Stanley Pieda
 */
public class TunaDaoImpl implements TunaDao{
	
	/** 
	 * Returns a reference to a Tuna object, loaded with data
	 * from the database, based on lookup using a UUID as String
	 * @param uuid String based UUID
	 * @return Tuna transfer object, or null if no match based on uuid found
	 * @throws SQLException
	 * @author Stanley Pieda
	 */
	@Override
	public Tuna getTunaByUUID(String uuid) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Tuna tuna = null;
		try{
			DataSource source = new DataSource();
			con = source.getConnection();
			pstmt = con.prepareStatement(
					"SELECT * FROM Tunas WHERE uuid = ?");
			pstmt.setString(1, uuid);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			tuna = new Tuna();
			tuna.setId(rs.getInt("id"));
			tuna.setRecordNumber(rs.getInt("recordnumber"));
			tuna.setOmega(rs.getString("omega"));
			tuna.setDelta(rs.getString("delta"));
			tuna.setTheta(rs.getString("theta"));
			tuna.setUUID(rs.getString("uuid"));
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
		finally{
			try{ if(rs != null){ rs.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(pstmt != null){ pstmt.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(con != null){ con.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
		}
		return tuna;
	}

    /**
	 * Accepts a Tuna object reference, inserting the encapsulated data into database.
	 * @param tuna with data for record insertion
	 * @throws SQLException
	 * @author Stanley Pieda
	 */
	public void insertTuna(Tuna tuna) throws SQLException{
    	DataSource source = new DataSource();
		Connection con = source.getConnection();
		PreparedStatement pstmt = null;
		try{
			pstmt = con.prepareStatement(
					"INSERT INTO Tunas (recordnumber, omega, delta, theta, uuid) " +
					"VALUES(?, ?, ?, ?, ?)");
			pstmt.setInt(1, tuna.getRecordNumber());
			pstmt.setString(2, tuna.getOmega());
			pstmt.setString(3, tuna.getDelta());
			pstmt.setString(4, tuna.getTheta());
			pstmt.setString(5, tuna.getUUID());
			pstmt.executeUpdate();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
		finally{
			try{ if(pstmt != null){ pstmt.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(con != null){ con.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
		}
	}

//	@Override
//	public List<Tuna> getAllTunas() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Tuna getTunaById(Integer tunaID) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void updateTuna(Tuna tuna) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void deleteTuna(Tuna tuna) {
//		// TODO Auto-generated method stub
//		
//	}
}
