/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.core;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Log;

/**
 * A Cloud Information Service (GIS) is an entity that provides grid resource
 * registration, indexing and discovery services. The Cloud hostList tell their
 * readiness to process Cloudlets by registering themselves with this entity,
 * done via {@link gridsim.CloudResource#startEntity()} method.
 * <p>
 * Other entities such as the resource broker can contact this class for
 * resource discovery service, which returns a list of registered resource IDs.
 * In summary, it acts like a yellow page service.
 * <p>
 * This class will be created by CloudSim upon initialisation of the simulation,
 * i.e. done via {@link gridsim.CloudSim#init(int, Calendar, boolean)} method.
 * Hence, do not need to worry about creating an object of this class.
 * <p>
 * 
 * @author Manzur Murshed
 * @author Rajkumar Buyya
 * @since CloudSim Toolkit 1.0
 */
public class CloudInformationService extends SimEntity {

    /** For all types of hostList. */
    private final List<Integer> resList;

    /** Only for AR hostList. */
    private final List<Integer> arList;

    /** List of all regional GIS. */
    private final List<Integer> gisList;

    /**
     * Allocates a new CloudInformationService object.
     * 
     * @param name
     *        the name to be associated with this entity (as required by
     *        SimEntity class)
     * 
     * @throws Exception
     *         This happens when creating this entity before initialising
     *         CloudSim package or this entity name is <tt>null</tt> or empty
     * 
     * @see gridsim.CloudSim#init(int, Calendar, boolean)
     * @see yoursim.SimEntity
     * @pre name != null
     * @post $none
     */
    public CloudInformationService(String name) throws Exception {
	super(name);
	this.resList = new LinkedList<Integer>();
	this.arList = new LinkedList<Integer>();
	this.gisList = new LinkedList<Integer>();
    }

    /**
     * Starts the CloudInformationService entity.
     */
    @Override
    public void startEntity() {
    }

    /**
     * Processes events scheduled for this entity.
     * 
     * @param ev
     *        the event to be handled.
     * 
     * @see SimEntity#processEvent(SimEvent)
     */
    @Override
    public void processEvent(SimEvent ev) {
	int id = -1; // requester id
	switch ((CloudSimTags) ev.getTag()) {
	// storing regional GIS id
	case REGISTER_REGIONAL_GIS:
	    this.gisList.add((Integer) ev.getData());
	    break;

	// request for all regional GIS list
	case REQUEST_REGIONAL_GIS:

	    // Get ID of an entity that send this event
	    id = ((Integer) ev.getData()).intValue();

	    // Send the regional GIS list back to sender
	    super.send(id, 0L, ev.getTag(), this.gisList);
	    break;

	// A resource is requesting to register.
	case REGISTER_RESOURCE:
	    this.resList.add((Integer) ev.getData());
	    break;

	// A resource that can support Advance Reservation
	case REGISTER_RESOURCE_AR:
	    this.resList.add((Integer) ev.getData());
	    this.arList.add((Integer) ev.getData());
	    break;

	// A Broker is requesting for a list of all hostList.
	case RESOURCE_LIST:

	    // Get ID of an entity that send this event
	    id = ((Integer) ev.getData()).intValue();

	    // Send the resource list back to the sender
	    super.send(id, 0L, ev.getTag(), this.resList);
	    break;

	// A Broker is requesting for a list of all hostList.
	case RESOURCE_AR_LIST:

	    // Get ID of an entity that send this event
	    id = ((Integer) ev.getData()).intValue();

	    // Send the resource AR list back to the sender
	    super.send(id, 0L, ev.getTag(), this.arList);
	    break;

	default:
	    processOtherEvent(ev);
	    break;
	}
    }

    /**
     * Shutdowns the CloudInformationService.
     */
    @Override
    public void shutdownEntity() {
	notifyAllEntity();
    }

    /**
     * Gets the list of all CloudResource IDs, including hostList that support
     * Advance Reservation.
     * 
     * @return LinkedList containing resource IDs. Each ID is represented by an
     *         Integer object.
     * 
     * @pre $none
     * @post $none
     */
    public List<Integer> getList() {
	return this.resList;
    }

    /**
     * Gets the list of CloudResource IDs that <b>only</b> support Advanced
     * Reservation.
     * 
     * @return LinkedList containing resource IDs. Each ID is represented by an
     *         Integer object.
     * 
     * @pre $none
     * @post $none
     */
    public List<Integer> getAdvReservList() {
	return this.arList;
    }

