// This implements server-side that is accessed from the client via
// remote procedure call.

// The data for each task list is rooted at a TaskList entity, whose key is created
// from the owner's e-mail address.  The children of the root entity are the individual
// Task entities, each of which has a task (the job to do) and the due date.

package com.mycompany.project.server;

import java.util.*;
import java.util.List;

import com.google.appengine.api.datastore.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.project.client.AllTasksService;
import com.mycompany.project.client.Task;

public class AllTasksServiceImpl extends RemoteServiceServlet implements AllTasksService {
	private static final long serialVersionUID = 1L;

	// Datastore reference
	private DatastoreService datastore;

	/**
	 * The service constructor initializes the reference to the datastore.
	 */
	public AllTasksServiceImpl () {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * Returns the list of tasks that make up the task list for the currently logged-in user.
	 */
	@Override
	public List<Task> getTasks() {
			
			// Get all the tasks that are grouped under the root entity.
			Query query = new Query("Task");
			Iterable<Entity> allTasks = datastore.prepare(query).asIterable();
			
			// Create a list of Task objects.
			List<Task> tasks = new ArrayList<Task>();
			for (Entity e: allTasks) {
				tasks.add(new Task(KeyFactory.keyToString(e.getKey()), 
						           (String)e.getProperty("task"),
						           (Date)e.getProperty("date"),
						           (String)e.getProperty("email")));
			}
			return tasks;
	}
}