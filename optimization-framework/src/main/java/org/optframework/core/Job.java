package org.optframework.core;

import java.io.Serializable;

/**
 * @author Hessam Modaberi
 * @since 2018
 * */

public class Job implements Serializable {

    private static final long serialVersionUID = 4123274113987701274L;

    private long id;

    private double weight;

    private double finishTime;

    private double exeTime[];

    public Job(long id, double weight, double finishTime, double[] exeTime) {
        this.id = id;
        this.weight = weight;
        this.finishTime = finishTime;
        this.exeTime = exeTime;
    }

    public int getIntId() {
        return (int)this.id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }
}
