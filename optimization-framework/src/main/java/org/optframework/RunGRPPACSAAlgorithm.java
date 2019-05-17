package org.optframework;

import com.rits.cloning.Cloner;
import org.cloudbus.spotsim.enums.AZ;
import org.cloudbus.spotsim.enums.InstanceType;
import org.cloudbus.spotsim.enums.OS;
import org.cloudbus.spotsim.enums.Region;
import org.optframework.config.Config;
import org.optframework.core.*;
import org.optframework.core.pacsa.PACSAIterationNumber;
import org.optframework.core.pacsa.PACSAOptimization;
import org.optframework.core.utils.PopulateWorkflow;
import org.optframework.core.utils.PreProcessor;
import org.optframework.core.utils.Printer;
import org.optframework.database.MySQLSolutionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunGRPPACSAAlgorithm {

    public static void runGRPPACSA(){
        {
            RunGRPHEFTAlgorithm.runGRPHEFT();
            Solution grpHEFTSolution = GlobalAccess.latestSolution;

            Log.logger.info("<<<<<<<<< GRP-PACSA Algorithm is started >>>>>>>>>");
            /**
             * Assumptions:
             * Region: europe
             * Availability Zone: A
             * OS type: Linux System
             * */
            InstanceInfo instanceInfo[] = InstanceInfo.populateInstancePrices(Region.EUROPE , AZ.A, OS.LINUX);
            Workflow workflow = PreProcessor.doPreProcessing(PopulateWorkflow.populateWorkflowWithId(Config.global.budget, 0, Config.global.workflow_id));

            Cloner cloner = new Cloner();
            GlobalAccess.orderedJobList = cloner.deepClone(workflow.getJobList());
            Collections.sort(GlobalAccess.orderedJobList, Job.rankComparator);

            Config.global.m_number = grpHEFTSolution.numberOfUsedInstances;

            computeCoolingFactorForSA(workflow.getJobList().size());
            workflow.setBeta(Beta.computeBetaValue(workflow, instanceInfo, Config.global.m_number));

            OptimizationAlgorithm optimizationAlgorithm;

            List<Solution> solutionList = new ArrayList<>();
            List<Solution> initialSolutionList = new ArrayList<>();

            long runTimeSum = 0;
            initialSolutionList.add(grpHEFTSolution);

            for (int i = 0; i < Config.pacsa_algorithm.getNumber_of_runs(); i++) {
                Printer.printSplitter();
                Log.logger.info("<<<<<<<<<<<    NEW RUN "+ i +"     >>>>>>>>>>>\n");

                Log.logger.info("Maximum number of instances (m_number): " + Config.global.m_number + " Number of different types of instances: " + InstanceType.values().length + " Number of tasks: "+ workflow.getJobList().size());

                if (Config.pacsa_algorithm.iteration_number_based){
                    optimizationAlgorithm = new PACSAIterationNumber(initialSolutionList, 1.0/(10.0*(double)grpHEFTSolution.makespan), workflow, instanceInfo, Config.global.m_number);
                }else {
                    optimizationAlgorithm = new PACSAOptimization(initialSolutionList ,(1.0/4.0*(double)grpHEFTSolution.makespan),workflow, instanceInfo, Config.global.m_number);
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
            toPrint += "Average Fitness value: " + fitnessSum / Config.pacsa_algorithm.getNumber_of_runs() + "\n";
            toPrint += "Average Cost value: " + costSum / Config.pacsa_algorithm.getNumber_of_runs() + "\n";
            toPrint += "Max fitness: " + fitnessMax + " Min fitness: "+ fitnessMin + "\n";
            Log.logger.info(toPrint);

            if (Config.global.use_mysql_to_log){
                MySQLSolutionRepository sqlSolutionRepository = new MySQLSolutionRepository();
                sqlSolutionRepository.updateRecord(
                        fitnessMin,
                        fitnessMax,
                        fitnessSum / Config.pacsa_algorithm.getNumber_of_runs(),
                        costSum / Config.pacsa_algorithm.getNumber_of_runs(),
                        (runTimeSum / Config.pacsa_algorithm.getNumber_of_runs())/1000);

                Log.logger.info("Successfully logged to the mysql database!");
            }
        }
    }

    static void computeCoolingFactorForSA(int numberOfTasks){
        if (!Config.sa_algorithm.force_cooling){
            if (numberOfTasks >= 10){
                Config.sa_algorithm.cooling_factor = 1 - 1 / (double)numberOfTasks;
            }else {
                Config.sa_algorithm.cooling_factor = 0.9;
            }
        }
    }
}
