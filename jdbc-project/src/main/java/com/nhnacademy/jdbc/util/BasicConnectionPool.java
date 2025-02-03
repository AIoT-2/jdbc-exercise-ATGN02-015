package com.nhnacademy.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class BasicConnectionPool  {
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int maximumPoolSize;
    private final Queue<Connection> connections;

    public BasicConnectionPool(String driverClassName, String jdbcUrl, String username, String password, int maximumPoolSize)  {

        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.maximumPoolSize = maximumPoolSize;
        connections = new LinkedList<>();

        checkDriver(driverClassName);
        initialize();
    }

    private void checkDriver(String driverClassName){
        //todo#1 driverClassName에 해당하는 class가 존재하는지 check합니다.
        //존재하지 않는다면 RuntimeException 예외처리
        Class clazz = null;

        try {
            clazz = Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("driverClassName에 해당하는 클래스가 존재하지 않음");
        }
    }

    private void initialize(){
        //todo#2 maximumPoolSize만큼 Connection 객체를 생성해서 Connection Pool에 등록합니다.
        for (int i = 0; i < maximumPoolSize; i ++) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(jdbcUrl, username, password);
//                connections.add(connection);
                connections.offer(connection);
                // add는 LinkedList에서 추가하려 할 때 공간이 부족하면 예외 발생
                // offer도 add와 같은 추가 방식이지만 실패하면 예외 대신 false를 반환 -> 안전한 추가 방식
            } catch (SQLException e) {
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConnection() throws InterruptedException {
        //todo#3 Connection Pool에 connection이 존재하면 반환하고 비어있다면 Connection Pool에 Connection이 존재할 때 까지 대기합니다.
//        while (connections.isEmpty()) {
//            wait();
//        }
//
//        return connections.poll();

        synchronized (this) {
            // this는 이 메서드가 속한 객체 인스턴스를 의미
            // getConnection() 메서드가 정의된 객체를 락으로 사용
            while (connections.isEmpty()) {
                // connection 리스트가 비어있으면 대기
                wait();
            }
            return connections.poll();
            // connection 객체를 FIFO 형식으로 하나 가져옴
        }
    }

    public void releaseConnection(Connection connection) {
        //todo#4 작업을 완료한 Connection 객체를 Connection Pool에 반납 합니다.
//        synchronized (this) {
//            connections.offer(connection);
//        }

        synchronized (this) {
            connections.offer(connection);
            notifyAll();
            // 작업하던 connection 객체의 작업이 완료했기 때문에 반납하고 wait로 대기하고 있는 모든 스레드를 깨움
            // synchronized로 인해 여러 개의 스레드가 깨어나지만 락을 얻은 하나의 스레드만 실행이 가능
            // 나머지 스레드는 다시 wait 상태가 됨s
        }
    }

    public int getUsedConnectionSize(){
        //todo#5 현재 사용중인 Connection 객체 수를 반환합니다.
//        return maximumPoolSize - connections.size();

        synchronized (this) {
            return this.maximumPoolSize - connections.size();
        }
        // connection pool에서 꺼내고 반납할 때 데이터 일관성에 어긋나지 않도록 동기화 같이 사용
    }

    public void distory() throws SQLException {
        // 실행 종료 시 모든 Connection을 닫아주는 것이 좋음
        // 리소스 누수 방지, 애플리케이션 종료 시 정리, DB 서버 연결 제한 문제 등을 위함
        // connection pool을 돌면서 모든 connection 객체를 닫음
        //todo#6 Connection Pool에 등록된 Connection을 close 합니다.
        for (Connection connection : connections) {
            if (!connection.isClosed()) {
                connection.close();
            }
        }

    }
}