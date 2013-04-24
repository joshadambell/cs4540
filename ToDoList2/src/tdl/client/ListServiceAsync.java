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
package tdl.client;

import java.util.List;
import java.util.Date;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ListServiceAsync {
	
	public void getTasks(AsyncCallback<List<Task>> callback);
	
	public void addTask (String task, Date date, AsyncCallback<Task> callback);
	
	public void removeTask (Task task, AsyncCallback<Void> callback);
	
	public void verifyLogin (String url, AsyncCallback<UserInfo> callback);
}
