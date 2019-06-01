import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.table.BayesianTable;
import org.encog.ml.bayesian.table.TableLine;

/**
 * @author 180029539 This Class contains all the elements of the bayesian agent
 *         and its functionalities
 *
 */
public class BayesianAgent {

	BayesianNetwork bn, bn1, bnCombined;
	ArrayList<String> bnGroupNodes = new ArrayList<String>();
	ArrayList<String> bn1GroupNodes = new ArrayList<String>();
	ArrayList<String> bnNodes = new ArrayList<String>();
	ArrayList<String> bn1Nodes = new ArrayList<String>();
	ArrayList<String> bnAllNodes = new ArrayList<String>();
	ArrayList<String> intersectNodes = new ArrayList<String>();
	ArrayList<String> nonIntersectNodes = new ArrayList<String>();
	ArrayList<String> internalNodes = new ArrayList<String>();
	ArrayList<String> externalNodes = new ArrayList<String>();
	boolean[][] parentRelation, parentRelation1, parentRelationAll;

	public BayesianAgent() {
		createNetwork1();
		createNetwork2();
		extractNodes1();
		extractNodes2();
		extractAllNodes();
		generateRelation1();
		generateRelation2();
		extractIntersectNodes();
		extractNonIntersectNodes();
		splitIntersectionNodes();
		deleteRule();
		treatNonIntersectionNodes();
		createNewNetwork();

	}

	/**
	 * This method creates the first network by initialising the events,
	 * dependencies and probabilities
	 */
	public void createNetwork1() {
		bn = new BayesianNetwork();
		BayesianEvent emailContent = bn.createEvent("Email_Content");
		BayesianEvent detectionSystem = bn.createEvent("Detection_System");
		BayesianEvent maintenanceSchedule = bn.createEvent("Maintenance_Schedule");
		BayesianEvent firewallStatus = bn.createEvent("Firewall_Status");
		BayesianEvent events = bn.createEvent("Political_Holiday_Events");
		BayesianEvent attack = bn.createEvent("Attack");
		BayesianEvent alert = bn.createEvent("Alert");
		BayesianEvent log = bn.createEvent("Log");

		bn.createDependency(emailContent, detectionSystem);
		bn.createDependency(maintenanceSchedule, firewallStatus);
		bn.createDependency(detectionSystem, attack);
		bn.createDependency(firewallStatus, attack);
		bn.createDependency(events, attack);
		bn.createDependency(attack, log);
		bn.createDependency(attack, alert);
		bn.finalizeStructure();

		emailContent.getTable().addLine(0.5, true);

		detectionSystem.getTable().addLine(0.97, true, true);
		detectionSystem.getTable().addLine(0.97, true, false);

		maintenanceSchedule.getTable().addLine(0.98, true);

		firewallStatus.getTable().addLine(0.95, true, true);
		firewallStatus.getTable().addLine(0.95, true, false);

		events.getTable().addLine(0.2778, true);

		attack.getTable().addLine(0.1, true, true, true, true);
		attack.getTable().addLine(0.2, true, false, true, true);
		attack.getTable().addLine(0.5, true, true, false, true);
		attack.getTable().addLine(0.4, true, false, false, true);
		attack.getTable().addLine(0.6, true, true, true, false);
		attack.getTable().addLine(0.2, true, false, true, false);
		attack.getTable().addLine(0.9, true, true, false, false);
		attack.getTable().addLine(0.75, true, false, false, false);

		alert.getTable().addLine(0.8, true, true);
		alert.getTable().addLine(0.2, true, false);

		log.getTable().addLine(0.7, true, true);
		log.getTable().addLine(0.7, true, false);

		bn.validate();
		System.out.println("First Network Created");
	}

