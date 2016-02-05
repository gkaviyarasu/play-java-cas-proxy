package controllers;

import com.avaje.ebean.Ebean;
import controllers.annotation.Authenticated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import models.Employee;
import models.PaginationData;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Employee Details", description = "Operations related to Employees")
public class EmployeeController extends CasController {

    /*@Authenticated
    @ApiOperation(value = "List of all employees", response = Employee.class, httpMethod = "GET", responseContainer = "List")
    public Result list() {
        List<Employee> employees = Ebean.find(Employee.class)
                .findList();

        return ok(Json.toJson(employees));
    }*/

    @Authenticated
    @ApiOperation(value = "Employee details for the given Employee id", response = Employee.class, httpMethod = "GET")
    public Result getEmployeeDetailsByEmployeeId(String employeeId) {
        return ok(Json.toJson(Ebean.find(Employee.class).where().eq("employee_id", employeeId).findUnique()));
    }

    @Authenticated
    @ApiOperation(value = "Employee details of currently logged in user", response = Employee.class, httpMethod = "GET")
    public Result getCurrentEmployeeProfile() {
        CasUserProfile profile = getUserProfile();
        return ok(Json.toJson(getEmployeeByMailId(profile.getEmail())));
    }

    @Authenticated
    @ApiOperation(value = "Employee details for the given Email Id.", response = Employee.class, httpMethod = "GET")
    public Result getEmployeeByEmail(String emailId) {
        return ok(Json.toJson(getEmployeeByMailId(emailId)));
    }

    @Authenticated
    @ApiOperation(value = "Employee details whose employee Ids fall in between fromEmpId and toEmpId.", response = EmployeePaginationData.class, httpMethod = "GET")
    public Result getEmployeeDetailsWithPagination(int fromEmpId, int toEmpId, int pageNo, int pageSize) {

        List<String> errors = validatePaginationParam(fromEmpId, toEmpId, pageNo, pageSize);
        if (errors.size() > 0) {
            return status(Http.Status.INTERNAL_SERVER_ERROR, Json.toJson(errors));
        }
        //pageNo-=1;
        List<Employee> employees = Ebean.find(Employee.class)
                .order("employee_id")
                .where()
                .between("employee_id", fromEmpId, toEmpId)
                .findPagedList(pageNo - 1, pageSize).getList();
        int count = getEmployeeDetailsCount(fromEmpId, toEmpId);
        PaginationData<Employee> paginationData = new EmployeePaginationData();
        paginationData.setPageNumber(pageNo);
        paginationData.setRecords(employees);
        paginationData.setPageSize(pageSize);
        paginationData.setTotalPages((int) Math.ceil(count / pageSize));

        return ok(Json.toJson(paginationData));
    }

    @Authenticated
    @ApiOperation(value = "Return Reportees for the Employee/manager with their email id ", response = Employee.class, httpMethod = "GET")
    public Result getReportees(String email) {
        Employee manager = getEmployeeByMailId(email);
        if (manager == null) {
            ok();
        }
        return ok(Json.toJson(Ebean.find(Employee.class).where().eq("manager_id", manager.getId()).findList()));
    }

    private Employee getEmployeeByMailId(String emailId) {
        return Ebean.find(Employee.class).where().eq("email_id", emailId).findUnique();
    }

    private List<String> validatePaginationParam(int fromEmpId, int toEmpId, int pageNo, int pageSize) {
        List<String> errors = new ArrayList<>();
        if (fromEmpId < 0) {
            errors.add("Invalid From Employee Id");
        }
        if (toEmpId < 1 || toEmpId < fromEmpId) {
            errors.add("Invalid To Employee Id");
        }
        if (pageNo < 1) {
            errors.add("Invalid page number");
        }
        if (pageSize < 1) {
            errors.add("Invalid page size");
        }
        return errors;
    }


    private int getEmployeeDetailsCount(int fromEmpId, int toEmpId) {

        return Ebean.find(Employee.class)
                .where()
                .between("employee_id", fromEmpId, toEmpId)
                .findRowCount();
    }

    /*
     * This class is created purely for swagger documentation purpose as it doesn't support Generics types
     */
    private static class EmployeePaginationData extends PaginationData<Employee> {

    }
}
