package service;

import domain.Department;
import domain.Employee;


import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadAndValidateService {

    private static final String PATH_TO_FILE = "C://employees.txt";
    private static final String PATH_TO_COMBINATION = "C://Users//segortseva//combination.txt";


    public Map<String, Department> readFromFile(String path) {
        Map<String, Department> mapOfDepartment = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            int numberOfLine = 0;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(", ");
                if (isValid(words, ++numberOfLine)) {
                    String name = words[0];
                    String department = words[1];
                    BigDecimal salary = new BigDecimal(words[2]);
                    mapOfDepartment.computeIfPresent(department, (key, value) -> value = addToList(department, (ArrayList<Employee>) value.getEmployeeList(), new Employee(name, salary)));
                    mapOfDepartment.putIfAbsent(department, new Department(department, new ArrayList<Employee>(Arrays.asList(new Employee(name, salary)))));
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла проблема при прочтении файла");
        }
        return mapOfDepartment;
    }

    private Department addToList(String department, ArrayList<Employee> list, Employee employee) {
        list.add(employee);
        return new Department(department, list);
    }

    private Boolean isValid(String[] words, int numberOfLine) {
        try {
            if (words.length != 3)
                throw new Exception(String.format("В строке %s более или менее 3-х параметров", numberOfLine));
            if (words[0].trim().isEmpty()) throw new Exception(String.format("В строке %s Имя пустое", numberOfLine));
            if (words[1].trim().isEmpty())
                throw new Exception(String.format("В строке %s Департамент пустой", numberOfLine));
            if (new BigDecimal(words[2]).scale() != 2)
                throw new Exception(String.format("В строке %s После запятой не два знака", numberOfLine));
        } catch (NumberFormatException e) {
            System.out.println(String.format("В строке %s в поле зарплата записано не числовое значение", numberOfLine));
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}




