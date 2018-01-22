package com.fillingstation.constants;

public interface IQueryConstants {



	String LOGIN_VERIFY ="SELECT  EmployeeId, ET.CompanyId, FirstName, LastName ,Password , Block ,  LockValue ,ET.Role ,ET.Department, EC.Role,EC.Permission  FROM EmployeeTable ET inner join EmployeeConfig EC on EC.Role = ET.Role  Where EmailId= ? or MobileNo = ? and Status = 0";
	
	String COMPANY_NAME="Select CompanyName from CompanyTable where CompanyId = ? ";
	
	String EMP_SELECTION="SELECT Role FROM EmployeeTable WHERE EmployeeId = ? ";

    String ROLE_PERMISSION="SELECT Permission FROM EmployeeConfig where Role = ? And CompanyId = ? ";

    String EMP_GET_DEPT="SELECT DISTINCT Department FROM EmployeeConfig WHERE CompanyId = ? And Role <> 'Propertier'";

    String EMP_GET_ROLE="SELECT DISTINCT Role FROM EmployeeConfig WHERE CompanyId = ? And Role <> 'Propertier'";

    String EMP_LIST="SELECT DISTINCT EmployeeId FROM EmployeeTable where status = 0 And CompanyId = ? And Role <> 'Propertier' ";

    String EMP_LOCK_LIST="SELECT DISTINCT EmployeeId FROM EmployeeTable WHERE status = 0 And CompanyId = ?  And LockValue >=3";

    
    
    
    String EMP_VALIDATION="SELECT EmployeeId FROM EmployeeTable WHERE EmployeeId = ? AND CompanyId = ? ";
	
    String EMP_RESET_LOCK="UPDATE EmployeeTable SET LockValue = 0 WHERE EmployeeId = ? And CompanyId = ? ";
	
    String EMP_SELECT_LOCK = "SELECT LockValue FROM EmployeeTable WHERE EmployeeId = ? And CompanyId = ? ";

    String EMP_LOCK =" UPDATE EmployeeTable SET LockValue = ? WHERE EmployeeId = ? And CompanyId = ? ";

    
	
    String EMP_BLOCK="UPDATE EmployeeTable SET Block = 1  WHERE EmployeeId = ? And CompanyId = ?   ";

    String EMP_SELECT_BLOCK="SELECT Block FROM EmployeeTable WHERE EmployeeId = ?  And CompanyId = ? ";
    
    String EMP_UNBLOCK="UPDATE EmployeeTable SET Block = 0 WHERE EmployeeId = ? And CompanyId = ? ";

	
	
	String EMPLOYEE_ATTENDANCE="SELECT  EA.EmployeeId ,EA.Name,EA.CheckInTime,EA.CheckOutTime,EA.Status,E.MobileNo FROM EmployeeAttendanceTable EA INNER JOIN EmployeeTable E Where EA.EmployeeId=E.EmployeeId AND EA.CompanyId=E.CompanyId AND date(Date) = ? AND E.CompanyId = ?  AND EA.RemoveStatus=0 ";
	
	
	
	String VALID_MAILID="SELECT FirstName FROM EmployeeTable Where EmailId = ? or MobileNo = ?";
	
	String VALID_EMP_ID="SELECT EmployeeId FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND date(Date) = ? AND CompanyId = ? ";

		
	String EMP_INSERT="INSERT INTO EmployeeTable(FirstName,LastName,DOB,EmailId,MobileNo,"
						+ "Address,Type,Role,Department,EmployeeProofType,"
						+ "EmployeeProofNum,CompanyId,EmployeeId)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	String EMP_ID= "SELECT EmployeeId FROM EmployeeTable where EmployeeProofNum= ?";
	
	String EMP_SEARCH="SELECT EmployeeId, FirstName, LastName, MobileNo, Address, Type, Role, Department FROM EmployeeTable WHERE (EmployeeId = ? or Type = ? or Department = ? or MobileNo = ? or Role = ? or FirstName = ? or LastName = ? ) AND CompanyId = ? ";
 
	String EMP_DETAILS= "SELECT * FROM EmployeeTable where EmployeeId= ? AND CompanyId = ? ";
	
	String EMP_DELETE="UPDATE EmployeeTable SET Status = ? WHERE EmployeeId = ? AND CompanyId = ?";

	
	
	String EMP_UPDATE="UPDATE EmployeeTable SET EmployeeId = ? ,FirstName = ?,"
						+ "LastName = ?,DOB = ?,EmailId = ?,MobileNo = ? ,"
						+ "Address = ?,Type = ?,Role = ?,Department = ?,"
						+ "EmployeeProofType = ?,EmployeeProofNum =? WHERE EmployeeId = ? AND CompanyId  = ? ";
	
	
    
	
	String CHECKED_IN_TODAY="SELECT EmployeeId FROM EmployeeAttendanceTable Where EmployeeId = ? AND Date = ? ";
	
	String EMP_CHECKIN="UPDATE EmployeeAttendanceTable SET CheckinTime = ? , Status = 'P' WHERE EmployeeId = ? AND date(Date) = ? AND CompanyId = ? ";	

