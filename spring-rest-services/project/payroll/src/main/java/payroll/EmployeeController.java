package payroll;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
	private final EmployeeRepository repository;
	
	EmployeeController(EmployeeRepository repo) {
		this.repository = repo;
	}
	
	@GetMapping("/employees")
	List<Employee> all() {
		return this.repository.findAll();
	}
	
	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmpl) {
		return this.repository.save(newEmpl);
	}
	
//	@GetMapping("/employees/{id}")
//	Employee one(@PathVariable Long id) {
//		return this.repository.findById(id)
//				              .orElseThrow( () -> new EmployeeNotFoundException(id));
//	}
	
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		
		Employee employee = this.repository.findById(id)
			.orElseThrow(() -> new EmployeeNotFoundException(id));
		
		return EntityModel.of(employee,
			linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
			linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
				
	}
	
	
	
	
	
	@PutMapping("/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		return this.repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return this.repository.save(employee);
				})
				.orElseGet( () -> {
					newEmployee.setId(id);
					return this.repository.save(newEmployee);
				});
	}
	
	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		this.repository.deleteById(id);
	}
	
	
	
}
