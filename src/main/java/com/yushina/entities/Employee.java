package com.yushina.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthday;

    @ElementCollection
    private List<String> hobbies;

    public void updateEmployee(Employee employeeWithNewInfo) {
        if (employeeWithNewInfo != null) {
            this.setBirthday(employeeWithNewInfo.getBirthday());
            this.setEmail(employeeWithNewInfo.getEmail());
            this.setHobbies(employeeWithNewInfo.getHobbies());
            this.setFullName(employeeWithNewInfo.getFullName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(email, employee.email)
                && Objects.equals(fullName, employee.fullName) && Objects.equals(birthday, employee.birthday)
                && areListsEqual(hobbies, employee.hobbies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, fullName, birthday, hobbies);
    }

    public static boolean areListsEqual(List<?> list1, List<?> list2) {
        return list1.size() == list2.size() &&
                IntStream.range(0, list1.size())
                        .allMatch(i -> Objects.equals(list1.get(i), list2.get(i)));

    }
}
