package org.example.service.Impl;

import org.example.mapper.PersonMapper;
import org.example.model.Person;
import org.example.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonServiceImpl implements PersonService {
	private final static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Autowired
	private PersonMapper dao;

	public List<Person> selectAll() {
		return dao.findAll();
	}


}
