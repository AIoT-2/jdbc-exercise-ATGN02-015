package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){ // print 또는 log로 확인하기 위한 용도, void로 실행 상관 X
        String sql = "insert into jdbc_students(id,name,gender,age) values(?,?,?,?)";
        // 데이터베이스에 입력할 sql 문

        try(Connection connection = DbUtils.getConnection();
            // 데이터베이스 서버?와 연결
            PreparedStatement statement = connection.prepareStatement(sql);
            // sql 문이 데이터베이스에 전달돼 미리 컴파일
            // insert 자체가 실행되는 것이 아닌 sql 문이 준비만 되는 것
        ){
            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString());
            statement.setInt(4,student.getAge());
            // Student 객체 네 가지 set을 다 설정해줌

            int result = statement.executeUpdate();
            // executeUpdate 메서드로 실제 데이터베이스에 데이터가 삽입됨
            log.debug("save:{}",result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id){
    // Optional은 null 값을 안전하게 처리하기 위한 용도, null 체크 쉬움
    // Student로 return을 받을 때 null이면 호출하는 쪽에서 null 체크를 계속 해서 Exception을 방지해야 함
        String sql = "select * from jdbc_students where id=?";
        log.debug("findById:{}",sql);

        ResultSet rs = null;
        // ResultSet은 sql 조회 결과를 표 형태로 저장하는 객체
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1,id);
            rs = statement.executeQuery();
            // sql 문을 실행해서 ResultSet 형태인 rs 변수에 저장
            if(rs.next()){ // 다음 행(데이터)가 있다면
            // next는 ResultSet이 표 형태로 저장되어 다음 행을 순회할 때 사용
                Student student =  new Student(rs.getString("id"),
                        rs.getString("name"),
                        Student.GENDER.valueOf(rs.getString("gender")),
                        rs.getInt("age"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                // 찾고자 하는 Student 객체의 정보를 가져와 새로운 Student 객체에 저장

                return Optional.of(student);
                // Optional<Student> 를 return 받아야 하기 때문에 of로 감싸서 반환(null 체크 위해)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
                // try-with-resources로 쓰지 않는 이유는 prepareStatement보다 먼저 닫힐 수 있음
                // 일찍 닫히거나 잘못 닫힐 수 있음 -> finally로 쓰는 게 좋음?
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
        // 값이 없으면 null을 반환
    }

    @Override
    public int update(Student student){
        String sql = "update jdbc_students set name=?, gender=?, age=? where id=?";
        // set으로 바인딩 할 값들을 미리 sql 문으로 작성
        log.debug("update:{}",sql);

        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            int index=0;
            statement.setString(++index, student.getName());
            statement.setString(++index, student.getGender().toString());
            statement.setInt(++index, student.getAge());
            statement.setString(++index, student.getId());
            // 1번째 값부터 4번째 인덱스 값으로 각각 Student의 정보를 가져옴

            int result = statement.executeUpdate();
            // sql 문 실행
            log.debug("result:{}",result);

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
        String sql = "delete from jdbc_students where id=?";

        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, id);
            // 입력받은 id를 sql에 대입
            int result = statement.executeUpdate();
            // sql문 실행
            log.debug("result:{}",result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}