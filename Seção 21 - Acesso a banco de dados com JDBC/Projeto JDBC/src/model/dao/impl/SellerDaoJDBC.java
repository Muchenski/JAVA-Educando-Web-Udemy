package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private Connection conn = null;
	private PreparedStatement st = null;
	private ResultSet rs = null;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
		try {
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void insert(Seller seller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {

		nullCheck(id);

		try {

			st = conn.prepareStatement(
					"SELECT seller.*, department.name as depName " + "FROM seller " + "INNER JOIN department "
							+ "ON seller.departmentId = department.id " + "WHERE seller.id = ?",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				return instantiateSeller(instantiateDepartment());
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}
	}

	@Override
	public List<Seller> findAll() {

		try {

			List<Seller> sellers = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			st = conn.prepareStatement(
					"SELECT seller.*, department.name as depName " + "FROM seller INNER JOIN department "
							+ "ON seller.departmentId = department.Id " + "ORDER BY seller.name;");

			rs = st.executeQuery();

			Department dep;
			while (rs.next()) {

				dep = map.get(rs.getInt("departmentId"));

				if (dep == null) {
					dep = instantiateDepartment();
					map.put(rs.getInt("departmentId"), dep);
				}

				sellers.add(instantiateSeller(dep));
			}
			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		nullCheck(department);

		try {

			List<Seller> sellers = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			st = conn.prepareStatement("SELECT seller.*, department.name as depName "
					+ "FROM seller INNER JOIN department " + "ON seller.departmentId = department.Id "
					+ "WHERE departmentId = ? " + "ORDER BY seller.name;");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			Department dep;
			while (rs.next()) {

				dep = map.get(rs.getInt("departmentId"));

				if (dep == null) {
					dep = instantiateDepartment();
					map.put(rs.getInt("departmentId"), dep);
				}

				sellers.add(instantiateSeller(dep));
			}
			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}
	}

	// Como este � apenas um m�todo auxiliar, e o m�todo que ir� utiliz�-lo
	// j� realiza o tratamento de uma poss�vel SQLException,
	// iremos propagar(throws) a poss�vel exce��o.
	private Department instantiateDepartment() throws SQLException {
		return new Department(rs.getInt("departmentId"), rs.getString("depName"));
	}

	// Como este � apenas um m�todo auxiliar, e o m�todo que ir� utiliz�-lo
	// j� realiza o tratamento de uma poss�vel SQLException,
	// iremos propagar(throws) a poss�vel exce��o.
	private Seller instantiateSeller(Department department) throws SQLException {

		try {

			return new Seller(rs.getInt(1), rs.getString("name"), rs.getString("email"),
					sdf.parse(rs.getString("birthDate")), rs.getDouble("baseSalary"), department);

		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		}
	}

	private void makeRollBack() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	private void nullCheck(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

}
