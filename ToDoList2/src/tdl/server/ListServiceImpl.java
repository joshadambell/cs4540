// This implements server-side that is accessed from the client via
// remote procedure call.

// The data for each task list is rooted at a TaskList entity, whose key is created
// from the owner's e-mail address.  The children of the root entity are the individual
// Task entities, each of which has a task (the job to do) and the due date.

package tdl.server;

import java.util.*;
import tdl.client.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.users.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ListServiceImpl extends RemoteServiceServlet implements ListService {
	private static final long serialVersionUID = 1L;

	// Datastore reference
	private DatastoreService datastore;

	/**
	 * The service constructor initializes the reference to the datastore.
	 */
	public ListServiceImpl () {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	// Gets/creates the root TaskList entity for the logged-in user.  If there is
	// no logged-in user, this will throw an exception.
	private Entity getTaskList () {
		
		// Get user information
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String email = user.getEmail();

		// Create a key for the user's root TaskList entity
		Key key = KeyFactory.createKey("TaskList", email);

		// Get the TaskList if it exists, create it if not.  In either case, return it.
		try {
			return datastore.get(key);
		}
		catch (EntityNotFoundException e) {
			Entity tasklist = new Entity("TaskList", email);
			datastore.put(tasklist);
			return tasklist;
		}		
	}

	/**
	 * Returns the list of tasks that make up the task list for the currently logged-in user.
	 */
	@Override
	public List<Task> getTasks() {
		
		Transaction txn = null;
		try {
			
			// Begin a transaction, then obtain the root entity for the currently logged-in
			// user's task collection.
			txn = datastore.beginTransaction();
			Entity tasklist = getTaskList();
			
			// Get all the tasks that are grouped under the root entity.
			Query query = new Query("Task", tasklist.getKey());
			Iterable<Entity> allTasks = datastore.prepare(query).asIterable();
			
			// Create a list of Task objects.
			List<Task> tasks = new ArrayList<Task>();
			for (Entity e: allTasks) {
				tasks.add(new Task(KeyFactory.keyToString(e.getKey()), 
						           (String)e.getProperty("task"),
						           (Date)e.getProperty("date"),
						           (String)e.getProperty("email")));
			}
			
			// Commit the transactions and return the list.
			txn.commit();
			return tasks;
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}

	
	/**
	 * Adds a task belonging to the currently logged-in user, then returns a Task
	 * object that describes it.
	 */
	@Override
	public Task addTask(String task, Date date) {
		
		// Get user information
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				String email = user.getEmail();
		
		Transaction txn = null;
		try {
			
			// Begin a transaction and get the user's root entity.
			txn = datastore.beginTransaction();
			Entity tasklist = getTaskList();
			
			// Create and populate a new Task entity
			Entity t = new Entity("Task", tasklist.getKey());
			t.setProperty("task", task);
			t.setProperty("date", date);
			t.setProperty("email", email);
			
			// Put it in the datastore and commit the transaction
			datastore.put(t);
			txn.commit();
			
			// Create and return the Task object
			return new Task(KeyFactory.keyToString(t.getKey()), task, date, email);
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}


	/**
	 * Removes the Task, which must be under the currently logged-in user's root entity.
	 */
	@Override
	public void removeTask(Task task) {
		
		Transaction txn = null;
		try {
			// Begin a transaction and get the root entity for the currently logged-in user.
			txn = datastore.beginTransaction();
			Entity tasklist = getTaskList();
			
			// Make sure that the entity to be deleted is under the root entity
			Query query = new Query("Task", tasklist.getKey());
			Filter keyFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, 
					                               FilterOperator.EQUAL, 
					                               KeyFactory.stringToKey(task.getKey()));
			query.setFilter(keyFilter);
			Entity e = datastore.prepare(query).asSingleEntity();
			
			// Delete and commit
			datastore.delete(e.getKey());
			txn.commit();
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}

	/**
	 * Checks whether the current user is logged in, and returns a UserInfo object
	 * describing the result of the check.  The parameter is the URL of the page
	 * to which the user is making an authenticated access.
	 */
	@Override
	public UserInfo verifyLogin(String url) {	
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String login = userService.createLoginURL(url);
		String logout = userService.createLogoutURL(url);
		if (user == null) {
			return new UserInfo(false, "", login, logout);
		}
		else {
			return new UserInfo(true, user.getEmail(), login, logout);
		}
	}

}