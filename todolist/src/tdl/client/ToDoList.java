package tdl.client;

import java.util.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;


/**
 * Represents the client view of a ToDo List.
 */
public class ToDoList implements EntryPoint {

	private FlexTable flexTable;       // The visible table
	private List<Task> tasks;          // Tasks displayed by the table
	private TextBox newTask;           // Where new tasks are entered
	private Remover removeHandler;     // Deals with clicks on "Done" buttons
	private ListServiceAsync server;   // Proxy for remote server
	private UserInfo userinfo;         // Info about authenticated user
	private DateBox dateBox;

	/**
	 * This method is called when a web page that references the module is loaded.
	 * 
	 */
	public void onModuleLoad() {

		// Create the server proxy
		server = ListService.Util.getInstance();

		// Authenticate the user.  Obtain user information and store in userinfo.  If not
		// logged in, forward to a login page.
		String url = Window.Location.getHref();
		server.verifyLogin(url, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
				return;
			}

			@Override
			public void onSuccess(UserInfo result) {
				userinfo = result;
				if (result.isLoggedIn()) {
					displayApp();
				}
				else {
					Window.Location.assign(userinfo.getLoginURL());
				}
			}		
		});
	}
	
	private void displayApp () {

		// Lay out the root panel with a vertical panel
		RootPanel rootPanel = RootPanel.get(); 
		VerticalPanel verticalPanel = new VerticalPanel();
		rootPanel.add(verticalPanel, 0, 0);
		verticalPanel.setSize("282px", "300px");

		// Create a flextable add add to the root
		flexTable = new FlexTable();
		flexTable.setBorderWidth(2);
		verticalPanel.add(flexTable);
		flexTable.setWidth("201px");
		flexTable.setText(0, 0, "Task");
		flexTable.setText(0, 1, "Due");
		flexTable.setText(0, 2, "Done?");

		// Takes care of removing tasks
		removeHandler = new Remover();

		// Initially, no tasks
		tasks = new ArrayList<Task>();

		// Get the tasks from the server.  Record then and add them to the 
		// flex table.
		server.getTasks(new AsyncCallback<List<Task>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override
			public void onSuccess(List<Task> t) {
				tasks = t;
				int row = 1;
				for (Task task: tasks) {
					flexTable.setText(row, 0, task.getName());
					flexTable.setText(row, 1, task.getDate().toString());
					addDoneButton(row++);
				}
			}
		});

		// Add the new task controls
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("281px");
		
		Label lblTask = new Label("Task");
		horizontalPanel.add(lblTask);
		newTask = new TextBox();
		horizontalPanel.add(newTask);
		newTask.setWidth("101px");

		Button btnAdd = new Button("Add");
		
		// This is the handler for the add button.
		btnAdd.addClickHandler(new ClickHandler() {
			
			private int rows;
			
			public void onClick(ClickEvent event) {
				
				// Make the call to the server to add the task to the datastore.
				rows = flexTable.getRowCount();
				server.addTask(newTask.getText(), dateBox.getValue(), new AsyncCallback<Task> () {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.toString());
					}

					@Override
					public void onSuccess(Task task) {
						flexTable.setText(rows, 0, task.getName());
						flexTable.setText(rows, 1, task.getDate().toString());
						tasks.add(task);
						addDoneButton(rows);
					}

				});

			}
		});
		
		Label lblDueDate = new Label("Due Date");
		horizontalPanel.add(lblDueDate);
		lblDueDate.setWidth("57px");
		
		dateBox = new DateBox();
		horizontalPanel.add(dateBox);
		dateBox.setWidth("107px");
		horizontalPanel.add(btnAdd);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.setWidth("282px");
		
		Anchor logout = new Anchor(true);
		horizontalPanel_1.add(logout);
		logout.setHTML("Logout");
		logout.setHref(userinfo.getLogoutURL());
		
	}


	/**
	 * Adds a Done button to the specified of the flex table
	 */
	private void addDoneButton(int row) {
		Button b = new Button("Done");
		b.addClickHandler(removeHandler);
		flexTable.setWidget(row, 2, b);
	}

	/**
	 *  Deals with a click on a Done button
	 */
	class Remover implements ClickHandler {

		private int row;    // Row number of Done button

		@Override
		public void onClick(ClickEvent event) {

			// Determine the row where the click happened
			row = flexTable.getCellForEvent(event).getRowIndex();

			// Ask the server to remove the corresponding task
			server.removeTask(tasks.get(row-1), new AsyncCallback<Void> () {

				@Override
				// If something goes wrong, display an alert for debugging purposes
				public void onFailure(Throwable caught) {
					Window.alert(caught.toString());

				}

				@Override
				// On success, remove from the flex table
				public void onSuccess(Void result) {
					flexTable.removeRow(row);
				}

			});
		}

	}
}
