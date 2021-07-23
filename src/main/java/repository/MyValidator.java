package repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class MyValidator {

    public static Boolean isValid(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] words = line.split(", ");
            isValidString(words);
            isValidData(words);
        }
        return true;

    }

    private static Boolean isValidString(String[] words) {

        if (words.length != 3) {
            throw new RuntimeException("Документ оформлен не в соответствии с требованиями");
        } else {
            return true;
        }
    }

    private static Boolean isValidData(String[] words) {
        String name = words[0];
        String department = words[1];
        BigDecimal salary = BigDecimal.valueOf(Double.parseDouble(words[2]));

        if (name.length() <= 2 | name.contains("  ")) {
            throw new RuntimeException("Имя сотрудника не валидно");
        }

        if (department.length() <= 1 | department.contains(" ")) {
            throw new RuntimeException("Название департамента не валидно");
        }

        if (salary.scale() > 2) {
            throw new RuntimeException("Количество знаков после запятой в поле зарплаты не равно двум");
        }

        return true;

    }
}