	/**
	 * This method creates the second network by initialising the events,
	 * dependencies and probabilities
	 */
	public void createNetwork2() {

		bn1 = new BayesianNetwork();

		BayesianEvent emailContent = bn1.createEvent("Email_Content");
		BayesianEvent detectionSystem = bn1.createEvent("Detection_System");
		BayesianEvent maintenanceSchedule = bn1.createEvent("Maintenance_Schedule");
		BayesianEvent firewallStatus = bn1.createEvent("Firewall_Status");
		BayesianEvent events = bn1.createEvent("Political_Holiday_Events");
		BayesianEvent attack = bn1.createEvent("Attack");
		BayesianEvent backupRestore = bn1.createEvent("Backup_Restore");
		BayesianEvent alert = bn1.createEvent("Alert");
		BayesianEvent notification = bn1.createEvent("Notification");
		BayesianEvent log = bn1.createEvent("Log");

		bn1.createDependency(emailContent, detectionSystem);
		bn1.createDependency(maintenanceSchedule, firewallStatus);
		bn1.createDependency(detectionSystem, attack);
		bn1.createDependency(firewallStatus, attack);
		bn1.createDependency(events, attack);
		bn1.createDependency(attack, log);
		bn1.createDependency(attack, alert);
		bn1.createDependency(alert, notification);
		bn1.createDependency(attack, backupRestore);

		bn1.finalizeStructure();

		emailContent.getTable().addLine(0.5, true);

		detectionSystem.getTable().addLine(0.97, true, true);
		detectionSystem.getTable().addLine(0.97, true, false);

		maintenanceSchedule.getTable().addLine(0.98, true);

		firewallStatus.getTable().addLine(0.95, true, true);
		firewallStatus.getTable().addLine(0.95, true, false);

		events.getTable().addLine(0.2778, true);

		attack.getTable().addLine(0.1, true, true, true, true);
		attack.getTable().addLine(0.2, true, false, true, true);
		attack.getTable().addLine(0.5, true, true, false, true);
		attack.getTable().addLine(0.4, true, false, false, true);
		attack.getTable().addLine(0.6, true, true, true, false);
		attack.getTable().addLine(0.2, true, false, true, false);
		attack.getTable().addLine(0.9, true, true, false, false);
		attack.getTable().addLine(0.75, true, false, false, false);

		alert.getTable().addLine(0.8, true, true);
		alert.getTable().addLine(0.2, true, false);

		log.getTable().addLine(0.7, true, true);
		log.getTable().addLine(0.7, true, false);

		backupRestore.getTable().addLine(0.95, true, true);
		backupRestore.getTable().addLine(0.1, true, false);

		notification.getTable().addLine(0.95, true, true);
		notification.getTable().addLine(0.1, true, false);

		bn1.validate();
		System.out.println("Second Network Created");
		// int[] abc = { 0 };
		// System.out.println(notification.getTable().findLine(1,
		// abc).getProbability());
	}

	/**
	 * This extracts all the event nodes from first network and stores them in a
	 * array list
	 */
	public void extractNodes1() {
		String eventString = bn.getEvents().toString().replaceAll("[\\[\\] ]", "");
		String[] splitEventString = eventString.split("\\),");
		for (int i = 0; i < splitEventString.length; i++) {
			splitEventString[i] = splitEventString[i].replace("P(", "").replace(")", "");
			bnGroupNodes.add(splitEventString[i]);
			if (splitEventString[i].contains("|"))
				splitEventString[i] = splitEventString[i].substring(0, splitEventString[i].indexOf("|"));
			bnNodes.add(splitEventString[i]);

		}
		System.out.println("Extracted Nodes from First Network");
	}

	/**
	 * This extracts all the event nodes from second network and stores them in a
	 * array list
	 */
	public void extractNodes2() {
		String eventString = bn1.getEvents().toString().replaceAll("[\\[\\] ]", "");
		String[] splitEventString = eventString.split("\\),");
		for (int i = 0; i < splitEventString.length; i++) {
			splitEventString[i] = splitEventString[i].replace("P(", "").replace(")", "");
			bn1GroupNodes.add(splitEventString[i]);
			if (splitEventString[i].contains("|"))
				splitEventString[i] = splitEventString[i].substring(0, splitEventString[i].indexOf("|"));
			bn1Nodes.add(splitEventString[i]);

		}
		System.out.println("Extracted Nodes from Second Network");
	}

	/**
	 * This extracts the parent relation information of each node from first network
	 * and stores in a boolean array
	 */
	public void generateRelation1() {
		parentRelation = new boolean[bnNodes.size()][bnNodes.size()];
		for (int i = 0; i < bnNodes.size(); i++) {
			if (bnGroupNodes.get(i).contains("|")) {
				for (int j = 0; j < bnNodes.size(); j++) {
					for (int k = 0; k < bnNodes.size(); k++) {
						if (bnGroupNodes.get(j).contains(bnNodes.get(k)) && j != k)
							parentRelation[j][k] = true;
					}
				}
			}
		}
		System.out.println("Extracted Dependencies from First Network");
	}

