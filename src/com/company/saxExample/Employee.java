package com.company.saxExample;

public class Employee {
    private String name;
    private String job;

    public Employee(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return "name: " + name + "; job: " + job;
    }
}
