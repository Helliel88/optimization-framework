package org.optframework.core;

import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.cloudsim.util.workload.Workflow;
import org.cloudbus.cloudsim.workflow.Models.DAX.Dax2Workflow;
import org.cloudbus.spotsim.main.config.SimProperties;

public class PopulateWorkflow {
    private static Workflow populateWorkflowFromDax(double budget, long deadline) {
        Log.logger.info("Populates the workflow from Dax file");

        Dax2Workflow dax = new Dax2Workflow();
        dax.processDagFile(SimProperties.WORKFLOW_FILE_DAG.asString()
                , 1, 100,0);

        Workflow workflow = dax.workflow;
        workflow.initBudget(budget);
        workflow.setDeadline(deadline);

        return workflow;
    }

    public static Workflow populateSimpleWorkflow(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(6, 1000, 1000);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->B, A->C, B->D, B->E, D->F, E->F, C->F
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 3600 , userID, groupID, 3600, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 1200 , userID, groupID, 1200, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfC = new Job(taskID, submitTime, 22000 , userID, groupID, 22000, numProc);
        simpleWorkflow.createTask(wfC);
        taskID++;

        Job wfD = new Job(taskID, submitTime, 8600 , userID, groupID, 8600, numProc);
        simpleWorkflow.createTask(wfD);
        taskID++;

        Job wfE = new Job(taskID, submitTime, 2200 , userID, groupID, 2200, numProc);
        simpleWorkflow.createTask(wfE);
        taskID++;

        Job wfF = new Job(taskID, submitTime, 11000, userID, groupID, 11000, numProc);
        simpleWorkflow.createTask(wfF);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfB, 0);
        simpleWorkflow.addEdge(wfA, wfC, 0);
        simpleWorkflow.addEdge(wfB, wfD, 0);
        simpleWorkflow.addEdge(wfB, wfE, 0);
        simpleWorkflow.addEdge(wfD, wfF, 0);
        simpleWorkflow.addEdge(wfE, wfF, 0);
        simpleWorkflow.addEdge(wfC, wfF, 0);