	/**
	 * This extracts the parent relation information of each node from second
	 * network and stores in a boolean array
	 */
	public void generateRelation2() {
		parentRelation1 = new boolean[bn1Nodes.size()][bn1Nodes.size()];
		for (int i = 0; i < bn1Nodes.size(); i++) {
			if (bn1GroupNodes.get(i).contains("|")) {
				for (int j = 0; j < bn1Nodes.size(); j++) {
					for (int k = 0; k < bn1Nodes.size(); k++) {
						if (bn1GroupNodes.get(j).contains(bn1Nodes.get(k)) && j != k)
							parentRelation1[j][k] = true;
					}
				}
			}
		}
		System.out.println("Extracted Dependencies from Second Network");
	}

	/**
	 * This stores all the nodes from both networks without duplicates and adds it
	 * to an array list
	 */
	public void extractAllNodes() {
		Set<String> nodeSet = new HashSet<String>();
		nodeSet.addAll(bnNodes);
		nodeSet.addAll(bn1Nodes);
		bnAllNodes.addAll(nodeSet);
		parentRelationAll = new boolean[bnAllNodes.size()][bnAllNodes.size()];
		System.out.println("Combined Nodes from Both Networks");
	}

	/**
	 * This stores all the common nodes from both networks and adds it to an array
	 * list
	 */
	public void extractIntersectNodes() {
		intersectNodes = new ArrayList<String>(bnNodes);
		intersectNodes.retainAll(bn1Nodes);
		System.out.println("Extracted the Intersecting Nodes");
	}

	/**
	 * This stores all the nodes that are not common from both networks and adds it
	 * to an array list
	 */
	public void extractNonIntersectNodes() {
		Set<String> nodeSet = new HashSet<String>();
		for (int i = 0; i < bnAllNodes.size(); i++) {
			if (!(intersectNodes.contains(bnAllNodes.get(i)))) {
				nodeSet.add(bnAllNodes.get(i));
			}
		}

		nonIntersectNodes.addAll(nodeSet);
		System.out.println("Extracted the Non-Intersecting Nodes");
	}

	/**
	 * This method splits the intersection nodes into external and internal nodes
	 * and stores them in separate array lists
	 */
	public void splitIntersectionNodes() {
		ArrayList<String> bnParents = new ArrayList<String>();
		ArrayList<String> bn1Parents = new ArrayList<String>();
		for (int i = 0; i < intersectNodes.size(); i++) {
			bnParents = getParentNodes(intersectNodes.get(i), bn);
			bn1Parents = getParentNodes(intersectNodes.get(i), bn1);
			if (bnParents.isEmpty() || bn1Parents.isEmpty())
				internalNodes.add(intersectNodes.get(i));
			else if (checkIntersection(bnParents) || checkIntersection(bn1Parents)) {
				internalNodes.add(intersectNodes.get(i));
			} else
				externalNodes.add(intersectNodes.get(i));
		}
		System.out.println("Split the Intersecting Nodes into Internal and External Nodes");
	}

	/**
	 * This method takes an event and a bayesian network as input and gives an array
	 * list of parents as output
	 * 
	 * @param childnode the event to be checked
	 * @param tempBN    the bayesian network to check for parents
	 * @return
	 */
	public ArrayList<String> getParentNodes(String childnode, BayesianNetwork tempBN) {
		ArrayList<String> tempList = new ArrayList<String>();
		if (tempBN.equals(bn)) {
			for (int i = 0; i < parentRelation.length; i++) {
				if (bnNodes.get(i).equals(childnode)) {
					for (int j = 0; j < parentRelation[i].length; j++) {
						if (parentRelation[i][j] == true)
							tempList.add(bnNodes.get(i));
					}

				}
			}
		} else if (tempBN.equals(bn1)) {
			for (int i = 0; i < parentRelation1.length; i++) {
				if (bn1Nodes.get(i).equals(childnode)) {
					for (int j = 0; j < parentRelation1[i].length; j++) {
						if (parentRelation1[i][j] == true)
							tempList.add(bn1Nodes.get(i));
					}

				}
			}
		}
		return tempList;
	}

	/**
	 * this method takes a list of events and checks if all of them are present in
	 * the intersection list
	 * 
	 * @param tempList the list of events to be checked
	 * @return true/false depending on the intersection status
	 */
	public boolean checkIntersection(ArrayList<String> tempList) {
		boolean intersectionStatus = true;
		for (int i = 0; i < tempList.size(); i++) {
			if (!(intersectNodes.contains(tempList.get(i)))) {
				intersectionStatus = false;
				break;
			}
		}
		return intersectionStatus;
	}

