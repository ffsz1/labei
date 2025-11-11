/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xchat.oauth2.service.provider.approval;

import java.util.Collection;

/**
 * Interface for saving, retrieving and revoking user approvals (per client, per scope). 
 * 
 * @author Dave Syer
 *
 */
public interface ApprovalStore {

	public boolean addApprovals(Collection<com.xchat.oauth2.service.provider.approval.Approval> approvals);

	public boolean revokeApprovals(Collection<com.xchat.oauth2.service.provider.approval.Approval> approvals);

	public Collection<com.xchat.oauth2.service.provider.approval.Approval> getApprovals(String userId, String clientId);

}