    /**
     * Checks whether a given resource ID supports Advanced Reservations or not.
     * 
     * @param id
     *        a resource ID
     * 
     * @return <tt>true</tt> if this resource supports Advanced Reservations,
     *         <tt>false</tt> otherwise
     * 
     * @pre id != null
     * @post $none
     */
    public boolean resourceSupportAR(Integer id) {
	if (id == null) {
	    return false;
	}

	return resourceSupportAR(id.intValue());
    }

    /**
     * Checks whether a given resource ID supports Advanced Reservations or not.
     * 
     * @param id
     *        a resource ID
     * 
     * @return <tt>true</tt> if this resource supports Advanced Reservations,
     *         <tt>false</tt> otherwise
     * 
     * @pre id >= 0
     * @post $none
     */
    public boolean resourceSupportAR(int id) {
	boolean flag = false;
	if (id < 0) {
	    flag = false;
	} else {
	    flag = checkResource(this.arList, id);
	}

	return flag;
    }

    /**
     * Checks whether the given CloudResource ID exists or not.
     * 
     * @param id
     *        a CloudResource id
     * 
     * @return <tt>true</tt> if the given ID exists, <tt>false</tt> otherwise
     * 
     * @pre id >= 0
     * @post $none
     */
    public boolean resourceExist(int id) {
	boolean flag = false;
	if (id < 0) {
	    flag = false;
	} else {
	    flag = checkResource(this.resList, id);
	}

	return flag;
    }

    /**
     * Checks whether the given CloudResource ID exists or not.
     * 
     * @param id
     *        a CloudResource id
     * 
     * @return <tt>true</tt> if the given ID exists, <tt>false</tt> otherwise
     * 
     * @pre id != null
     * @post $none
     */
    public boolean resourceExist(Integer id) {
	if (id == null) {
	    return false;
	}
	return resourceExist(id.intValue());
    }

    // //////////////////////// PROTECTED METHODS ////////////////////////////

    /**
     * This method needs to override by a child class for processing other
     * events. These events are based on tags that are not mentioned in
     * {@link #body()} method.
     * 
     * @param ev
     *        a Sim_event object
     * 
     * @pre ev != null
     * @post $none
     */
    protected void processOtherEvent(SimEvent ev) {
	if (ev == null) {
	    Log.logger.info("["
		    + Double.toString(CloudSim.clock())
		    + "] "
		    + "CloudInformationService.processOtherEvent(): "
		    + "Unable to handle a request since the event is null.");
	    return;
	}

	Log.logger.info("["
		+ Double.toString(CloudSim.clock())
		+ "] "
		+ "CloudInformationSevice.processOtherEvent(): "
		+ "Unable to handle a request from "
		+ CloudSim.getEntityName(ev.getSource())
		+ " with event tag = "
		+ ev.getTag());
    }

    /**
     * Notifies the registered entities about the end of simulation. This method
     * should be overridden by the child class
     */
    protected void processEndSimulation() {
	// this should be overridden by the child class
    }

    // ////////////////// End of PROTECTED METHODS ///////////////////////////

    /**
     * Checks for a list for a particular resource id.
     * 
     * @param list
     *        list of hostList
     * @param id
     *        a resource ID
     * 
     * @return true if a resource is in the list, otherwise false
     * 
     * @pre list != null
     * @pre id > 0
     * @post $none
     */
    private boolean checkResource(Collection<Integer> list, int id) {
	boolean flag = false;
	if (list == null || id < 0) {
	    return flag;
	}

	Integer obj = null;
	Iterator<Integer> it = list.iterator();

	// a loop to find the match the resource id in a list
	while (it.hasNext()) {
	    obj = it.next();
	    if (obj.intValue() == id) {
		flag = true;
		break;
	    }
	}

	return flag;
    }

    /**
     * Tells all registered entities the end of simulation.
     * 
     * @pre $none
     * @post $none
     */
    private void notifyAllEntity() {
	Log.logger.info("["
		+ Double.toString(CloudSim.clock())
		+ "] "
		+ super.getName()
		+ ": Notify all CloudSim entities for shutting down.");

	signalShutdown(this.resList);
	signalShutdown(this.gisList);

	// reset the values
	this.resList.clear();
	this.gisList.clear();
    }

    /**
     * Sends a signal to all entity IDs mentioned in the given list.
     * 
     * @param list
     *        List storing entity IDs
     * 
     * @pre list != null
     * @post $none
     */
    protected void signalShutdown(Collection<Integer> list) {
	// checks whether a list is empty or not
	if (list == null) {
	    return;
	}

	Iterator<Integer> it = list.iterator();
	Integer obj = null;
	int id = 0; // entity ID

	// Send END_OF_SIMULATION event to all entities in the list
	while (it.hasNext()) {
	    obj = it.next();
	    id = obj.intValue();
	    super.send(id, 0L, CloudSimTags.END_OF_SIMULATION);
	}
    }

} // end class