	/**
	 * This applies the delete rule on the internal node and creates a boolean array
	 * of parent dependencies
	 */
	public void deleteRule() {
		ArrayList<String> bnParents = new ArrayList<String>();
		ArrayList<String> bn1Parents = new ArrayList<String>();
		int position = 0, position1 = 0, position2 = 0;
		for (int i = 0; i < internalNodes.size(); i++) {
			position = getPosition(internalNodes.get(i), bnNodes);
			position1 = getPosition(internalNodes.get(i), bn1Nodes);
			position2 = getPosition(internalNodes.get(i), bnAllNodes);
			bnParents = getParentNodes(internalNodes.get(i), bn);
			bn1Parents = getParentNodes(internalNodes.get(i), bn1);

			if (bnParents.size() > 0 && bn1Parents.size() > 0) {
				if (!checkIntersection(bnParents) && checkIntersection(bn1Parents)) {
					setInternalValues(bnNodes, position, position2);
				} else if (!checkIntersection(bn1Parents) && checkIntersection(bnParents)) {
					setInternalValues(bn1Nodes, position1, position2);
				} else if (checkIntersection(bn1Parents) && checkIntersection(bnParents)) {
					if (bnParents.size() > bn1Parents.size())
						setInternalValues(bnNodes, position, position2);
					else if (bnParents.size() <= bn1Parents.size())
						setInternalValues(bn1Nodes, position1, position2);

				}
			} else if (bnParents.size() > 0 && !checkIntersection(bnParents)) {
				setInternalValues(bnNodes, position, position2);
			} else if (bn1Parents.size() > 0 && !checkIntersection(bn1Parents)) {
				setInternalValues(bn1Nodes, position1, position2);
			}
		}
		System.out.println("Applied the delete rule to get the dependencies of new network");
	}

	/**
	 * this takes an element as input and returns the position of element in the
	 * list
	 * 
	 * @param inputNode the element to be checked
	 * @param inputList the list to be checked
	 * @return the position of element
	 */
	private int getPosition(String inputNode, ArrayList<String> inputList) {

		return inputList.indexOf(inputNode);
	}

	/**
	 * This takes the internal node and copies its parent dependencies to the new
	 * dependencies array
	 * 
	 * @param inputList     the list of nodes to know which bayesian network
	 *                      dependencies we are considering
	 * @param bnPosition    the position of node in nodes list of the selected
	 *                      network whose properties are to be transferred
	 * @param bnAllPosition the position of node in all nodes list whose properties
	 *                      are to be transferred
	 */
	public void setInternalValues(ArrayList<String> inputList, int bnPosition, int bnAllPosition) {
		int position, position1;

		for (int i = 0; i < intersectNodes.size(); i++) {
			if (inputList.equals(bnNodes)) {
				position = getPosition(intersectNodes.get(i), bnNodes);
				position1 = getPosition(intersectNodes.get(i), bnAllNodes);
				parentRelationAll[bnAllPosition][position1] = parentRelation[bnPosition][position];
			} else if (inputList.equals(bn1Nodes)) {

				position = getPosition(intersectNodes.get(i), bn1Nodes);
				position1 = getPosition(intersectNodes.get(i), bnAllNodes);
				parentRelationAll[bnAllPosition][position1] = parentRelation1[bnPosition][position];
			}
		}
	}

	/**
	 * This takes the non intersecting node and copies its parent dependencies to
	 * the new dependencies array
	 * 
	 * @param inputList     the list of nodes to know which bayesian network
	 *                      dependencies we are considering
	 * @param bnPosition    the position of node in nodes list of the selected
	 *                      network whose properties are to be transferred
	 * @param bnAllPosition the position of node in all nodes list whose properties
	 *                      are to be transferred
	 */
	public void setNonIntersectingValues(ArrayList<String> inputList, int bnPosition, int bnAllPosition) {
		int position, position1;
		for (int i = 0; i < bnAllNodes.size(); i++) {
			if (inputList.equals(bnNodes)) {
				position = getPosition(bnAllNodes.get(i), bnNodes);
				position1 = getPosition(bnAllNodes.get(i), bnAllNodes);
				parentRelationAll[bnAllPosition][position1] = parentRelation[bnPosition][position];
			} else if (inputList.equals(bn1Nodes)) {
				position = getPosition(bnAllNodes.get(i), bn1Nodes);
				position1 = getPosition(bnAllNodes.get(i), bnAllNodes);
				parentRelationAll[bnAllPosition][position1] = parentRelation1[bnPosition][position];
			}
		}
	}

