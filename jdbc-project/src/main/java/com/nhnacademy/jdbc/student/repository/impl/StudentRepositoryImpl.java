package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class StudentRepositoryImpl {
    public void save(Student studentDto){

        String sql = String.format("insert into jdbc_students(id,name,gender,age) values('%s','%s','%s',%d)",
                studentDto.getId(),
                studentDto.getName(),
                studentDto.getGender(),
                studentDto.getAge()
        );

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.info("result:{}",result);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}