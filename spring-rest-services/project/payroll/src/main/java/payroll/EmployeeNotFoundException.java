package payroll;

public class EmployeeNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5321824598296334815L;

	EmployeeNotFoundException(Long id) {
		super("Could not find employee with id of " + id);
	}
}
