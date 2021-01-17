package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn = null;
	private PreparedStatement st = null;
	private ResultSet rs = null;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
		try {
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void insert(Department department) {

		nullCheck(department);

		try {

			st = conn.prepareStatement("INSERT INTO department(name) VALUES(?);", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, department.getName());
			int rowsAffected = st.executeUpdate();

			conn.commit();

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);

					// Em mem�ria, o objeto ainda n�o sabe o pr�prio id
					// por isso devemos setar ao inserir.
					department.setId(id);
					System.out.println("CREATED - " + department);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			makeRollBack();
			throw new DbException(e.getMessage());

		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}
	}

	@Override
	public void update(Department department) {

		nullCheck(department);

		try {

			st = conn.prepareStatement("UPDATE department SET name = ? WHERE id = ?;");

			st.setString(1, department.getName());
			st.setInt(2, department.getId());
			int rowsAffected = st.executeUpdate();

			conn.commit();

			if (rowsAffected > 0) {
				System.out.println("UPDATED - " + department);
			} else {
				System.out.println("Update fail!");
			}

		} catch (SQLException e) {
			makeRollBack();
			throw new DbException(e.getMessage());

		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}

	}

	@Override
	public void deleteById(Integer id) {

		nullCheck(id);

		try {

			st = conn.prepareStatement("DELETE FROM department WHERE id = ?;");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();

			conn.commit();

			if (rowsAffected > 0) {
				System.out.println("DELETED - " + id);
			} else {
				System.out.println("Delete fail!");
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			makeRollBack();
			throw new DbIntegrityException(e.getMessage());

		} catch (SQLException e) {
			makeRollBack();
			throw new DbException(e.getMessage());

		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}

	}

	@Override
	public Department findById(Integer id) {

		nullCheck(id);

		try {

			st = conn.prepareStatement("SELECT * FROM department WHERE id = ?;");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				return new Department(rs.getInt(1), rs.getString("name"));
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
		}

	}

	@Override
	public List<Department> findAll() {
		try {

			List<Department> departments = new ArrayList<Department>();

			st = conn.prepareStatement("SELECT * FROM department;");
			rs = st.executeQuery();

			while (rs.next()) {
				departments.add(new Department(rs.getInt("id"), rs.getString("name")));
			}

			return departments;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeSettings(Arrays.asList(st, rs));
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

		if (object != null) {
			if (object instanceof Department) {
				if (((Department) object).getName() == null) {
					throw new IllegalArgumentException("Invalid Department!");
				}
			}
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

}