	String EMP_SELECT_CHECKINTIME="SELECT CheckinTime FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND date(Date) = ? AND CompanyId = ? ";
	
	
	String EMP_CHECKOUTUPDATE="UPDATE EmployeeAttendanceTable SET CheckoutTime = ? , TotalWorkHour = ? "
								+ " WHERE EmployeeId = ? AND date(Date) = ? AND CompanyId = ? ";

	String EMP_SELECT_CHECKOUTTIME="SELECT CheckoutTime FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND date(Date) = ? AND CompanyId = ? ";

	
	String EMP_ID_SELECT="SELECT DISTINCT EmployeeId FROM EmployeeAttendanceTable WHERE date(Date) BETWEEN ? AND ? AND CompanyId = ? ";
	
	String EMP_NAME_SELECT="SELECT CONCAT(FirstName,' ',LastName) AS Name FROM EmployeeTable WHERE EmployeeId = ? AND CompanyId = ? " ;
	
	String EMP_MAINTENANCE_REPORT="SELECT EmployeeId,FirstName,LastName,Type,Department,Role,MobileNo from EmployeeTable  where  CompanyId  = ? And Role <> 'Propertier' ";
	
	String EMP_MAINTENANCE_COUNT="SELECT (SELECT COUNT(EmployeeId) FROM EmployeeTable WHERE Type = ?  AND CompanyId = ? And Status=0 )as PermanentEmployeeCount,"
			+ "(SELECT COUNT(EmployeeId) FROM EmployeeTable WHERE Type = ?  AND CompanyId = ? And Status=0 )as TemporaryEmployeeCount,"
			+ "COUNT(EmployeeId) as ContractEmployeeCount FROM EmployeeTable WHERE Type = ? AND CompanyId = ? And Status=0 And Role <> 'Propertier'";
	
	
	
	
	String EMP_DAILYREPORT="SELECT EA.EmployeeId,EA.Name,EA.CheckinTime,EA.CheckoutTime,EA.TotalWorkHour,EA.Status,"
			+ "E.Type,E.Department FROM EmployeeTable E INNER JOIN EmployeeAttendanceTable EA ON EA.EmployeeId=E.EmployeeId AND EA.CompanyId=E.CompanyId WHERE date(Date) = ? AND EA.CompanyId = ? And RemoveStatus=0  ";

	
	String EMP_DAILYREPORTPRESENTCOUNT="SELECT (SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'P' AND CompanyId = ? AND RemoveStatus=0 )as PermanentEmployeePresentCount," 
			+ "(SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'P' AND CompanyId = ? AND RemoveStatus=0  )as TemporaryEmployeePresentCount,"
			+ "COUNT(EmployeeId) as ContractEmployeePresentCount FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'P' AND CompanyId = ? AND RemoveStatus=0 ";

	
	String EMP_DAILYREPORTABSENTCOUNT="SELECT (SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'A' AND CompanyId = ? AND RemoveStatus=0 )as PermanentEmployeeAbsentCount," 
			+ "(SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'A' AND CompanyId = ? AND RemoveStatus=0 )as TemporaryEmployeeAbsentCount,"
			+ "COUNT(EmployeeId) as ContractEmployeeAbsentCount FROM EmployeeAttendanceTable WHERE Type = ? AND date(Date) = ? AND Status = 'A' AND CompanyId = ?  AND RemoveStatus=0 ";
	
	String EMP_DAILYREPORTCOUNT="SELECT (SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND CompanyId = ? )as PermanentEmployeeCount," 
			+ "(SELECT COUNT(EmployeeId) FROM EmployeeAttendanceTable WHERE Type = ? AND CompanyId = ? )as TemporaryEmployeeCount,"
			+ "COUNT(EmployeeId) as ContractEmployeeCount FROM EmployeeAttendanceTable WHERE Type = ? AND Date = ? ";

	
	
	
	
	String EMP_MONTHLYID_SELECT="SELECT DISTINCT EmployeeId FROM EmployeeAttendanceTable WHERE month(Date) = ? AND year(Date) =  ? AND CompanyId = ? ";
	
	
	String EMP_MONTHLYREPORT="SELECT EA.EmployeeId,EA.Name,EA.CheckinTime,EA.CheckoutTime,date(Date) as Date,EA.TotalWorkHour,EA.Status,"  
       		+"E.Type,E.Department FROM EmployeeTable E INNER JOIN EmployeeAttendanceTable EA ON EA.EmployeeId=E.EmployeeId AND EA.CompanyId=E.CompanyId WHERE month(Date) = ? AND year(Date) = ? AND EA.CompanyId =? " ;


	String EMP_MONTHLYREPORTPRESENTCOUNT="SELECT COUNT(EmployeeId) AS PresentDays  FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND month(Date) = ? And year(Date) = ? AND Status = 'P' AND CompanyId =? ";

	String EMP_MONTHLYREPORTABSENTCOUNT="SELECT COUNT(EmployeeId) AS AbsentDays FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND month(Date) = ? And year(Date) = ? AND Status = 'A' AND CompanyId =? ";

	
	String EMP_SELECT_TOTALWORKHOUR="SELECT TotalWorkhour from EmployeeAttendanceTable where employeeid = ? and month(Date) = ? And year(Date) = ? and companyid = ? ";
	
