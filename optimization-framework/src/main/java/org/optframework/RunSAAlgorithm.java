package org.optframework;

import org.cloudbus.cloudsim.util.workload.Workflow;
import org.cloudbus.spotsim.enums.AZ;
import org.cloudbus.spotsim.enums.InstanceType;
import org.cloudbus.spotsim.enums.OS;
import org.cloudbus.spotsim.enums.Region;
import org.cloudbus.spotsim.main.config.Config;
import org.cloudbus.spotsim.pricing.PriceRecord;
import org.cloudbus.spotsim.pricing.SpotPriceHistory;
import org.cloudbus.spotsim.pricing.db.PriceDB;
import org.optframework.config.StaticProperties;
import org.optframework.core.*;
import org.optframework.core.sa.SimulatedAnnealingAlgorithm;
import org.optframework.core.utils.PopulateWorkflow;
import org.optframework.core.utils.Printer;

/**
 * @author Hessam Modabberi hessam.modaberi@gmail.com
 * @version 1.0.0
 */

public class RunSAAlgorithm implements StaticProperties {

    public static void main( String[] args ) throws Exception
    {
        Log.init();
        Log.logger.info("<<<<<<<<< SA Algorithm is started >>>>>>>>>");

        /**
         * Initializes Cloudsim Logger
         * */
        org.cloudbus.cloudsim.Log.init("cloudsim.log");

        Log.logger.info("Loads configs");
        Config.load(null);

        Workflow workflow = PopulateWorkflow.populateWorkflowFromDax(1000, 0);
        Log.logger.info("Maximum number of instances: " + M_NUMBER + " Number of different types of instances: " + N_TYPES + " Number of tasks: "+ workflow.getJobList().size());

        /**
         * Assumptions:
         * Region: europe
         * Availability Zone: A
         * OS type: Linux System
         * */
        InstanceInfo instanceInfo[] = populateInstancePrices(Region.EUROPE , AZ.A, OS.LINUX);

        workflow.setBeta(Beta.computerBetaValue(workflow, instanceInfo, M_NUMBER));

        SimulatedAnnealingAlgorithm saAlgorithm = new SimulatedAnnealingAlgorithm(workflow, instanceInfo);

        long start = System.currentTimeMillis();

        Solution solution = saAlgorithm.runAlgorithm();

        long stop = System.currentTimeMillis();

        Printer.printSolution(solution, instanceInfo,stop-start);
    }

    private static InstanceInfo[] populateInstancePrices(Region region , AZ az, OS os){
        Log.logger.info("Loads spot prices history");
        SpotPriceHistory priceTraces = PriceDB.getPriceTrace(region , az);
        InstanceInfo info[] = new InstanceInfo[InstanceType.values().length];

        for (InstanceType type: InstanceType.values()){
            PriceRecord priceRecord = priceTraces.getNextPriceChange(type,os);
            InstanceInfo instanceInfo = new InstanceInfo();
            instanceInfo.setSpotPrice(priceRecord.getPrice());
            instanceInfo.setType(type);

            info[type.getId()] = instanceInfo;
        }
        return info;
    }
}