	/**
	 * This method goes through all non intersecting nodes and copies their parent
	 * dependencies to the new dependencies array
	 * 
	 */
	public void treatNonIntersectionNodes() {
		int position, position1, position2;
		for (int i = 0; i < nonIntersectNodes.size(); i++) {

			position2 = getPosition(nonIntersectNodes.get(i), bnAllNodes);
			if (bnNodes.contains(nonIntersectNodes.get(i))) {
				position = getPosition(nonIntersectNodes.get(i), bnNodes);
				setNonIntersectingValues(bnNodes, position, position2);
			} else if (bn1Nodes.contains(nonIntersectNodes.get(i))) {
				position1 = getPosition(nonIntersectNodes.get(i), bn1Nodes);
				setNonIntersectingValues(bn1Nodes, position1, position2);
			}
		}
		System.out.println("Treated the Non-Intersecting Nodes");
	}

	/**
	 * This method creates the new network with all the new nodes and dependencies
	 */
	public void createNewNetwork() {
		bnCombined = new BayesianNetwork();
		BayesianEvent[] bEvents = new BayesianEvent[bnAllNodes.size()];
		// create nodes
		for (int i = 0; i < bnAllNodes.size(); i++) {
			bEvents[i] = bnCombined.createEvent(bnAllNodes.get(i));
		}
		// create dependencies
		for (int i = 0; i < parentRelationAll.length; i++) {
			for (int j = 0; j < parentRelationAll[i].length; j++) {
				if (parentRelationAll[i][j] == true) {
					bnCombined.createDependency(bnAllNodes.get(j), bnAllNodes.get(i));
				}
			}
		}
		bnCombined.finalizeStructure();
		// set probability
		for (int i = 0; i < bnAllNodes.size(); i++) {
			setProbability(bnAllNodes.get(i));
		}
		bnCombined.validate();
		System.out.println("\nNew Network is created");

		displayNewNetwork();
	}

	/**
	 * This method prints out the new network details
	 */
	public void displayNewNetwork() {
		System.out.println("\nNew Network Events and Parents:");
		Iterator<BayesianEvent> itr = bnCombined.getEvents().iterator();
		while (itr.hasNext()) {
			System.out.println();
			BayesianEvent be = itr.next();
			System.out.println("Event: " + be.getLabel());
			Iterator<BayesianEvent> itr1 = be.getParents().iterator();
			if (itr1.hasNext()) {
				System.out.print("Parents: " + itr1.next().getLabel());
				while (itr1.hasNext()) {
					System.out.print(", " + itr1.next().getLabel());
				}
				System.out.println();
			} else
				System.out.println("Parents: None");
			System.out.println("Probability:\n" + be.getTable());
		}
	}

	/**
	 * This calculates the probabilities of all the nodes in the new network
	 */
	public void setProbability(String inputString) {
		Iterator<TableLine> itr;
		TableLine tl, tl1, tl2, tl3;
		double p1, p2, p3, p4, ptotal;
		if (nonIntersectNodes.contains(inputString)) {
			if (bnNodes.contains(inputString)) {
				itr = bn.getEvent(inputString).getTable().getLines().iterator();
				while (itr.hasNext()) {
					tl = itr.next();
					bnCombined.getEvent(inputString).getTable().addLine(tl.getProbability(), tl.getResult(),
							tl.getArguments());
				}
			} else {
				itr = bn1.getEvent(inputString).getTable().getLines().iterator();
				while (itr.hasNext()) {
					tl = itr.next();
					bnCombined.getEvent(inputString).getTable().addLine(tl.getProbability(), tl.getResult(),
							tl.getArguments());
				}

			}

		} else if (internalNodes.contains(inputString)) {
			BayesianTable bt, bt1;
			bt = bn.getEvent(inputString).getTable();
			bt1 = bn1.getEvent(inputString).getTable();
			itr = bt.getLines().iterator();
			while (itr.hasNext()) {
				tl = itr.next();

				if (tl.getResult() == 0) {
					tl1 = bt1.findLine(0, tl.getArguments());
					tl2 = bt.findLine(1, tl.getArguments());
					tl3 = bt1.findLine(1, tl1.getArguments());
					p1 = tl.getProbability();
					p2 = tl1.getProbability();
					p3 = tl2.getProbability();
					p4 = tl3.getProbability();
					ptotal = (p1 + p2 - (p1 * p2)) + (p3 + p4 - (p3 * p4));
					bnCombined.getEvent(inputString).getTable().addLine((p1 + p2 - (p1 * p2)) / ptotal, 0,
							tl.getArguments());
					bnCombined.getEvent(inputString).getTable().addLine((p3 + p4 - (p3 * p4)) / ptotal, 1,
							tl.getArguments());
				}
			}
		}
	}
}
