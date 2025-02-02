package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
    // userId와 userPassword로 User를 조회
        String sql = String.format("select user_id, user_name, user_password from jdbc_users where user_id='%s' and user_password='%s' ", userId, userPassword);
        // 파라미터로 입력 받은 userId, userPassword를 sql문으로 작성
        // 간단한 sql 실행에는 적합하지만 sql Injection에 취약하고, sql이 매번 새롭게 파싱되어 성능이 저하됨
        // -> Statement statement = connection.createStatement()로 이렇게 쓰는 것보다 connection.preparedStatement() 사용이 좋음
        log.debug("sql:{}",sql);

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            // select 문을 실행한 결과를 rs에 저장
        ) {
            if(rs.next()){
            // 초기 ResultSet의 커서는 테이블 밖(테이블 바로 앞)에 위치함
            // 그래서 시작부터 next를 해야 처음 조회된 데이터가 들어옴
            // 열 이름에는 커서가 들어가지 않음
                return Optional.of(
                        new User(rs.getString("user_id"),rs.getString("user_name"),rs.getString("user_password"))
                // select문 실행한 결과로 새로운 User 객체를 만들어 반환
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
    // userId로 User 조회
        String sql = String.format("select user_id, user_name, user_password from jdbc_users where user_id='%s'", userId);

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
        ) {
            if(rs.next()){
                return Optional.of(
                        new User(rs.getString("user_id"),rs.getString("user_name"),rs.getString("user_password"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(User user) {
    // User 객체를 파라미터로 받아서 id, name, password의 정보까지 데이터베이스에 저장
        String sql = String.format("insert into jdbc_users (user_id, user_name, user_password) values ('%s','%s','%s')",
                user.getUserId(),
                user.getUserName(),
                user.getUserPassword()
        );

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("save-result:{}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
    // id와 password를 입력받아 id에 해당되는 User 객체의 password를 입력받은 password로 변경
        String sql = String.format("update jdbc_users set user_password='%s' where user_id='%s'",
                userPassword,
                userId
        );
        log.debug("sql:{}",sql);
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("updateUserPasswordByUserId : {}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
    // 입력받은 id에 해당하는 User 객체 정보를 삭제
        String sql = String.format("delete from jdbc_users where user_id='%s'", userId);
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("result:{}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
