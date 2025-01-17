/* 
 * Spring Boot Database Admin - An automatically generated CRUD admin UI for Spring Boot apps
 * Copyright (C) 2023 Ailef (http://ailef.tech)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package tech.ailef.dbadmin.internal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.ailef.dbadmin.external.dto.LogsSearchRequest;
import tech.ailef.dbadmin.external.dto.PaginatedResult;
import tech.ailef.dbadmin.external.dto.PaginationInfo;
import tech.ailef.dbadmin.internal.model.UserAction;
import tech.ailef.dbadmin.internal.repository.CustomActionRepositoryImpl;
import tech.ailef.dbadmin.internal.repository.UserActionRepository;

/**
 * Service class to retrieve user actions through the {@link CustomActionRepositoryImpl}. 
 *
 */
@Service
public class UserActionService {
	@Autowired
	private UserActionRepository repo;
	
	@Autowired
	private CustomActionRepositoryImpl customRepo;
	
	@Transactional("internalTransactionManager")
	public UserAction save(UserAction a) {
		return repo.save(a);
	}
	
	/**
	 * Retruns a page of results of user actions that match the given input request.
	 * @param request a request containing filtering parameters for user actions
	 * @return a page of results matching the input request
	 */
	public PaginatedResult<UserAction> findActions(LogsSearchRequest request) {
		String table = request.getTable();
		String actionType = request.getActionType();
		String itemId = request.getItemId();
		PageRequest page = request.toPageRequest();
		
		long count = customRepo.countActions(table, actionType, itemId);
		List<UserAction> actions = customRepo.findActions(request);
		int maxPage = (int)(Math.ceil ((double)count / page.getPageSize()));
		
		return new PaginatedResult<>(
			new PaginationInfo(page.getPageNumber() + 1, maxPage, page.getPageSize(), count, null, request),
			actions
		);
	}
	
}
