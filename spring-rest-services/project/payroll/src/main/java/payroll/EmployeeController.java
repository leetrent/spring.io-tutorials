package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {
	private final EmployeeRepository repository;
	private final EmployeeModelAssembler assembler;
	
	EmployeeController(EmployeeRepository repo, EmployeeModelAssembler asmblr) {
		this.repository = repo;
		this.assembler = asmblr;
	}
	
//	@GetMapping("/employees")
//	List<Employee> all() {
//		return this.repository.findAll();
//	}
	
//	@GetMapping("/employees")
//	CollectionModel<EntityModel<Employee>> all() {
//		List<EntityModel<Employee>> employees = this.repository.findAll().stream()
//			.map(employee -> EntityModel.of(employee,
//			linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//			linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
//			.collect(Collectors.toList());
//		
//		return CollectionModel.of(employees,
//				linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//	}
	
	
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {
		List<EntityModel<Employee>> employees = this.repository.findAll().stream()
			.map(this.assembler::toModel)
			.collect(Collectors.toList());
		
		return CollectionModel.of(employees,
				linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
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
		
		return this.assembler.toModel(employee);
				
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
