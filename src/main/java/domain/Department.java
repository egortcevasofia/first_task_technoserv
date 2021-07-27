package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Department{
    private String name;
    private List<Employee> employeeList;

    public Department(String name) {
        this.name = name;
    }

    public Department(String name, List<Employee> employeeList) {
        this.name = name;
        this.employeeList = employeeList;
    }

    public Department() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", employeeList=" + employeeList +
                '}';
    }

    public BigDecimal getSumSalary(){
        BigDecimal summ = BigDecimal.ZERO;
        for (Employee e : employeeList) {
            summ = summ.add(e.getSalary());
        }
        return summ;
    }

    public BigDecimal getAverageSalary() {
        BigDecimal summ = getSumSalary();
        return summ.divide(new BigDecimal(employeeList.size()), 2, RoundingMode.HALF_UP);
    }


}
