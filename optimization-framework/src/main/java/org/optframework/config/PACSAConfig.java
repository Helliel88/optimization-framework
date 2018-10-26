package org.optframework.config;

public class PACSAConfig {
    public int iteration_number;
    public boolean iteration_number_based;
    public int number_of_ants;
    public int number_of_runs;
    public double cf_increase_ratio;
    public double temp_decrease_ratio;
    public double equilibrium_point;
    public double evaporation_factor;
    public boolean m_number_from_heft;
    public boolean global_based;
    public boolean compute_m_number_from_budget;

    public int getNumber_of_ants() {
        return number_of_ants;
    }

    public void setNumber_of_ants(int number_of_ants) {
        this.number_of_ants = number_of_ants;
    }

    public int getNumber_of_runs() {
        return number_of_runs;
    }

    public void setNumber_of_runs(int number_of_runs) {
        this.number_of_runs = number_of_runs;
    }

    public double getCf_increase_ratio() {
        return cf_increase_ratio;
    }

    public void setCf_increase_ratio(double cf_increase_ratio) {
        this.cf_increase_ratio = cf_increase_ratio;
    }

    public double getTemp_decrease_ratio() {
        return temp_decrease_ratio;
    }

    public void setTemp_decrease_ratio(double temp_decrease_ratio) {
        this.temp_decrease_ratio = temp_decrease_ratio;
    }

    public double getEquilibrium_point() {
        return equilibrium_point;
    }

    public void setEquilibrium_point(double equilibrium_point) {
        this.equilibrium_point = equilibrium_point;
    }

    public double getEvaporation_factor() {
        return evaporation_factor;
    }

    public void setEvaporation_factor(double evaporation_factor) {
        this.evaporation_factor = evaporation_factor;
    }

    public boolean isM_number_from_heft() {
        return m_number_from_heft;
    }

    public void setM_number_from_heft(boolean m_number_from_heft) {
        this.m_number_from_heft = m_number_from_heft;
    }

    public boolean isGlobal_based() {
        return global_based;
    }

    public void setGlobal_based(boolean global_based) {
        this.global_based = global_based;
    }

    public boolean isCompute_m_number_from_budget() {
        return compute_m_number_from_budget;
    }

    public void setCompute_m_number_from_budget(boolean compute_m_number_from_budget) {
        this.compute_m_number_from_budget = compute_m_number_from_budget;
    }

    public int getIteration_number() {
        return iteration_number;
    }

    public void setIteration_number(int iteration_number) {
        this.iteration_number = iteration_number;
    }

    public boolean isIteration_number_based() {
        return iteration_number_based;
    }

    public void setIteration_number_based(boolean iteration_number_based) {
        this.iteration_number_based = iteration_number_based;
    }
}