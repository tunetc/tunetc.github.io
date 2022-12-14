/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
class UserController {

	private final UserRepository userRepository;

	public UserController(UserRepository clinicService) {
		this.userRepository = clinicService;
	}

	@GetMapping("/users.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		Users vets = new Users();
		Page<User> paginated = findPaginated(page);
		vets.getUserList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);

	}

	private String addPaginationModel(int page, Page<User> paginated, Model model) {
		List<User> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "users/userList";
	}

	private Page<User> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return userRepository.findAll(pageable);
	}

	@GetMapping({ "/users" })
	public @ResponseBody Users showResourcesVetList() {
		Users vets = new Users();
		vets.getUserList().addAll(this.userRepository.findAll());
		return vets;
	}

}
