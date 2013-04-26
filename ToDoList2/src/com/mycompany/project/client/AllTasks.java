/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.mycompany.project.client;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AllTasks implements EntryPoint {
	
	private AllTasksServiceAsync server; 
	
		public void onModuleLoad() {
			  
			// Create the server proxy
			server = AllTasksService.Util.getInstance();
			
			// Get the tasks from the server.  Record then and add them to the 
			// flex table.
			server.getTasks(new AsyncCallback<List<Task>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.toString());
				}

				@Override
				public void onSuccess(List<Task> t) {
					buildTable(t);
				}
			});	 
	}
		/**
		 * @wbp.parser.entryPoint
		 */
		private void buildTable(final List<Task> list) {
			
			 // Create a CellTable.
		    CellTable<Task> table = new CellTable<Task>();

		    // Create name column.
		    TextColumn<Task> emailColumn = new TextColumn<Task>() {
		      @Override
		      public String getValue(Task task) {
		        return task.getEmail();
		      }
		    };

		    // Make the name column sortable.
		    emailColumn.setSortable(true);

		    // Create address column.
		    TextColumn<Task> taskColumn = new TextColumn<Task>() {
		      @Override
		      public String getValue(Task task) {
		        return task.getName();
		      }
		    };
		    
		 // Create address column.
		    TextColumn<Task> dateColumn = new TextColumn<Task>() {
		      @Override
		      public String getValue(Task task) {
		        return task.getDate();
		      }
		    };
		    
		    dateColumn.setSortable(true);

		    // Add the columns.
		    table.addColumn(emailColumn, "Email");
		    table.addColumn(taskColumn, "Task");
		    table.addColumn(dateColumn, "Date");

		    // Create a data provider.
		    ListDataProvider<Task> dataProvider = new ListDataProvider<Task>();

		    // Connect the table to the data provider.
		    dataProvider.addDataDisplay(table);

		    // Add the data to the data provider, which automatically pushes it to the
		    // widget.
		    List<Task> dplist = dataProvider.getList();
		    for (Task task : list) {
		      dplist.add(task);
		    }

		    // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		   ListHandler<Task> columnSortHandler = new ListHandler<Task>(
		        list);
		    columnSortHandler.setComparator(emailColumn,
		        new Comparator<Task>() {
		          public int compare(Task o1, Task o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		              return (o2 != null) ? o1.getEmail().compareTo(o2.getEmail()) : 1;
		            }
		            return -1;
		          }
		        });

		    table.addColumnSortHandler(columnSortHandler);

		    // We know that the data is sorted alphabetically by default.
		    table.getColumnSortList().push(emailColumn);
		    

		    // Add it to the root panel.
		    RootPanel rootPanel = RootPanel.get();
		    rootPanel.add(table);
		    
		    Button btnNewButton = new Button("New button");
		    btnNewButton.setText("Sort by Email");
		    rootPanel.add(btnNewButton, 0, 225);
		    
		    Button btnNewButton_1 = new Button("New button");
		    btnNewButton_1.setText("Sort by Date");
		    rootPanel.add(btnNewButton_1, 118, 225);
		    
		    Anchor hprlnkNewHyperlink = new Anchor("ToDoList");
		    hprlnkNewHyperlink.setHref("ToDoList.html?gwt.codesvr=127.0.0.1:9997");
		    rootPanel.add(hprlnkNewHyperlink, 5, 275);
		    
		    btnNewButton.addClickHandler(new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					Window.Location.reload();

					};
			});
		    
		    btnNewButton_1.addClickHandler(new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					Window.Location.reload();

					};
			});
		  }
}
