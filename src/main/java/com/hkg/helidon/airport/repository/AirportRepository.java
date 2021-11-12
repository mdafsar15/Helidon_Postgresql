package com.hkg.helidon.airport.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.hkg.helidon.airport.enitity.Airport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class AirportRepository {
	//JDBC Connection
	public static String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	public static String userName = "root";
	public static String password = "afsaralam15";
	static Connection con = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public Airport createOrUpdate(Airport airport) {
		if (airport.getId() == null) {
			this.entityManager.persist(airport);
			return airport;
		} else {
			return this.entityManager.merge(airport);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaDelete<Airport> delete = cb.createCriteriaDelete(Airport.class);
		Root<Airport> root = delete.from(Airport.class);
		delete.where(cb.equal(root.get("id"), id));
		this.entityManager.createQuery(delete).executeUpdate();
	}

	public List<Airport> getAllAirports() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Airport> cq = cb.createQuery(Airport.class);
		Root<Airport> rootEntry = cq.from(Airport.class);
		CriteriaQuery<Airport> all = cq.select(rootEntry);
		TypedQuery<Airport> allQuery = entityManager.createQuery(all);
		return allQuery.getResultList();

	}

	// JDBC
	public static Airport insert(Airport airport) {

		String qry = "insert into public.airport(id,apt_code, apt_name, city_name, country) values(?,?,?,?,?)";

		try {
			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Driver Class Loaded");

			con = DriverManager.getConnection(jdbcURL, userName, password);

			System.out.println("Connetion Establish with db server");

			pstmt = con.prepareStatement(qry);
			System.out.println("Platform Created");

			pstmt.setLong(1, airport.getId());

			pstmt.setString(2, airport.getAirportCode());

			pstmt.setString(3, airport.getAirportName());

			pstmt.setString(4, airport.getCityName());

			pstmt.setString(5, airport.getCountryName());

			pstmt.executeUpdate();
			System.out.println("Congrats! Data Inserted Successfully!!!!");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return airport;

	}

}
