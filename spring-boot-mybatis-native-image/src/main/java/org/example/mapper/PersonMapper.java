package org.example.mapper;

import org.example.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author lst
 * @date 2023/11/17 14:43
 * @return null
 */
@Component
public interface PersonMapper {

    List<Person> findAll();

}
