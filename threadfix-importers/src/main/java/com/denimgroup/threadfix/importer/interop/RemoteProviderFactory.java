////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2014 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.importer.interop;

import com.denimgroup.threadfix.data.entities.RemoteProviderApplication;
import com.denimgroup.threadfix.data.entities.RemoteProviderType;
import com.denimgroup.threadfix.data.entities.Scan;
import com.denimgroup.threadfix.importer.impl.remoteprovider.RemoteProvider;

import java.util.List;

public interface RemoteProviderFactory {

	public List<RemoteProviderApplication> fetchApplications(RemoteProviderType remoteProviderType);
	
	public RemoteProvider getProvider(String providerType);

	/**
	 * This method takes a remoteProviderApplication and does the rest of the work of getting
	 * a scan file from the remote provider in question.
	 * @param remoteProviderApplication
	 * @return
	 */
	public List<Scan> fetchScans(RemoteProviderApplication remoteProviderApplication);
	
}
