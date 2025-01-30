import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.impl.StudentRepositoryImpl;
import org.junit.jupiter.api.*;

import java.util.Random;

class StudentRepositoryTest {

    public static StudentRepositoryImpl studentRepository;

    @BeforeAll
    static void setUp(){
        studentRepository = new StudentRepositoryImpl();
    }

    @Test
    @DisplayName("save 10 students")
    void save() {
        Random random = new Random();
        for(int i=1; i<=10; i++){
            String id="marco" + i;
            String name="마르코" + i;
            Student.GENDER gender = Student.GENDER.M;
            int age = random.nextInt(50);
            Student student = new Student(id,name,gender,age);

            Assertions.assertDoesNotThrow(()->{
                studentRepository.save(student);
            });
        }
    }

}