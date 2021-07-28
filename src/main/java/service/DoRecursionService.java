package service;

import domain.Department;
import domain.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoRecursionService {
    public ReadAndValidateService readAndValidateService = new ReadAndValidateService();
    private Comparator<Department> comparator = (d1, d2) -> d1.getAverageSalary().compareTo(d2.getAverageSalary());

    public void findAllPossibleCombination(String pathForRead, String pathForWrite) {
        Map<String, Department> validDepartments = readAndValidateService.readFromFile(pathForRead);
        System.out.println(validDepartments);
        List<Department> listOfDepartments = new ArrayList<>(validDepartments.values());
        listOfDepartments.sort(comparator);

        for (int i = 0; i < listOfDepartments.size(); i++) {
            for (int j = i + 1; j < listOfDepartments.size(); j++) {
                writeAllPossiibleCombinationToFile(listOfDepartments.get(i), listOfDepartments.get(j), pathForWrite);
            }
        }
    }

    private void writeAllPossiibleCombinationToFile(Department lessDepartment, Department moreDepartment, String pathForWrite) {
        try (FileWriter writer = new FileWriter(pathForWrite, true)) {
            BigDecimal bigSalary = moreDepartment.getAverageSalary();
            BigDecimal smallSalary = lessDepartment.getAverageSalary();
            writer.write(String.format("Возможен перевод из департамента " + moreDepartment.getName() + " в департамент "
                    + lessDepartment.getName() + ", где средние зарплаты: " + bigSalary.toString()
                    + " и " + smallSalary.toString() + " соответственно, следующих сотрудников: \n"));

            ArrayList<Employee> listOfEmployee = moreDepartment
                    .getEmployeeList()
                    .stream()
                    .filter(e -> e.getSalary().compareTo(bigSalary) < 0)
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<ArrayList<Employee>> listOfCombination = combination(0, listOfEmployee,/////Рекурсивная функция
                    new ArrayList<>(), new ArrayList<>());

            for (int i = 0; i < listOfCombination.size(); i++) {
                System.out.println(listOfCombination.get(i));//Для отладки
            }

            for (int i = 0; i < listOfCombination.size(); i++) {
                ArrayList<Employee> oneListCombination = listOfCombination.get(i);
                if (bigSalary.compareTo(getSum(oneListCombination)) > 0
                        && getSum(oneListCombination).compareTo(smallSalary) > 0) {

                    writer.write(lineForWrite(oneListCombination, moreDepartment, lessDepartment));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    private String lineForWrite(ArrayList<Employee> oneListCombination, Department moreDepartment, Department lessDepartment) {
        BigDecimal bigSalary = moreDepartment.getSumSalary();
        BigDecimal smallSalary = lessDepartment.getSumSalary();

        BigDecimal newBigAverageSalary = (bigSalary.subtract(getSum(oneListCombination)))
                .divide(new BigDecimal(moreDepartment.getEmployeeList().size() - 1), 2, RoundingMode.HALF_UP);

        BigDecimal newSmallAverageSalary = (smallSalary.add(getSum(oneListCombination)))
                .divide(new BigDecimal(lessDepartment.getEmployeeList().size() + 1), 2, RoundingMode.HALF_UP);

        String lineForWrite = oneListCombination.toString() + " from " + moreDepartment.getName()
                + " to " + lessDepartment.getName()
                + " после перевода среднии зарплаты составлят соответственно, \n"
                + newBigAverageSalary.toString() + " и " + newSmallAverageSalary.toString() + "\n";
        return lineForWrite;
    }

    private BigDecimal getSum(ArrayList<Employee> listOfCombination) {
        BigDecimal result = new BigDecimal(BigInteger.ZERO);
        for (int i = 0; i < listOfCombination.size(); i++) {
            result = result.add(listOfCombination.get(i).getSalary());
        }
        return result;
    }


    private static ArrayList<ArrayList<Employee>> combination(int count, ArrayList<Employee> listOfEmployee,
                                                              ArrayList<Employee> list, ArrayList<ArrayList<Employee>> finalList) {
        for (int i = 0; i < listOfEmployee.size(); i++) {
            if (list.contains(listOfEmployee.get(i))) break;
            ArrayList<Employee> newList = new ArrayList<>();
            newList.add(listOfEmployee.get(i));
            newList.addAll(list);
            finalList.add(newList);

            if (count < listOfEmployee.size() - 1) {
                ArrayList<Employee> list1 = new ArrayList<>(list);
                list1.add(listOfEmployee.get(i));

                combination(count + 1, listOfEmployee, list1, finalList);
            }
        }

        return finalList;

    }


}
