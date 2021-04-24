package com.jp.springrestcrm.dao;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jp.springrestcrm.entity.Customer;

@Repository
public class CustomerDaoImpl implements CustomerDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Customer> getCustomers() {

		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//create a query and sort by Last Name
		Query<Customer> query = currentSession.createQuery("from Customer order by lastName", Customer.class);
		
		//execute the query and get the result list
		List<Customer> customers = query.getResultList();
		
		return customers;
		
		
	}

	@Override
	public void saveCustomer(Customer customer) {
		//get current hibernate session 
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save the customer
		currentSession.saveOrUpdate(customer);
		

	}

	@Override
	public Customer getCustomer(int id) {
		//get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// retrieve from database using primary key
		Customer customer = currentSession.get(Customer.class, id);
		
		return customer;
	}

	@Override
	public void deleteCustomer(int id) {
		//get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object using primary key
		Query<Customer> query = currentSession.createQuery("delete from Customer where id=:customerId");
		query.setParameter("customerId", id);
		
		query.executeUpdate();

	}

}
