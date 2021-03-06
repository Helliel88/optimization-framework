package org.optframework;

import com.rits.cloning.Cloner;
import org.cloudbus.spotsim.enums.AZ;
import org.cloudbus.spotsim.enums.InstanceType;
import org.cloudbus.spotsim.enums.OS;
import org.cloudbus.spotsim.enums.Region;
import org.optframework.config.Config;
import org.optframework.core.InstanceInfo;
import org.optframework.core.Log;
import org.optframework.core.Solution;
import org.optframework.core.grpheft.GRPHEFTAlgorithm;
import org.optframework.core.utils.Printer;

import java.util.ArrayList;

/**
 * Iterative version of the GRP Modified-HEFT Algorithm
 * @author Hessam hessam.mdoaberi@gmail.com
 * */

public class RunIterativeGRPHEFTAlgorithm {
    public static boolean thisTypeIsUsedAsMaxEfficient[];

    public static void runGRPHEFT(){
        Log.logger.info("<<<<<<<<< GRP-HEFT Algorithm is started >>>>>>>>>");

        /**
         * Assumptions:
         * Region: europe
         * Availability Zone: A
         * OS type: Linux System
         * */
        InstanceInfo instanceInfo[] = InstanceInfo.populateInstancePrices(Region.EUROPE , AZ.A, OS.LINUX);

        long start = System.currentTimeMillis();
        ArrayList<Solution> solutionArrayList = new ArrayList<>();
        Cloner cloner = new Cloner();
        InstanceInfo originalInstanceInfo[] = cloner.deepClone(instanceInfo);
        InstanceInfo tempInstanceInfo[] = instanceInfo;
        RunIterativeGRPHEFTAlgorithm.thisTypeIsUsedAsMaxEfficient = new boolean[instanceInfo.length];

        for (int i = 0; i < InstanceType.values().length; i++) {
            Log.logger.info("Run " + i);
            GRPHEFTAlgorithm grpheftAlgorithm = new GRPHEFTAlgorithm(tempInstanceInfo);
            Solution solution = grpheftAlgorithm.runAlgorithm();
            solutionArrayList.add(solution);

            int maxInstanceId = findFastestInstanceId(instanceInfo);

            int k =0;
            tempInstanceInfo = new InstanceInfo[instanceInfo.length-1];
            for (int j = 0; j < instanceInfo.length; j++) {
                if (maxInstanceId != instanceInfo[j].getType().getId()){
                    tempInstanceInfo[k] = instanceInfo[j];
                    k++;
                }
            }
            instanceInfo = tempInstanceInfo;
        }

        double temp = 99999999999999.9;
        Solution finalSolution = null;
        for (Solution solution : solutionArrayList){
            if (solution.fitnessValue < temp){
                temp = solution.fitnessValue;
                finalSolution = solution;
            }
        }

        finalSolution.origin = "grp-heft";

        long end = System.currentTimeMillis();
        Log.logger.info("<<<<<<<<< GRP Final Result >>>>>>>>>");
        Printer.printSolutionWithouthTime(finalSolution, originalInstanceInfo);

        if (Config.global.algorithm.equals("iterative-grp-heft")){
            GlobalAccess.solutionArrayListToCSV.add(finalSolution);
            GlobalAccess.timeInMilliSecArrayList.add(end - start);
            GlobalAccess.solutionRepository.add(finalSolution);
        }
        GlobalAccess.latestSolution = finalSolution;
    }

    public static int findFastestInstanceId(InstanceInfo instanceInfo[]){
        InstanceInfo temp = instanceInfo[0];
        for (InstanceInfo info: instanceInfo){
            if (info.getType().getEcu() > temp.getType().getEcu()){
                temp = info;
            }
        }
        return  temp.getType().getId();
    }
}
