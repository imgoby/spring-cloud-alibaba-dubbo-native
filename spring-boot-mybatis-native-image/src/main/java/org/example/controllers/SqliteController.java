package org.example.controllers;

import org.example.model.Person;
import org.example.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author lst
 * @date 2023/11/17 14:43
 * @return null
 */
@RestController
@RequestMapping("/person")
public class SqliteController {

    @Autowired
    PersonService personService;

    @RequestMapping("/list")
    public List<Person> listPerson() {
        List<Person> list = personService.selectAll();
        return list;
    }


}
