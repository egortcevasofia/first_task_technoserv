import repository.EmployeeRepositoryImpl;
import repository.MyValidator;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        EmployeeRepositoryImpl employeeRepository = new EmployeeRepositoryImpl();

        try {
            System.out.println( "Входящие данные валидны?: " + MyValidator.isValid(Path.of("C://employees.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            employeeRepository.findCombination();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
