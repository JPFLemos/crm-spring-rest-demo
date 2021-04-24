package com.jp.springrestcrm.service;

import java.util.List;

import com.jp.springrestcrm.entity.Customer;

public interface CustomerService {

	public List<Customer> getCustomers();
	
	public void saveCustomer(Customer customer);

	public Customer getCustomer(int id);
	
	public void deleteCustomer(int id);
	
}