	String EMP_MONTHLY_TOTALWORKHOUR="select (sec_to_time(sum(time_to_sec(totalworkhour)) )) As TotalWorkhour from EmployeeAttendanceTable where employeeid= ? and month(Date) = ? And year(Date) = ? and CompanyId = ?  ";
    
	  
	
	
	
	
	String EMP_PERIODREPORT="SELECT EA.EmployeeId,EA.Name,EA.CheckinTime,EA.CheckoutTime,date(Date) as Date,EA.Status,EA.TotalWorkHour,EA.Status,"
       		+"E.Type,E.Department FROM EmployeeTable E INNER JOIN EmployeeAttendanceTable EA ON EA.EmployeeId=E.EmployeeId AND EA.CompanyId=E.CompanyId WHERE date(Date) BETWEEN ? AND ? AND EA.CompanyId = ? ";


	String EMP_PERIODREPORTPRESENTCOUNT="SELECT COUNT(EmployeeId) AS PresentDays  FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND date(Date) between ? and ? AND Status = 'P' AND CompanyId =? ";

	String EMP_PERIODREPORTABSENTCOUNT="SELECT COUNT(EmployeeId)AS AbsentDays FROM EmployeeAttendanceTable WHERE EmployeeId = ? AND date(Date) BETWEEN ? AND ? AND Status = 'A' AND CompanyId = ? ";
	
	String EMP_SELECT_PERIODTOTALWORKHOUR="SELECT TotalWorkhour from EmployeeAttendanceTable where employeeid = ? and date(Date) between ? And  ? and companyid = ? ";

	String EMP_PERIOD_TOTALWORKHOUR="select (sec_to_time(sum(time_to_sec(totalworkhour))))AS TotalWorkHour  from EmployeeAttendanceTable where employeeid= ? and date(Date) between ? and ? and CompanyId =? ";
      
	
	
	
	
	 String EMP_REGULATION="UPDATE EmployeeAttendanceTable SET CheckinTime = ? ,"
	    		+ "CheckoutTime = ? WHERE EmployeeId = ? AND date(Date) = ? And CompanyId = ?  ";

	 String EMP_DEFAULT_ATTENDANCE="UPDATE EmployeeAttendanceTable SET Status = 'A' WHERE CheckoutTime IS NULL AND date(Date) = ? ";

	String EMP_GET_AUTHORIZATIONDETAILS="SELECT Authorization FROM EmployeeAttendanceTable WHERE Date = ? ";

	String SUP_AUTHORIZATION="UPDATE EmployeeAttendanceTable SET Authorization = ? WHERE date(Date) = ? And CompanyId = ? ";


	
	
	
	String EMP_ADD_DEPT="INSERT INTO EmployeeConfig (Department,CompanyId) VALUES(?,?) ";
	    
	String EMP_SELECT_DEPT="SELECT Department from EmployeeConfig WHERE Department = ? And CompanyId = ? ";
	    
	String EMP_SELECT_ROLE="SELECT Role FROM EmployeeConfig WHERE Role = ? And CompanyId = ? ";
	    
	//String  EMP_ADD_ROLE="UPDATE EmployeeConfig SET Role = ? WHERE Role is NULL ";
	    
	String EMP_ADD_ROLE1="INSERT INTO EmployeeConfig(Role,CompanyId)VALUES(?,?) ";
	    
	String EMP_SET_PERMISSION="UPDATE EmployeeConfig SET Permission =? WHERE Role = ? And CompanyId = ? ";

	
	

	String EMP_DELETE_DEPT="DELETE  FROM EmployeeConfig WHERE Department = ? And CompanyId = ? ";
    
    String EMP_DELETE_ROLE="DELETE FROM EmployeeConfig WHERE Role = ? And CompanyId = ? ";

  
    
    String EMP_VERIFY_MAIL = "SELECT EmailId FROM EmployeeTable where CompanyId = ?  And  EmailId  = ? " ; 
	
    String EMP_VERIFY_MOBILENO= "SELECT  MobileNo FROM EmployeeTable where CompanyId = ?  And MobileNo = ? " ; 
	
	String EMP_INSERT_SELECT = "SELECT MAX(EmployeeId) as EmployeeId FROM EmployeeTable where CompanyId = ?  " ; 
	
	
	
	
	String EMP_DEPT_SELECT = "SELECT EmployeeId FROM EmployeeTable WHERE Department = ? And Status = 0  And CompanyId = ? " ;

	String EMP_ROLE_SELECT = "SELECT EmployeeId FROM EmployeeTable WHERE Role = ? AND  Status = 0 And CompanyId = ? " ;


	
	
	
	String STORE_OTP="UPDATE EmployeeTable set Otp = ? where EmailId= ? or MobileNo = ? ";
    
	
	String OTP_VERIFY="SELECT Otp FROM   EmployeeTable where EmailId= ?";
    
 
	String UPADTE_NEW_PASSWORD="UPDATE EmployeeTable set Password = ? where EmailId= ?";
    
}
