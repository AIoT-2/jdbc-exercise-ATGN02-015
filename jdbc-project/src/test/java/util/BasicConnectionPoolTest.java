package util;

import com.nhnacademy.jdbc.util.BasicConnectionPool;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class BasicConnectionPoolTest {

    static BasicConnectionPool basicConnectionPool;
    static Connection connection1;
    static Connection connection2;
    static Connection connection3;

    static Connection connection4;
    static Connection connection5;

    @BeforeAll
    static void setUp() {
        basicConnectionPool = new BasicConnectionPool(com.mysql.cj.jdbc.Driver.class.getName(),"jdbc:mysql://220.67.216.14:13306/nhn_academy_215","nhn_academy_215","yQ4vfzap!",5);
    }

    @AfterAll
    static void connectionClose() throws SQLException {
        basicConnectionPool.distory();
    }

    @Test
    @Order(1)
    @DisplayName("get connection")
    void getConnection() throws InterruptedException, SQLException {
        connection1 = basicConnectionPool.getConnection();
        connection2 = basicConnectionPool.getConnection();
        connection3 = basicConnectionPool.getConnection();

        int usedConnections = basicConnectionPool.getUsedConnectionSize();
        System.out.println("현재 사용 중인 Connection 개수: " + usedConnections);

        Assertions.assertAll(
                ()->Assertions.assertTrue(connection1.isValid(1000)),
                ()->Assertions.assertTrue(connection2.isValid(1000)),
                ()->Assertions.assertTrue(connection3.isValid(1000)),
                ()->Assertions.assertEquals(basicConnectionPool.getUsedConnectionSize(),3)
        );
    }

    @Disabled
    // connection6을 연결하려 하면 wait가 걸려서 실행이 끝나지 않음
    @Test
    @Order(2)
    @DisplayName("empty connection-pool")
    void getConnection_empty() throws InterruptedException, SQLException {
        connection4 = basicConnectionPool.getConnection();
        connection5 = basicConnectionPool.getConnection();
        Connection connection6 = basicConnectionPool.getConnection();
        // maximum pool size가 5

        int usedConnections = basicConnectionPool.getUsedConnectionSize();
        System.out.println("현재 사용 중인 Connection 개수: " + usedConnections);

        Assertions.assertAll(
                ()->Assertions.assertEquals(basicConnectionPool.getUsedConnectionSize(),5)
        );

    }

    @Test
    @Order(3)
    @DisplayName("release connection")
    void releaseConnection() {
        basicConnectionPool.releaseConnection(connection1);
        basicConnectionPool.releaseConnection(connection2);
        basicConnectionPool.releaseConnection(connection3);

        Assertions.assertEquals(basicConnectionPool.getUsedConnectionSize(),0);
    }

}