package org.optframework.automator;

import org.optframework.*;
import org.optframework.config.Config;
import org.optframework.core.Log;
import org.optframework.core.Solution;
import org.optframework.core.utils.Printer;
import static org.optframework.automator.BudgetList.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Cost Automator (Automates cost setup)
 * This Utility class facilitates getting the result process
 * Automates budget based on Evolutionary Multi-Objective Workflow Scheduling in Cloud paper
 *
 * todo: remove main method and make it configurable through the config file
 * */

public class BudgetAutomator {

    public static Solution solution;
    public static long timeInMilliSec;
    //array of solutions which should be printed to csv file
    public static ArrayList<Solution> solutionArrayListToCSV;
    public static ArrayList<Long> timeInMilliSecArrayList;

    public static void main(String[] args) throws Exception{
        double budgetList[] = null;
        solutionArrayListToCSV = new ArrayList<>();
        timeInMilliSecArrayList = new ArrayList<>();
        GlobalAccess.solutionRepository = new ArrayList<>();

        Log.init();
        Log.logger.info("+++++++++ BudgetAutomator is started +++++++++");

        /**
         * Initializes Cloudsim Logger
         * */
        org.cloudbus.cloudsim.Log.init("cloudsim.log");

        org.cloudbus.spotsim.main.config.Config.load(null);

        /**
         * Loads configs from YAML file
         * */
        Config.initConfig();
        Printer.printSplitter();

        switch (Config.global.workflow_id){
            case 1: budgetList = inspiral1000; break;
            case 2: budgetList = inspiral100; break;
            case 10: budgetList = montage1000; break;
            case 11: budgetList = montage100; break;
            case 20: budgetList = sipht1000; break;
            case 21: budgetList = sipht100; break;
            case 30: budgetList = epigenomics997; break;
            case 31: budgetList = epigenomics100; break;
            case 40: budgetList = cybershake1000; break;
            case 41: budgetList = cybershake100; break;
        }

        for (double budget: budgetList){
            Config.global.budget = budget;
//            switch (Config.global.algorithm){
//                case "sa": RunSAAlgorithm.runSA(); break;
//                case "hbmo": RunHBMOAlgorithm.runHBMO(); break;
//                case "heft": RunHEFTAlgorithm.runSingleHEFT(); break;
//                case "hbmo-heft": RunHEFTWithHBMO.runHEFTWithHBMO();break;
//                case "heft-example": RunHEFTExample.runHEFTExample();break;
//                case "pacsa": RunPACSAAlgorithm.runPACSA(0);break;
//                case "pacsa-plus": RunPACSAAlgorithm.runPACSA(1);break;
//                case "pso": RunPSOAlgorithm.runPSO(0);break;
//                case "zpso": RunPSOAlgorithm.runPSO(1);break;
//                case "iterative-grp-heft": RunIterativeGRPHEFTAlgorithm.runGRPHEFT();break;
//                case "grp-heft": RunGRPHEFTAlgorithm.runGRPHEFT();break;
//                case "grp-pacsa": RunGRPPACSAAlgorithm.runGRPPACSA();break;
//            }
            RunGRPPACSAAlgorithm.runGRPPACSA();
            timeInMilliSecArrayList.add(timeInMilliSec);
            solutionArrayListToCSV.add(solution);
        }

        try (PrintWriter writer = new PrintWriter(new File("cost-automator.csv"))) {

            StringBuilder sb = new StringBuilder();
            sb.append("Budget");
            sb.append(',');
            sb.append("Cost");
            sb.append(',');
            sb.append("Makespan");
            sb.append(',');
            sb.append("Fitness");
            sb.append(',');
            sb.append("MilliSec");
            sb.append(',');
            sb.append("Sec");
            sb.append('\n');

            for (int i = 0; i < solutionArrayListToCSV.size(); i++) {
                sb.append(budgetList[i]);
                sb.append(',');
                sb.append(solutionArrayListToCSV.get(i).cost);
                sb.append(',');
                sb.append(solutionArrayListToCSV.get(i).makespan);
                sb.append(',');
                sb.append(solutionArrayListToCSV.get(i).fitnessValue);
                sb.append(',');
                sb.append(timeInMilliSecArrayList.get(i));
                sb.append(',');
                sb.append(timeInMilliSecArrayList.get(i)/1000);
                sb.append('\n');
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