        return simpleWorkflow;
    }

    public static Workflow populateSimpleWorkflow2(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(5, 1000, 2);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->B B->D, B->E, D->F, E->F
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 300 , userID, groupID, 300, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 10 , userID, groupID, 10, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfD = new Job(taskID, submitTime, 50 , userID, groupID, 50, numProc);
        simpleWorkflow.createTask(wfD);
        taskID++;

        Job wfE = new Job(taskID, submitTime, 100 , userID, groupID, 100, numProc);
        simpleWorkflow.createTask(wfE);
        taskID++;

        Job wfF = new Job(taskID, submitTime, 40 , userID, groupID, 40, numProc);
        simpleWorkflow.createTask(wfF);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfB, 0);
        simpleWorkflow.addEdge(wfB, wfD, 0);
        simpleWorkflow.addEdge(wfB, wfE, 0);
        simpleWorkflow.addEdge(wfD, wfF, 0);
        simpleWorkflow.addEdge(wfE, wfF, 0);

        return simpleWorkflow;
    }

    public static Workflow populateSimpleWorkflow3(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(5, deadline, budget);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->B B->D, B->E, D->F, E->F
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 1200 , userID, groupID, 1200, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 2100 , userID, groupID, 2100, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfD = new Job(taskID, submitTime, 5600 , userID, groupID, 5600, numProc);
        simpleWorkflow.createTask(wfD);
        taskID++;

        Job wfE = new Job(taskID, submitTime, 5500 , userID, groupID, 5500, numProc);
        simpleWorkflow.createTask(wfE);
        taskID++;

        Job wfF = new Job(taskID, submitTime, 2300 , userID, groupID, 2300, numProc);
        simpleWorkflow.createTask(wfF);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfB, 0);
        simpleWorkflow.addEdge(wfB, wfD, 0);
        simpleWorkflow.addEdge(wfB, wfE, 0);
        simpleWorkflow.addEdge(wfD, wfF, 0);
        simpleWorkflow.addEdge(wfE, wfF, 0);

        return simpleWorkflow;
    }

    public static Workflow populateSimpleWorkflow4(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(5, 1000, 1000);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->D A->C, B->D, C->E, D->E
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 50 , userID, groupID, 50, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 100 , userID, groupID, 100, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfC = new Job(taskID, submitTime, 30 , userID, groupID, 30, numProc);
        simpleWorkflow.createTask(wfC);
        taskID++;

        Job wfD = new Job(taskID, submitTime, 40 , userID, groupID, 40, numProc);
        simpleWorkflow.createTask(wfD);
        taskID++;

        Job wfE = new Job(taskID, submitTime, 10 , userID, groupID, 10, numProc);
        simpleWorkflow.createTask(wfE);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfC, 0);
        simpleWorkflow.addEdge(wfA, wfD, 0);
        simpleWorkflow.addEdge(wfB, wfD, 0);
        simpleWorkflow.addEdge(wfC, wfE, 0);
        simpleWorkflow.addEdge(wfD, wfE, 0);

        return simpleWorkflow;
    }

    public static Workflow populateSimpleWorkflow5(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(3, 1000, 1000);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->C B->C
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 3000 , userID, groupID, 3000, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 10000 , userID, groupID, 10000, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfC = new Job(taskID, submitTime, 50000 , userID, groupID, 50000, numProc);
        simpleWorkflow.createTask(wfC);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfC, 0);
        simpleWorkflow.addEdge(wfB, wfC, 0);

        return simpleWorkflow;
    }

    public static Workflow populateSimpleWorkflow6(double budget, long deadline){
        Log.logger.info("Populates the workflow from the simple workflow");

        Workflow simpleWorkflow = new Workflow(10, 1000, 1000);

        simpleWorkflow.initBudget(budget);
        simpleWorkflow.setDeadline(deadline);

        /**
         * Defining a simple workflow A->B, A->C, B->D, B->E, D->F, E->F, C->F
         * */

        int taskID = 0;

        int groupID = 1;
        int userID = 1;
        long submitTime = 0 ;
        int numProc = 1;

        Job wfA = new Job(taskID, submitTime, 3600 , userID, groupID, 3600, numProc);
        simpleWorkflow.createTask(wfA);
        taskID++;

        Job wfB = new Job(taskID, submitTime, 1200 , userID, groupID, 1200, numProc);
        simpleWorkflow.createTask(wfB);
        taskID++;

        Job wfC = new Job(taskID, submitTime, 22000 , userID, groupID, 22000, numProc);
        simpleWorkflow.createTask(wfC);
        taskID++;

        Job wfD = new Job(taskID, submitTime, 8600 , userID, groupID, 8600, numProc);
        simpleWorkflow.createTask(wfD);
        taskID++;

        Job wfE = new Job(taskID, submitTime, 2200 , userID, groupID, 2200, numProc);
        simpleWorkflow.createTask(wfE);
        taskID++;

        Job wfF = new Job(taskID, submitTime, 11000, userID, groupID, 11000, numProc);
        simpleWorkflow.createTask(wfF);
        taskID++;

        Job wfG = new Job(taskID, submitTime, 8000, userID, groupID, 8000, numProc);
        simpleWorkflow.createTask(wfG);
        taskID++;

        Job wfH = new Job(taskID, submitTime, 5600, userID, groupID, 5600, numProc);
        simpleWorkflow.createTask(wfH);
        taskID++;

        Job wfI = new Job(taskID, submitTime, 17000, userID, groupID, 17000, numProc);
        simpleWorkflow.createTask(wfI);
        taskID++;

        Job wfJ = new Job(taskID, submitTime, 1200, userID, groupID, 1200, numProc);
        simpleWorkflow.createTask(wfJ);
        taskID++;

        simpleWorkflow.addEdge(wfA, wfB, 0);
        simpleWorkflow.addEdge(wfA, wfC, 0);
        simpleWorkflow.addEdge(wfB, wfD, 0);
        simpleWorkflow.addEdge(wfB, wfE, 0);
        simpleWorkflow.addEdge(wfC, wfF, 0);
        simpleWorkflow.addEdge(wfF, wfH, 0);
        simpleWorkflow.addEdge(wfF, wfI, 0);
        simpleWorkflow.addEdge(wfD, wfG, 0);
        simpleWorkflow.addEdge(wfE, wfG, 0);
        simpleWorkflow.addEdge(wfG, wfJ, 0);
        simpleWorkflow.addEdge(wfH, wfJ, 0);
        simpleWorkflow.addEdge(wfI, wfJ, 0);

        return simpleWorkflow;
    }
}
