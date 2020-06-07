package org.optframework.config;

public class Automator {
    public String type;
    public Integer number_of_runs;
    public Integer number_of_initial_solutions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumber_of_runs() {
        return number_of_runs;
    }

    public void setNumber_of_runs(Integer number_of_runs) {
        this.number_of_runs = number_of_runs;
    }

    public Integer getNumber_of_initial_solutions() {
        return number_of_initial_solutions;
    }

    public void setNumber_of_initial_solutions(Integer number_of_initial_solutions) {
        this.number_of_initial_solutions = number_of_initial_solutions;
    }
}
