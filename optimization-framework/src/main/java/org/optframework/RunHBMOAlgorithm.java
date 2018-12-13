package org.optframework;

import com.rits.cloning.Cloner;
import org.cloudbus.spotsim.enums.AZ;
import org.cloudbus.spotsim.enums.InstanceType;
import org.cloudbus.spotsim.enums.OS;
import org.cloudbus.spotsim.enums.Region;
import org.optframework.config.Config;
import org.optframework.core.*;
import org.optframework.core.hbmo.HBMOAlgorithm;
import org.optframework.core.hbmo.HBMOAlgorithmWithFullMutation;
import org.optframework.core.heft.HEFTAlgorithm;
import org.optframework.core.utils.PopulateWorkflow;
import org.optframework.core.utils.PreProcessor;
import org.optframework.core.utils.Printer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hessam Modabberi
 * @version 1.0.0
 */

public class RunHBMOAlgorithm {


    public static void runHBMO(){
        Log.logger.info("<<<<<<<<< HBMO Algorithm is started >>>>>>>>>");

        Workflow workflow = PreProcessor.doPreProcessing(PopulateWorkflow.populateWorkflowWithId(Config.global.budget, 0, Config.global.workflow_id));

        Cloner cloner = new Cloner();
        GlobalAccess.orderedJobList = cloner.deepClone(workflow.getJobList());
        Collections.sort(GlobalAccess.orderedJobList, Job.rankComparator);

        Config.global.m_number = GlobalAccess.maxLevel;

        honeyBeePreProcessing(workflow);

        Log.logger.info("Maximum number of instances: " + Config.global.m_number + " Number of different types of instances: " + InstanceType.values().length + " Number of tasks: "+ workflow.getJobList().size());
        Printer.printHoneBeeInfo();

        /**
         * Assumptions:
         * Region: europe
         * Availability Zone: A
         * OS type: Linux System
         * */
        InstanceInfo instanceInfo[] = InstanceInfo.populateInstancePrices(Region.EUROPE , AZ.A, OS.LINUX);

        workflow.setBeta(Beta.computeBetaValue(workflow, instanceInfo, Config.global.m_number));

        OptimizationAlgorithm optimizationAlgorithm;
        ArrayList<Solution> solutionList = new ArrayList<>();
        long runTimeSum = 0;

        /**
         * HEFT algorithm which is limited by m_number
         * */
        List<Job> orderedJobList = GlobalAccess.orderedJobList;

        double cost_fastest_instance = 0.9;
        int number_of_affordable_fastest_instance = (int)(Config.global.budget/cost_fastest_instance);
        if(number_of_affordable_fastest_instance > 0) {

            int totalInstances2[] = HEFTAlgorithm.getTotalInstancesForHEFTMostPowerful(number_of_affordable_fastest_instance);
            Workflow heftWorkflow2 = PreProcessor.doPreProcessingForHEFT(PopulateWorkflow.populateWorkflowWithId(Config.global.budget, 0, Config.global.workflow_id), Config.global.bandwidth, totalInstances2, instanceInfo);

            heftWorkflow2.setBeta(Beta.computeBetaValue(heftWorkflow2, instanceInfo, Config.global.m_number));

            HEFTAlgorithm heftAlgorithm2 = new HEFTAlgorithm(heftWorkflow2, instanceInfo, totalInstances2, Config.global.m_number);
            Solution heftSolution2 = heftAlgorithm2.runAlgorithm();
            heftSolution2.heftFitness();

            Integer zArray2[] = new Integer[orderedJobList.size()];
            for (int i = 0; i < orderedJobList.size(); i++) {
                zArray2[i] = orderedJobList.get(i).getIntId();
            }

            heftSolution2.zArray = zArray2;

            Printer.printSolutionWithouthTime(heftSolution2,instanceInfo);
        }

        number_of_affordable_fastest_instance++;

        int totalInstances3[] = HEFTAlgorithm.getTotalInstancesForHEFTMostPowerful(number_of_affordable_fastest_instance);

        Workflow heftWorkflow3 = PreProcessor.doPreProcessingForHEFT(PopulateWorkflow.populateWorkflowWithId(Config.global.budget, 0, Config.global.workflow_id), Config.global.bandwidth, totalInstances3, instanceInfo);

        heftWorkflow3.setBeta(Beta.computeBetaValue(heftWorkflow3, instanceInfo, Config.global.m_number));

        HEFTAlgorithm heftAlgorithm3 = new HEFTAlgorithm(heftWorkflow3, instanceInfo, totalInstances3, Config.global.m_number);
        Solution heftSolution3 = heftAlgorithm3.runAlgorithm();
        heftSolution3.heftFitness();

        Integer zArray3[] = new Integer[orderedJobList.size()];
        for (int i = 0; i < orderedJobList.size(); i++) {
            zArray3[i] = orderedJobList.get(i).getIntId();
        }

        heftSolution3.zArray = zArray3;

        for (int i = 0; i < Config.honeybee_algorithm.getNumber_of_runs(); i++) {
            Printer.printSplitter();
            Log.logger.info("<<<<<<<<<<<    NEW RUN "+ i +"     >>>>>>>>>>>\n");
            if (Config.honeybee_algorithm.getFull_mutation()){
                optimizationAlgorithm = new HBMOAlgorithmWithFullMutation(true, workflow, instanceInfo, Config.honeybee_algorithm.getGeneration_number(), heftSolution3);
            }else {
                optimizationAlgorithm = new HBMOAlgorithm(true, workflow, instanceInfo, Config.honeybee_algorithm.getGeneration_number(), heftSolution3);
            }
            long start = System.currentTimeMillis();

            Solution solution = optimizationAlgorithm.runAlgorithm();
            solution.solutionMapping();
            solution.fitness();
            solutionList.add(solution);

            long stop = System.currentTimeMillis();

            runTimeSum += (stop - start);

            Printer.lightPrintSolution(solution,stop-start);
            Printer.printSolutionWithouthTime(solution, instanceInfo);
        }

        double fitnessSum = 0.0, costSum = 0.0;
        double fitnessMax = 0.0, costMax = 0.0;
        double fitnessMin = 999999999999.9, costMin = 9999999999.9;
        Solution bestSolution = null;

        for (Solution solution: solutionList){
            fitnessSum += solution.fitnessValue;
            if (solution.fitnessValue > fitnessMax){
                fitnessMax = solution.fitnessValue;
            }
            if (solution.fitnessValue < fitnessMin){
                fitnessMin = solution.fitnessValue;
                bestSolution = solution;
            }

            costSum += solution.cost;
            if (solution.cost > costMax){
                costMax = solution.cost;
            }
            if (solution.cost < costMin){
                costMin = solution.cost;
            }
        }
        Printer.printSplitter();

        Printer.printSolutionWithouthTime(bestSolution, instanceInfo);

        //compute resource utilization
        double resourceUtilization[] = new double[bestSolution.numberOfUsedInstances];
        double onlyTaskUtilization[] = new double[bestSolution.numberOfUsedInstances];
        for (Job job : workflow.getJobList()){
            onlyTaskUtilization[bestSolution.xArray[job.getIntId()]] += job.getExeTime()[bestSolution.yArray[bestSolution.xArray[job.getIntId()]]];
        }

        for (int i = 0; i < bestSolution.numberOfUsedInstances; i++) {
            resourceUtilization[i] = onlyTaskUtilization[i] / bestSolution.instanceTimes[i];
        }
        Printer.printUtilization(resourceUtilization);

        Printer.printSplitter();

        String toPrint = "\n";
        toPrint += "Average Fitness value: " + fitnessSum / Config.honeybee_algorithm.getNumber_of_runs() + "\n";
        toPrint += "Average Cost value: " + costSum / Config.honeybee_algorithm.getNumber_of_runs() + "\n";
        toPrint += "Max fitness: " + fitnessMax + " Min fitness: "+ fitnessMin + "\n";
        Log.logger.info(toPrint);
        Printer.printHoneBeeInfo();
    }

    static void honeyBeePreProcessing(Workflow workflow){
        if (workflow.getJobList().size() >= 100){
            double kRandom = workflow.getJobList().size() * Config.honeybee_algorithm.getNeighborhood_ratio();
            Config.honeybee_algorithm.kRandom =  (int) kRandom;
        }else {
            Config.honeybee_algorithm.kRandom = workflow.getJobList().size()/3;
        }
    }
}
