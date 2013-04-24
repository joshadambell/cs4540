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


import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AllTasks implements EntryPoint {
	
		public void onModuleLoad() {
			  
			 List<Task> contacts = new ArrayList<Task>();
			 contacts.add(new Task("josh", "Address", new Date(0)));
			 contacts.add(new Task("tay", "Address2", new Date(0)));

		    // Create a CellTable.
		    CellTable<Task> table = new CellTable<Task>();

		    // Create name column.
		    TextColumn<Task> emailColumn = new TextColumn<Task>() {
		      @Override
		      public String getValue(Task task) {
		        return task.getKey();
		      }
		    };

		    

		    // Create address column.
		    TextColumn<Task> nameColumn = new TextColumn<Task>() {
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
		    
		 // Make the name column sortable.
		    nameColumn.setSortable(true);
		    dateColumn.setSortable(true);

		    // Add the columns.
		    table.addColumn(emailColumn, "Email");
		    table.addColumn(nameColumn, "Name");
		    table.addColumn(dateColumn, "Date");

		    // Create a data provider.
		    ListDataProvider<Task> dataProvider = new ListDataProvider<Task>();

		    // Connect the table to the data provider.
		    dataProvider.addDataDisplay(table);

		    // Add the data to the data provider, which automatically pushes it to the
		    // widget.
		    List<Task> list = dataProvider.getList();
		    for (Task contact : contacts) {
		      list.add(contact);
		    }

		    // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    ListHandler<Task> columnSortHandler = new ListHandler<Task>(
		        list);
		    columnSortHandler.setComparator(nameColumn,
		        new Comparator<Task>() {
		          public int compare(Task o1, Task o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		              return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
		            }
		            return -1;
		          }
		        });
		    table.addColumnSortHandler(columnSortHandler);

		    // We know that the data is sorted alphabetically by default.
		    table.getColumnSortList().push(nameColumn);

		    // Add it to the root panel.
		    RootPanel.get().add(table);
		  }

	}
