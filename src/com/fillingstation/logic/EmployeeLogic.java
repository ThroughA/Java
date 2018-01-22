package com.fillingstation.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fillingstation.constants.IQueryConstants;
import com.fillingstation.json.EmployeeAttendanceJSON;
import com.fillingstation.json.EmployeeConfigJSON;
import com.fillingstation.json.EmployeeLoginJSON;
import com.fillingstation.json.EmployeeMaintenanceJSON;
import com.fillingstation.json.EmployeeReportAndCount;
import com.fillingstation.json.EmployeeResponseJSON;
import com.fillingstation.util.DBUtil;

public class EmployeeLogic {

	static String employeeRole=null;
	static String employeeId=null;
	static String employeeType=null;
	static String department=null;
	static String totalWorkHour;
	static String checkinTime = null;
	  static String checkoutTime = null;
	  static long second = 1000l;
	  static long minute = 60l * second;
	  static long hour = 60l * minute;
	  static Date checkinTimecal;
	  static Date checkoutTimecal;
	  static long difference;
	  static String hh;
	  static String mm;
	  static String ss;
	  static int validValue = 0;
	//  static String timeConstraint=String.format("%02d",6);
	  static String Status;
	  static String role;
	  static String dept;
	  
	
	/*
	 * Function for Login Verification using Email Id and Password 
	 */
	public static EmployeeReportAndCount loginVerify(EmployeeLoginJSON json)throws ParseException{
	    Connection connection=null;
	    EmployeeReportAndCount reportAndCount=new EmployeeReportAndCount();
	    ArrayList<EmployeeConfigJSON> employeeDepartmentlist=new ArrayList<EmployeeConfigJSON>() ;
        ArrayList<EmployeeConfigJSON> employeeRolelist=new ArrayList<EmployeeConfigJSON>();
        ArrayList<EmployeeConfigJSON> employeePermisionlist=new ArrayList<EmployeeConfigJSON>();
        
        ArrayList<EmployeeConfigJSON> employeeList=new ArrayList<EmployeeConfigJSON>();
        ArrayList<EmployeeConfigJSON> lockList=new ArrayList<EmployeeConfigJSON>();
        
        int lock;
        int block;
        String companyId;
        String employeeId;
    	String permission = null;
    	String companyName= null;
	       
	    reportAndCount.setEmployeeId("NOT_REGISTERED");//setting default value not Registered
		int valid=EmployeeLogic.vaildmailid(json);
		System.out.println("valid"+valid+" "+json.getEmailId()+" "+json.getPassword());	
		
	     if (valid==0)
	      {
	    	try {
			connection =DBUtil.getDBConnection();
			String querySelect=IQueryConstants.LOGIN_VERIFY;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		    preparedStmt.setString(1, json.getEmailId());
		    preparedStmt.setString(2, json.getEmailId());
		    
	        ResultSet result=preparedStmt.executeQuery();
	        
	        System.out.println("sandy" + result);
	        
	        
	        	while(result.next()) {
	        	String password=result.getString("Password");
	        	 employeeId=result.getString("EmployeeId");
	        	 companyId=result.getString("CompanyId");
	        	 	
	        	lock=result.getInt("LockValue");
	        	block=result.getInt("Block");
	          	
	          	System.out.println("verify"+json.getEmailId());
	          	
	          	
	    	    System.out.println("got the lock details for the entered employeeid............"+lock);
	    	    if(lock<3 && block==0) {
	    		
	        	//verify the password
	        		if((json.getPassword().equals(password))){
	        			
	        	//System.out.println((new StringBuilder("status ")).append(json.getPassword().toString()));
	        	System.out.println(json.getEmailId()+" "+json.getEmployeeId());	
	        	
	        	System.out.println("password for the employeeId is matched hence log in............");	
	        	String empId=result.getString("EmployeeId");
	        	 
	        	
	        	if(lock!=0) {
	      	    	System.out.println("got the lock value for the entered employeeid is not zero............"+lock);
	      	    	String querySelect11=IQueryConstants.EMP_RESET_LOCK;
	      			PreparedStatement preparedStmt11= connection.prepareStatement(querySelect11);
	      			preparedStmt11.setString(1,empId);
	      			preparedStmt11.setString(2,companyId);
	      			
	      			preparedStmt11.executeUpdate();
	      			System.out.println("updated the lock value to zero............");
	      	    }
	        	
	        	//Password is correct so returning their Filling StationId Details
	        	       	String firstname=result.getString("FirstName");
	        	       String lastname=result.getString("LastName");
	        	       String role=result.getString("Role");
	        	       permission=result.getString("Permission");
	        	       String name=firstname+" "+lastname;
	        	       String querySelect2=IQueryConstants.COMPANY_NAME;
	       			PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect2);
	       		    preparedStmt1.setString(1, companyId);
	       		    
	       	        ResultSet rs=preparedStmt1.executeQuery();
	       	        
	       	        	while(rs.next()) {
	       	        		companyName=rs.getString(1);
	       	        	}
					
					System.out.println("permission for the employee role is............"+permission);
				if(permission!=null) {
			    List<String> aList= Arrays.asList(permission.split(","));
				for(int i=0;i<aList.size();i++)
				{
				System.out.println("permission for the employee role in list is............"+aList.get(i));
				EmployeeConfigJSON empConf=new EmployeeConfigJSON();
				empConf.setPermission(aList.get(i));
				employeePermisionlist.add(empConf);
				}
				
				}     
	        	       
	        	       reportAndCount.setUserName(name);
	        	       reportAndCount.setEmployeeId(empId) ;
	        	       reportAndCount.setRole(role);
			       reportAndCount.setCompanyId(companyId);
			       reportAndCount.setCompanyName(companyName);
		        	  
	        	       System.out.println("entering  into process of getting permission for each role.......");
				    	
				        employeeDepartmentlist=EmployeeLogic.DeptDropDownDetails(companyId);
				    	employeeRolelist=EmployeeLogic.RoleDropDownDetails(companyId);
				    	employeeList=EmployeeLogic.EmployeeList(companyId);
				    	lockList=EmployeeLogic.LockList(companyId);
				    	
				    	//employeePermisionlist=EmployeeLogic.GetPermissionDetails(config);
				    	System.out.println("cjhecking"+reportAndCount.getEmployeeDepartmentlist());
				    	reportAndCount.setEmployeeDepartmentlist(employeeDepartmentlist);
				    	reportAndCount.setEmployeeRolelist(employeeRolelist);
				    	reportAndCount.setEmployeePermisionlist(employeePermisionlist);
				    	reportAndCount.setEmployeeList(employeeList);
				    	reportAndCount.setLockList(lockList);	    	
				    	System.out.println("completed employee permission successfully.......");
				     	
	        		}
	        	
	        	else {
     	    	   reportAndCount.setEmployeeId("PASSWORD_INCORRECT");
     	 			System.out.println("Password Incorrect");
     	 			//setting VALUE INCORRECT because he is existing user but password is incorrect
     	 			lock=lock+1;
     	 			String querySelect3=IQueryConstants.EMP_LOCK;
     	 			PreparedStatement preparedStmt3= connection.prepareStatement(querySelect3);
     	 			preparedStmt3.setInt(1,lock);
     	 			preparedStmt3.setString(2,employeeId);
     	 			preparedStmt3.setString(3,companyId);
     	 			
     	 			preparedStmt3.executeUpdate();
     	 	        System.out.println("updated lock value.......");

     	 			
     	 			
     	 		}
	    	 }else {
	    		 if(block==1) {
	    			 System.out.println("employeeId is Blocked............");	
		    			reportAndCount.setEmployeeId("BLOCKED");
	     	 			
	    		 }else {
	    			System.out.println("employeeId is locked............");	
	    			reportAndCount.setEmployeeId("LOCKED");
	    		 }	
	    		}
	        	}
  		
  	}
  	catch (Exception e) {
  		e.printStackTrace();
  	} finally {
  		DBUtil.closeConnection(connection);
  	}
	        	         System.out.println((new StringBuilder("status ")));
	        	       }

	       return reportAndCount; 
	       
	   }
	/*
	 * verifying where EmailID is Valid 
	 */
	
	public static int vaildmailid(EmployeeLoginJSON json) {
		
		int flag=1;//false
		Connection connection=null;
		
		try {
			connection=DBUtil.getDBConnection();
			String querySelect=IQueryConstants.VALID_MAILID;
			PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
			preparedStmt.setString(1, json.getEmailId());
			preparedStmt.setString(2, json.getEmailId());
	        ResultSet result=preparedStmt.executeQuery();
	       
	        if(result.next()) {
	        		        	flag=0; //set the flag value 0 if it is valid email id otherwise it return 1 means invalid
	        	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		  }finally {
			DBUtil.closeConnection(connection);
		    }
	        
	     return flag;   
    }
	
	
	
	
	/*
	 * function for sending dept in DB to the dropdown for getting the permission
	 */

	public static ArrayList<EmployeeConfigJSON> DeptDropDownDetails(String companyId) {
	
			ArrayList<EmployeeConfigJSON> employeeDepartmentlist = new ArrayList<EmployeeConfigJSON>();
	Connection connection=null;
		
		try {
			System.out.println("going to get department............");
			connection =DBUtil.getDBConnection();
			String querySelect=IQueryConstants.EMP_GET_DEPT;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			preparedStmt.setString(1,companyId);
			ResultSet rs=preparedStmt.executeQuery();
			  while(rs.next()) {
				  
			    	EmployeeConfigJSON deptObj=new EmployeeConfigJSON();
			    	String dept=rs.getString("Department");
			    	if(dept!=null) {
			    	deptObj.setDepartment(rs.getString("Department"));
			    	
			    	
			    	employeeDepartmentlist.add(deptObj);
			    	}
			    }

			    
			System.out.println("getting department was done successfully............");
			connection.close();
		}
		catch (SQLException e)
	    {
	    e.printStackTrace();
	    }
	     	
	   finally {
		DBUtil.closeConnection(connection);
	}

	return employeeDepartmentlist;	
	}

	/*
	 * function for sending role in DB to the dropdown for getting the permission
	 */
	
	public static ArrayList<EmployeeConfigJSON> RoleDropDownDetails(String companyId) {
		
		ArrayList<EmployeeConfigJSON> employeeRolelist = new ArrayList<EmployeeConfigJSON>();
		Connection connection=null;
			
			try {
				System.out.println("going to get department............");
				connection =DBUtil.getDBConnection();
				  String querySelect=IQueryConstants.EMP_GET_ROLE;
					PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
					preparedStmt.setString(1,companyId);
					
					ResultSet rs=preparedStmt.executeQuery();
					  while(rs.next()) {
					    	EmployeeConfigJSON roleObj=new EmployeeConfigJSON();
					    	String role=rs.getString("Role");
					    	if(role!=null) {
					    	roleObj.setRole(rs.getString("Role"));
					    	
					    	employeeRolelist.add(roleObj);
					    	}
					    }
					  
				   
				System.out.println("getting department was done successfully............");
				connection.close();
			}
			catch (SQLException e)
		    {
		    e.printStackTrace();
		    }
		     	
		   finally {
			DBUtil.closeConnection(connection);
		}

		return employeeRolelist;	

	}
	/*
	 * function for sending 
	 * lock employee list	 */
	
	public static ArrayList<EmployeeConfigJSON> LockList(String companyId) {
		
		ArrayList<EmployeeConfigJSON>   lockList = new ArrayList<EmployeeConfigJSON>();
		Connection connection=null;
			
			try {
				System.out.println("going to get department............");
				connection =DBUtil.getDBConnection();
				  String querySelect=IQueryConstants.EMP_LOCK_LIST;
					PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
					preparedStmt.setString(1,companyId);
					
					ResultSet rs=preparedStmt.executeQuery();
					  while(rs.next()) {
					    	EmployeeConfigJSON lockObj=new EmployeeConfigJSON();
					    	String lock=rs.getString("EmployeeId");
					    	if(lock!=null) {
					    		lockObj.setEmployeeId(rs.getString("EmployeeId"));
					    	
					    	lockList.add(lockObj);
					    	}
					    }
					  
				   
				System.out.println("getting department was done successfully............");
				connection.close();
			}
			catch (SQLException e)
		    {
		    e.printStackTrace();
		    }
		     	
		   finally {
			DBUtil.closeConnection(connection);
		}

		return lockList;	

	}



	/*
	 * function for getting EmployeeList
	 */
	
	public static ArrayList<EmployeeConfigJSON> EmployeeList(String companyId) {
		
		ArrayList<EmployeeConfigJSON> employeeList = new ArrayList<EmployeeConfigJSON>();
		Connection connection=null;
			
			try {
				System.out.println("going to get Employee List............");
				connection =DBUtil.getDBConnection();
				  String querySelect=IQueryConstants.EMP_LIST;
					PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
					preparedStmt.setString(1,companyId);
					
					ResultSet rs=preparedStmt.executeQuery();
					  while(rs.next()) {
					    	EmployeeConfigJSON emp=new EmployeeConfigJSON();
					    	
					    	emp.setEmployeeId(rs.getString("EmployeeId"));
					    	
					    	employeeList.add(emp);
					    	}   
				System.out.println("getting Employee List was done successfully............");
				connection.close();
			}
			catch (SQLException e)
		    {
		    e.printStackTrace();
		    }
		     	
		   finally {
			DBUtil.closeConnection(connection);
		}

		return employeeList;	

	}


	
	/*
	 * function for getting EmployeeList
	 */
	
	public static EmployeeReportAndCount RetreivePermission(EmployeeConfigJSON config) {
		
		EmployeeReportAndCount reportAndCount=new EmployeeReportAndCount();
	    
		ArrayList<EmployeeConfigJSON> employeePermisionlist=new ArrayList<EmployeeConfigJSON>();
        
		Connection connection=null;
		String permission=null;	
			try {
				System.out.println("going to get Permision List for Particular Role............");
				connection =DBUtil.getDBConnection();
				  String querySelect=IQueryConstants.ROLE_PERMISSION;
					PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
					preparedStmt.setString(1,config.getRole());
					preparedStmt.setString(2,config.getCompanyId());
					
					ResultSet rs=preparedStmt.executeQuery();
					  while(rs.next()) {
						  permission=rs.getString("Permission");
						  
		        	       }  
					  System.out.println("permission for the employee role is............"+permission);
						if(permission!=null) {
					    List<String> aList= Arrays.asList(permission.split(","));
						for(int i=0;i<aList.size();i++)
						{
						System.out.println("permission for the employee role in list is............"+aList.get(i));
						EmployeeConfigJSON empConf=new EmployeeConfigJSON();
						empConf.setPermission(aList.get(i));
						employeePermisionlist.add(empConf);
						}
						
						}     
			        	
						reportAndCount.setEmployeePermisionlist(employeePermisionlist);
				    	
				System.out.println("getting Employee List was done successfully............");
				connection.close();
			}
			catch (SQLException e)
		    {
		    e.printStackTrace();
		    }
		     	
		   finally {
			DBUtil.closeConnection(connection);
		}

		return reportAndCount;	

	}



	/*
	 * function for blocking the employee
	 */

	public static EmployeeAttendanceJSON BlockUnblock(EmployeeAttendanceJSON details) {
		int block=0;
		System.out.println("in employee block unblock ......");
		details.setEmployeeName("NOT_VALID");
		int valid=EmployeeIdValidation(details);
		if(valid==0) {
			
			System.out.println("entered employee id is valid...");	
				Connection connection=null;
				
				try {
					connection=DBUtil.getDBConnection();
					
					
					if(details.getBlock()==0) {
					
						String querySelect=IQueryConstants.EMP_SELECT_BLOCK;
						PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
						preparedStmt.setString(1,details.getEmployeeId());
						preparedStmt.setString(2,details.getCompanyId());
						
						ResultSet rs=preparedStmt.executeQuery();
						while(rs.next()) {
							block=rs.getInt("Block");
						}
						if(block==1) {
						
						String querySelect1=IQueryConstants.EMP_UNBLOCK;
						PreparedStatement preparedStmt1=connection.prepareStatement(querySelect1);
						preparedStmt1.setString(1,details.getEmployeeId());
						preparedStmt1.setString(2,details.getCompanyId());
						
						preparedStmt1.executeUpdate();	
						System.out.println("updating the block process since blocked already");
						details.setEmployeeName("");
						details.setStatus("UNBLOCKED");
						
						}
						else{
							System.out.println("Not blocked already");
							
							details.setEmployeeName("NOT_BLOCKED");
						}
						}
					else if(details.getBlock()==1){
					String querySelect=IQueryConstants.EMP_BLOCK;
					PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
					preparedStmt.setString(1,details.getEmployeeId());
					preparedStmt.setString(2,details.getCompanyId());
					
					preparedStmt.executeUpdate();
					System.out.println("blocked the employeeId");
					details.setEmployeeName("");
					details.setStatus("BLOCKED");
					
					
					connection.close();
					}
					} catch (Exception e) {
					e.printStackTrace();
				  }finally {
					DBUtil.closeConnection(connection);
				    }
			}
		
	System.out.println("completed block unblock changes for the entered employeeid for the ...");	
		return details;
		
	}
	

		
	/*
	 * function for unlocking the locked employeeId
	 */

	public static EmployeeAttendanceJSON UnLock(EmployeeAttendanceJSON details) {
		System.out.println("going to unlock the locked employeeId");
		Connection connection=null;
		int vaildValue = 0;
		int lock=0;
		System.out.println("got the employee details for validation............");
		connection =DBUtil.getDBConnection();
		vaildValue=EmployeeIdValidation(details);
		if(vaildValue==0) {
	    
			 System.out.println("valid employee returing to unblock............");	
	    try {
		 String querySelect=IQueryConstants.EMP_SELECT_LOCK;
			PreparedStatement preparedStmt= connection.prepareStatement(querySelect);
			preparedStmt.setString(1,details.getEmployeeId());
			preparedStmt.setString(2,details.getCompanyId());
			ResultSet rs=preparedStmt.executeQuery();
		    while(rs.next()) {
		    	lock=rs.getInt("LockValue");
		    }
		    System.out.println("got the lock details for the entered employeeid............"+lock);
	   if(lock==3) {
		   System.out.println(" entered employeeid is locked............"+lock);
		   System.out.println(" going to unlock the locked employeeId............");
		   String querySelect1=IQueryConstants.EMP_RESET_LOCK;
			PreparedStatement preparedStmt1= connection.prepareStatement(querySelect1);
			preparedStmt1.setString(1,details.getEmployeeId());
			preparedStmt1.setString(2,details.getCompanyId());
			preparedStmt1.executeUpdate();
			System.out.println("updated the lock value to zero............");
			details.setEmployeeName("UNLOCKED");
	  
	   }else {
		   System.out.println("entered employeeid is not locked........................");
		   details.setEmployeeName("NOT_LOCKED");
			  
	   }  
	    }catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	DBUtil.closeConnection(connection);
	    }
	}else {
		 System.out.println("invalid employee Id............");	
	}
return details;

	}


	
	
	/*
	 * function to add new employee
	 */
	
	public static EmployeeMaintenanceJSON AddEmployee(EmployeeMaintenanceJSON details) {
		Connection connection=null;
		int employeeNo = 0;
		String email=null;
		Long mobileNo=null;
		try {
			
			System.out.println("got the employee details to be added............");
			connection =DBUtil.getDBConnection();
			
			String querySelect0=IQueryConstants.EMP_VERIFY_MAIL;
			PreparedStatement preparedStmt0 = connection.prepareStatement(querySelect0);
			preparedStmt0.setString(1,details.getCompanyId());
			preparedStmt0.setString(2,details.getEmailId());
			
			ResultSet rs0=preparedStmt0.executeQuery();
			
			while(rs0.next()) {
				email=rs0.getString("EmailId");
				System.out.print("MobileNo Already exits "+email );
				
				
			}
			String querySelect01=IQueryConstants.EMP_VERIFY_MOBILENO;
			
			PreparedStatement preparedStmt01 = connection.prepareStatement(querySelect01);
			preparedStmt01.setString(1,details.getCompanyId());
			preparedStmt01.setLong(2,details.getMobileNo());
			
			ResultSet rs01=preparedStmt01.executeQuery();
			
			while(rs01.next()) {
				mobileNo=rs01.getLong("MobileNo");
				System.out.print("MobileNo Already exits "+mobileNo );
				
			}
			
			if(email==null && mobileNo==null) {
			
			String querySelect=IQueryConstants.EMP_INSERT_SELECT;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			preparedStmt.setString(1,details.getCompanyId());
			ResultSet rs=preparedStmt.executeQuery();
			while(rs.next()) {
				employeeNo=rs.getInt("EmployeeId");
			}
			System.out.println("got the employee details to be added............"+employeeNo);
			
			int employeeId1=employeeNo+1;
			String employeeId = String.format("%03d", employeeId1);
			
			
			String querySelect1=IQueryConstants.EMP_INSERT;
			PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
			preparedStmt1.setString(1, details.getFirstName());
			preparedStmt1.setString(2,details.getLastName());
			preparedStmt1.setString(3,details.getDob());
			preparedStmt1.setString(4,details.getEmailId());
			preparedStmt1.setLong(5,details.getMobileNo());
			preparedStmt1.setString(6,details.getAddress());
			preparedStmt1.setString(7,details.getEmployeeType());
			preparedStmt1.setString(8,details.getRole());
			preparedStmt1.setString(9,details.getDepartment());
			preparedStmt1.setString(10,details.getProofType());
			preparedStmt1.setString(11,details.getProofNo());
			preparedStmt1.setString(12,details.getCompanyId());
			preparedStmt1.setString(13,employeeId);
			    
			preparedStmt1.executeUpdate();
			    System.out.println("completed adding employee returing to webservice............");
			    System.out.println("added employee successfully.......");
			    String querySelect2=IQueryConstants.EMP_ID;
				PreparedStatement preparedStmt2= connection.prepareStatement(querySelect2);
				preparedStmt2 = connection.prepareStatement(querySelect2);
				preparedStmt2.setString(1, details.getProofNo());
				ResultSet result=preparedStmt2.executeQuery();
			    // Returning the Corresponding VendorCode to Vendor
			    while(result.next()) {
			       String empId=result.getString("EmployeeId");
			       details.setEmployeeId(empId);
			    }
			    connection.close(); 
			    
			}else if(mobileNo!=null) {
			details.setEmployeeId("MOBILE");
			System.out.print("MobileNo Already exits");
				
			}else if(email!=null) {
				details.setEmployeeId("EMAIL");
				System.out.print("Email Already exits");
				
				
				}
        } catch (SQLException e)
        {
        e.printStackTrace();
        }
         	
	   finally {
		DBUtil.closeConnection(connection);
	}
		return details;
		
	}
	
/*
 *Retreving Employee Details
 */
	public static EmployeeMaintenanceJSON EmployeeDetail(EmployeeMaintenanceJSON details) {
		Connection connection=null;
		try {
			String empId=null;
			System.out.println("Retreiving the Employee Details............");
			connection =DBUtil.getDBConnection();
			 	String querySelect1=IQueryConstants.EMP_DETAILS;
			    PreparedStatement preparedStmt = connection.prepareStatement(querySelect1);
				preparedStmt.setString(1, details.getEmployeeId());
				preparedStmt.setString(2, details.getCompanyId());
				ResultSet result=preparedStmt.executeQuery();
			    // Returning the Corresponding VendorCode to Vendor
			    while(result.next()) {
			        empId=result.getString("EmployeeId");
			       String firstName=result.getString("FirstName");
			       String lastName=result.getString("LastName");
			       String dept=result.getString("Department");
			       String role=result.getString("Role");
			       System.out.println("Retreiving the Employee Details............"+role +" "+dept);
					
			       String employeeName=firstName+" "+lastName;
			       details.setEmployeeId(empId);
			       details.setEmployeeName(employeeName);
			       details.setDepartment(dept);
			       details.setRole(role);
				     
			       
			    }
			    System.out.println("Returning  Employee Details"+ empId);
			    connection.close(); 		 
        
		
				} catch (SQLException e)
        {
        e.printStackTrace();
        }
         	
	   finally {
		DBUtil.closeConnection(connection);
	}
		return details;
}
	
	/*
	 * function to delete employee
	 */
	
	public static void DeleteEmployee(EmployeeMaintenanceJSON details) {
		
		Connection connection=null;
		try {
			
			System.out.println("got the employee details to be deleted............");
			connection =DBUtil.getDBConnection();
			String querySelect=IQueryConstants.EMP_DELETE;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			preparedStmt.setString(1, details.getEmployeeId());
		    
			    preparedStmt.setInt(1,1);
			    preparedStmt.setString(2, details.getEmployeeId());
			    
			    preparedStmt.setString(3, details.getCompanyId());
			    preparedStmt.executeUpdate();
			    System.out.println("completed deleting remployee returing to webservice............"+details.getCompanyId()+ details.getEmployeeId());
			    connection.close(); 		 
        } catch (SQLException e)
        {
        e.printStackTrace();
        }
         	
	   finally {
		DBUtil.closeConnection(connection);
	}
		
	}
	
	
	
	
	/*
	 *Retreving Employee Details of editing
	 */
		public static EmployeeMaintenanceJSON EditEmployee(EmployeeMaintenanceJSON details) {
			Connection connection=null;
			try {

				String empId=null;
				System.out.println("Going to give  new EmployeeId ............");
				connection =DBUtil.getDBConnection();
				 	String querySelect1=IQueryConstants.EMP_DETAILS;
				    PreparedStatement preparedStmt = connection.prepareStatement(querySelect1);
					preparedStmt.setString(1, details.getEmployeeId());
					preparedStmt.setString(2, details.getCompanyId());
					ResultSet result=preparedStmt.executeQuery();
				    
				    while(result.next()) {
				        empId=result.getString("EmployeeId");
				       details.setEmployeeId(empId);
				       details.setFirstName(result.getString("FirstName"));
				       details.setLastName(result.getString("LastName"));
				       details.setDob(result.getString("DOB"));
				       details.setRole(result.getString("Role"));
				       details.setEmailId(result.getString("EmailId"));
				       details.setMobileNo(result.getLong("MobileNO"));
				       details.setAddress(result.getString("Address"));
				       details.setEmployeeType(result.getString("Type"));
				       details.setDepartment(result.getString("Department"));
				       details.setProofType(result.getString("EmployeeProofType"));
				       details.setProofNo(result.getString("EmployeeProofNum"));
				       
				    }
				    System.out.println("Returning  Employee Details"+ empId + "for Edit");
				    connection.close(); 		 
	        
				
			
				} catch (SQLException e)
	        {
	        e.printStackTrace();
	        }
	         	
		   finally {
			DBUtil.closeConnection(connection);
		}
			return details;
	}

	/*
	 * function to update employee
	 */
	
public static EmployeeMaintenanceJSON UpdateEmployee(EmployeeMaintenanceJSON details) {
		
		Connection connection=null;
		try {
			int blockValue=BlockValidation(details.getCompanyId(),details.getEmployeeId());
			if(blockValue==0) {
				
			System.out.println("got the employee details to be updated............");
			connection =DBUtil.getDBConnection();
			String querySelect=IQueryConstants.EMP_UPDATE;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			    preparedStmt.setString(1,details.getEmployeeId());
			    preparedStmt.setString(2,details.getFirstName());
				preparedStmt.setString(3,details.getLastName());
				preparedStmt.setString(4,details.getDob());
				preparedStmt.setString(5,details.getEmailId());
				preparedStmt.setLong(6,details.getMobileNo());
				preparedStmt.setString(7,details.getAddress());
			    preparedStmt.setString(8,details.getEmployeeType());
			    preparedStmt.setString(9,details.getRole());
			    preparedStmt.setString(10,details.getDepartment());
			    preparedStmt.setString(11,details.getEmployeeType());
			    preparedStmt.setString(12,details.getProofNo());
			    preparedStmt.setString(13,details.getEmployeeId());
			    preparedStmt.setString(14,details.getCompanyId());
			    preparedStmt.executeUpdate();
			    System.out.println("completed updating employee returing to webservice............");
			    connection.close(); 
			    
			}else {
				details.setEmployeeName("BLOCKED");
				System.out.println("Employee ID is BLocked");
			}
        } catch (SQLException e)
        {
        e.printStackTrace();
        }
         	
	   finally {
		DBUtil.closeConnection(connection);
	}
		return details;
	}
/*
 * function for searching an employee
 */

public static ArrayList<EmployeeMaintenanceJSON> SearchEmployee(EmployeeMaintenanceJSON details) {
	Connection connection=null;
	ArrayList<EmployeeMaintenanceJSON> employeeRetrievelist = new ArrayList<EmployeeMaintenanceJSON>();
	try {
		System.out.println("getting the employee details for searching............");
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SEARCH;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		    preparedStmt.setString(1,details.getSearch());
		    preparedStmt.setString(2,details.getSearch());
		    preparedStmt.setString(3,details.getSearch());
		    preparedStmt.setString(4,details.getSearch());
		    preparedStmt.setString(5,details.getSearch());
		    preparedStmt.setString(6,details.getSearch());
		    preparedStmt.setString(7,details.getSearch());
		    preparedStmt.setString(8,details.getCompanyId());
		    ResultSet rs=preparedStmt.executeQuery();
	        while(rs.next())
	        {
	        	EmployeeMaintenanceJSON searchEmpDetails = new EmployeeMaintenanceJSON();
	        	searchEmpDetails.setEmployeeId(rs.getString("EmployeeId"));
	        String firstName=rs.getString("FirstName");
	        String lastName=rs.getString("LastName");
	        String employeeName=firstName+ " "+lastName;
	        	searchEmpDetails.setEmployeeName(employeeName);
	        	searchEmpDetails.setEmployeeType(rs.getString("Type"));
	        	searchEmpDetails.setDepartment(rs.getString("Department"));
	        	searchEmpDetails.setRole(rs.getString("Role"));
	        	searchEmpDetails.setAddress(rs.getString("Address"));
	        	searchEmpDetails.setMobileNo(rs.getLong("MobileNo"));
	        	employeeRetrievelist.add(searchEmpDetails);
	        }
		    System.out.println("completed searching returing to webservice............");
		    connection.close(); 	 
    } catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return employeeRetrievelist;
}
  




/*
 * function to insert employee into DB 
 * on clicking the checkin button
 */
public static int EmployeeCheckin(EmployeeAttendanceJSON details) {
	
	Connection connection=null;
	String firstName = null;
	String lastName = null;
	String name = null;
	int valid = 0;
	int blockValue=0;
	
	try {
		
		System.out.println("got the employee details for checkin from employeetable............");
		connection =DBUtil.getDBConnection();
		details.setEmployeeName("NOT_VAILD");
		valid=vaildEmployeeId(details);
		if(valid==0) {
			
			blockValue=BlockValidation(details.getCompanyId(),details.getEmployeeId());
			
			if(blockValue==0) {
				
				
			validValue=CheckinValidation(details);
			if(validValue==0) {
			System.out.println("the entered employeeid is valid....");
			System.out.println("updating the employee checkin details........");
			String querySelect=IQueryConstants.EMP_CHECKIN;
			PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			    preparedStmt.setString(1,details.getCheckInTime());
			    preparedStmt.setString(2,details.getEmployeeId());
			    preparedStmt.setString(3,details.getDate());
			    preparedStmt.setString(4,details.getCompanyId());
			    preparedStmt.executeUpdate();
			    System.out.println("updated the employee checkin details........");
			    details.setEmployeeName("");
			}else {
				details.setEmployeeName("ALREADY_CHECKIN");
				System.out.println("Already checkedIn");
			}
	    
			
		    System.out.println("completed checkin returing to webservice............");
		 
			connection.close(); 
			
			}
			else {
				details.setEmployeeName("BLOCKED");
				System.out.println("employee id is blocked............");
			}
		}else {
			System.out.println("EmployeeId"+details.getEmployeeId()+" is not valid");
		}
		
		} catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return valid;
}
	
	
	/*
	 * function for checking whether the employee is blocked or not
	 */

	private static int BlockValidation(String companyId,String employeeId) {
		
		int flag=0;//TRue
		Connection connection=null;
		
		try {
			connection=DBUtil.getDBConnection();
			String querySelect=IQueryConstants.EMP_SELECT_BLOCK;
			PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
			preparedStmt.setString(1,employeeId);
			preparedStmt.setString(2,companyId);
			
	        ResultSet result=preparedStmt.executeQuery();
	       
	        while(result.next()) {
	        	int block=result.getInt("Block");
	        	if(block==1) {
	        	flag=1; //set the flag value 0 if it is valid email id otherwise it return 1 means invalid
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		  }finally {
			DBUtil.closeConnection(connection);
		    }
	        
	     return flag;   

	}


/*
 *Attendance
 */
public static ArrayList<EmployeeResponseJSON> Attendance(EmployeeAttendanceJSON details) {
	ArrayList<EmployeeResponseJSON> employeeResponse= new ArrayList<EmployeeResponseJSON>();
	Connection connection=null;
	
	
	try {
		
		System.out.println("Retrieving Employee Attendance");
		connection =DBUtil.getDBConnection();
		
		   
		    String querySelect=IQueryConstants.EMPLOYEE_ATTENDANCE;
		    PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
			
			preparedStmt.setString(1, details.getDate());
			preparedStmt.setString(2, details.getCompanyId());
			System.out.println(details.getDate());
        	
		    ResultSet result=preparedStmt.executeQuery();
	        while(result.next())
	        {
	        	EmployeeResponseJSON retrieveobj = new EmployeeResponseJSON();    	
	        	retrieveobj.setEmployeeId(result.getString("EmployeeId"));
	        	retrieveobj.setEmployeeName(result.getString("Name"));
	        	retrieveobj.setCheckIn(result.getString("CheckinTime"));
	        	retrieveobj.setCheckOut(result.getString("CheckoutTime"));
	        	retrieveobj.setStatus(result.getString("Status"));
	        	
	        	retrieveobj.setMobileNo(result.getLong("MobileNo"));
	        	System.out.println(result.getString("EmployeeId")+" "+result.getString("Name"));
	        	employeeResponse.add(retrieveobj);
	        }
		    connection.close(); 
    
		
		} catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	
	return employeeResponse;
}




/*
 * function to validate employeeid before delete and update
 */

private static int EmployeeIdValidation(EmployeeAttendanceJSON details) {

int flag=0;//TRue
Connection connection=null;

try {
	connection=DBUtil.getDBConnection();
	String querySelect=IQueryConstants.EMP_VALIDATION;
	PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
	preparedStmt.setString(1,details.getEmployeeId());
	preparedStmt.setString(2,details.getCompanyId());
    ResultSet result=preparedStmt.executeQuery();
   
    if(!result.next()) {
    	flag=1; //set the flag value 0 if it is valid email id otherwise it return 1 means invalid
    	
    }
} catch (Exception e) {
	e.printStackTrace();
  }finally {
	DBUtil.closeConnection(connection);
    }
	return flag;
}


	
/*
 * Checking whether employee id is valid for checkin and checkout
 */

public static int vaildEmployeeId(EmployeeAttendanceJSON details) {
	
	int flag=0;//TRue
	Connection connection=null;
	
	try {
		connection=DBUtil.getDBConnection();
		String querySelect=IQueryConstants.VALID_EMP_ID;
		PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
		preparedStmt.setString(1,details.getEmployeeId());
		preparedStmt.setString(2,details.getDate());
		preparedStmt.setString(3,details.getCompanyId());
		
        ResultSet result=preparedStmt.executeQuery();
       
        if(!result.next()) {
        	flag=1; //set the flag value 0 if it is valid email id otherwise it return 1 means invalid
        	
        }
	} catch (Exception e) {
		e.printStackTrace();
	  }finally {
		DBUtil.closeConnection(connection);
	    }
        
     return flag;   
}

	
/*
 * function to calculate the total work hour on clicking the checkout button
 * after calculation checkouttime and totl work hour will be updated into DB
 */

public static int EmployeeCheckout(EmployeeAttendanceJSON details) throws ParseException {
	
	Connection connection=null;
	try {
		System.out.println("getting the employee details for calculating total working hours............");
		connection =DBUtil.getDBConnection();
		details.setEmployeeName("NOT_VAILD");
		int valid=vaildEmployeeId(details);
		if (valid==0) {
			int validValue1=CheckoutValidation(details);
			if(validValue1==0) {
			
		validValue=CheckinValidation(details);
		System.out.println("return flag value........."+validValue);
		if(validValue==1)
			{ 
			System.out.println("calling totalwork calculation function..........");
			TotalWorkCalculation(details,connection);
			}
		else {
			details.setEmployeeName("NOT_CHECKED_IN");
		    
			System.out.println("employee not checked in so checkout is not permitted..............");
		}
	}else{
		details.setEmployeeName("ALREADY_CHECKOUT");
		System.out.println("employee Id "+details.getEmployeeId()+ " is already checked out............");
		
	}
			
	}
		else {
		
			System.out.println("employee Id "+details.getEmployeeId()+ " is not valid............");
		}	
    } catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return validValue;
}

/*
 * 
 */
public static void TotalWorkCalculation(EmployeeAttendanceJSON details,Connection connection) throws SQLException, ParseException {
	
	 System.out.println("in getting checkin time............");
		String querySelect=IQueryConstants.EMP_SELECT_CHECKINTIME;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		    preparedStmt.setString(1,details.getEmployeeId());
		    preparedStmt.setString(2,details.getDate());
		    preparedStmt.setString(3,details.getCompanyId());
		    ResultSet rs=preparedStmt.executeQuery();
		    while(rs.next()) {
		    	checkinTime=rs.getString("CheckinTime");
		    }
		    System.out.println("got the checkintime employee details for calculating checkout............"+checkinTime);
		    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		    System.out.println("calculating totalworking hour............");
		    checkoutTime=details.getCheckOutTime();
		    System.out.println("checkout time............"+checkoutTime);
		    checkinTimecal=format.parse(checkinTime);
		    System.out.println("checkin time cal............"+checkinTimecal);
		    checkoutTimecal=format.parse(checkoutTime);
		    System.out.println("checkout time cal............"+checkoutTimecal);
			difference = checkoutTimecal.getTime() - checkinTimecal.getTime();
			System.out.println("difference in  time ............"+difference);
			hh=String.format("%02d",difference / hour);
			mm=String.format("%02d",(difference % hour) / minute) ;
			ss=String.format("%02d",(difference % minute) / second);
			totalWorkHour=hh+":"+mm+":"+ss;
		    System.out.println("completed calculating  totalworkhour............"+totalWorkHour);
		 //   int result=totalWorkHour.compareTo(timeConstraint);
		  /* System.out.println("condition for present or absent............"+result);
		    if(result>0) {
		    	Status="P";
		    }
		    	else {
		    		Status="A";
		    	}
		    	*/
	    	System.out.println("got the employee details for checkout fully............");
	String querySelect1=IQueryConstants.EMP_CHECKOUTUPDATE;
	PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
	    preparedStmt1.setString(1,details.getCheckOutTime());
		preparedStmt1.setString(2,totalWorkHour);
		//preparedStmt1.setString(3,Status);
	    preparedStmt1.setString(3,details.getEmployeeId());
	    preparedStmt1.setString(4,details.getDate());
	    preparedStmt1.setString(5,details.getCompanyId());
	    preparedStmt1.executeUpdate();
	    System.out.println("completed checking out updation returing to webservice............");
	    details.setEmployeeName("");
	    connection.close(); 
	

}

/*
 * function to check whether employee is checked in before checking out,
 * checking whether the employee is checked in already to prevent more than one checkin and
 * checkout without checkin once 
 */

private static int CheckinValidation(EmployeeAttendanceJSON details) {

	int flag = 1;
	Connection connection=null;
	String checkinTime = null;
	try {
		System.out.println("in checkIn validaation..........");
		connection=DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SELECT_CHECKINTIME;
		PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
		preparedStmt.setString(1,details.getEmployeeId());
		preparedStmt.setString(2,details.getDate());
		preparedStmt.setString(3,details.getCompanyId());
        ResultSet rs=preparedStmt.executeQuery();
       while(rs.next()) {
    	   checkinTime=rs.getString("CheckinTime");
    	   System.out.println("checkIn"+checkinTime);
       }
       if(checkinTime.equals("-")) {
    	   flag=0;
    	   System.out.println("checkIn"+flag);
           
       }
       else {
    	   flag=1;
       }
        
	} catch (Exception e) {
		e.printStackTrace();
	  }finally {
		DBUtil.closeConnection(connection);
	    }
	System.out.println("result flag..."+flag);
	   
	return flag;
}

/*
* function for checkout validation
*/
private static int CheckoutValidation(EmployeeAttendanceJSON details) {
	
	int flag = 1;
	Connection connection=null;
	String checkinTime = null;
	try {
		System.out.println("in checkout validaation..........");
		connection=DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SELECT_CHECKOUTTIME;
		PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
		preparedStmt.setString(1,details.getEmployeeId());
		preparedStmt.setString(2,details.getDate());
		preparedStmt.setString(3,details.getCompanyId());
        ResultSet rs=preparedStmt.executeQuery();
       while(rs.next()) {
    	   checkoutTime=rs.getString("CheckoutTime");
       }
       if(checkoutTime.equals("-")) {
    	   flag=0;
       }
       else {
    	   flag=1;
       }
        
	} catch (Exception e) {
		e.printStackTrace();
	  }finally {
		DBUtil.closeConnection(connection);
	    }
        
	return flag;
}


/*
* function for checking supervisor authorization
*/

public static int Authorization(EmployeeMaintenanceJSON details) {
	
	Connection connection=null;
	try {
		System.out.println("going to set supervisor authorization............");
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.SUP_AUTHORIZATION;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		    preparedStmt.setInt(1,1);
		    preparedStmt.setString(2,details.getDate());
		    preparedStmt.setString(3,details.getCompanyId());
		    
		    preparedStmt.executeUpdate();
		    System.out.println("supervisor authorization done successfully............");
		    connection.close();
	}
	catch (SQLException e)
   {
   e.printStackTrace();
   }
    	
  finally {
	DBUtil.closeConnection(connection);
}
	return 0;
}

public static EmployeeAttendanceJSON TimeRegulation(EmployeeAttendanceJSON details) throws ParseException {
	
	//String checkinTime = null;
		int blockValue=BlockValidation(details.getCompanyId(),details.getEmployeeId());
		if(blockValue==0) {
		
		System.out.println("entered employee id is valid...");	
			Connection connection=null;
			
			try {
				connection=DBUtil.getDBConnection();
				String querySelect=IQueryConstants.EMP_REGULATION;
				PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
				preparedStmt.setString(1,details.getCheckInTime());
				preparedStmt.setString(2,details.getCheckOutTime());
				preparedStmt.setString(3,details.getEmployeeId());
				preparedStmt.setString(4,details.getDate());
				preparedStmt.setString(5,details.getCompanyId());
			    
				preparedStmt.executeUpdate();
				
				TotalWorkCalculation(details,connection);		 
				connection.close();
		        } catch (Exception e) {
				e.printStackTrace();
			  }finally {
				DBUtil.closeConnection(connection);
			    }
			EmployeeCheckout(details);
		}else {
			details.setEmployeeId("BLOCKED");//blocked
			System.out.println("entered employee id is Blocked...");	
			
		}
		
System.out.println("completed the time regulation changes for the entered employeeid for the ...");	
	return details;
}




/*
 * function for adding new department
 */

public static EmployeeConfigJSON AddDeprtment(EmployeeConfigJSON config) {
	
	Connection connection=null;
	connection =DBUtil.getDBConnection();
	
	try {
		
		
		String querySelect=IQueryConstants.EMP_SELECT_DEPT;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1, config.getDepartment());
		preparedStmt.setString(2, config.getCompanyId());    
		
		ResultSet rs = preparedStmt.executeQuery();
		
		if(rs.next()) {
			config.setAuthorization("DUPLICATE");
			}
		else{
			
			
			System.out.println("going to add department since new...");
			System.out.println("going to add department............");
			String querySelect1=IQueryConstants.EMP_ADD_DEPT;
			PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
			preparedStmt1.setString(1, config.getDepartment());
			preparedStmt1.setString(2, config.getCompanyId());    
			
			preparedStmt1.executeUpdate();
			    System.out.println("adding a new department was done successfully............");
			    config.setAuthorization("");
			    connection.close();
		}
	}
		catch (SQLException e)
	    {
	    e.printStackTrace();
	    }
	     	
	   finally {
		DBUtil.closeConnection(connection);
	}
		
	
		return config;
		}




/*
 * checking whether department is already present
 */

public static int DeprtmentValidation(EmployeeConfigJSON config) {
	
	ArrayList<EmployeeConfigJSON> employeeDepartmentlist = new ArrayList<EmployeeConfigJSON>();
int flag=0;
	Connection connection=null;
	try {
		System.out.println("going to validate department............");
		
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SELECT_DEPT;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1, config.getDepartment());
		preparedStmt.setString(2, config.getCompanyId());    
		
		ResultSet rs=preparedStmt.executeQuery();
		
		    while(rs.next()) {
		    	EmployeeConfigJSON deptObj=new EmployeeConfigJSON();
		    	deptObj.setDepartment(rs.getString("Department"));
		    	employeeDepartmentlist.add(deptObj);
		    }
		    for(EmployeeConfigJSON dept:employeeDepartmentlist) {
		    	System.out.println("available department.....\n"+dept.getDepartment());
		    	if((config.getDepartment()).equals(dept.getDepartment())){
		    		System.out.println("department already exist........\n"+flag);
		    		flag=1;
		    	}else {
		    		System.out.println("department doesnot exist........\n"+flag);
		    		
		    	}
		    }
		    connection.close();
	}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return flag;
}



/*
 * function for adding new role
 */

public static EmployeeConfigJSON AddRole(EmployeeConfigJSON config) {
	Connection connection=null;
	String role = null;
		System.out.println("going to add role since new...");
	try {
		System.out.println("going to add role............");
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SELECT_ROLE;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1,config.getRole());
		preparedStmt.setString(2,config.getCompanyId());
		
		    ResultSet rs=preparedStmt.executeQuery();
		    if(rs.next()) {
		    	role=rs.getString("Role");
		    	System.out.println("role..........\n"+role );
		    	config.setAuthorization("DUPLICATE"); 
		    
		    }
		if(role==null) {
			 System.out.println("adding a new role inside null............");

		String querySelect1=IQueryConstants.EMP_ADD_ROLE1;
		PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
		preparedStmt1.setString(1,config.getRole());
		preparedStmt1.setString(2,config.getCompanyId());
		    preparedStmt1.executeUpdate();
		    config.setAuthorization(""); 
		    System.out.println("adding a new role was done successfully in null............");
		  
	}	    
			    
			    	}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	
		   
		
	
	return config;
	}

/*
 * checking whether role in a department is existing already 
 */

public static int RoleValidation(EmployeeConfigJSON config) {
	
	ArrayList<EmployeeConfigJSON> employeeRolelist = new ArrayList<EmployeeConfigJSON>();
int flag=0;
	Connection connection=null;
	try {
		System.out.println("going to validate role............");
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SELECT_ROLE;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1,config.getCompanyId());
		
		    ResultSet rs=preparedStmt.executeQuery();
		    while(rs.next()) {
		    	
		    	EmployeeConfigJSON deptObj=new EmployeeConfigJSON();
		    	deptObj.setRole(rs.getString("Role"));
		    	employeeRolelist.add(deptObj);
		    }
		    for(EmployeeConfigJSON role:employeeRolelist) {
		    	System.out.println("available role.....\n"+role.getRole());
		    	if((config.getRole()).equals(role.getRole())){
		    		flag=1;
		    		System.out.println("role already exist........\n"+flag);
		    	}else {
		    		System.out.println("role doesnot exist........\n"+flag);
		    		
		    	}
		    }
		    connection.close();
	}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return flag;
}

/*
 * function to set permission
 */

public static ArrayList<EmployeeConfigJSON> SetPermission(EmployeeConfigJSON config) {
	Connection connection=null;
	ArrayList<EmployeeConfigJSON> employeePermisionlist=new ArrayList<EmployeeConfigJSON>();
	String permission = null;
	
	
	try {
		System.out.println("going to set permission............");
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.EMP_SET_PERMISSION;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1,config.getPermission());
		preparedStmt.setString(2,config.getRole());
		preparedStmt.setString(3,config.getCompanyId());
		
		preparedStmt.executeUpdate();
		 permission=config.getPermission();
	      
		if(config.getPermission()!=null) {
		    List<String> aList= Arrays.asList(permission.split(","));
			for(int i=0;i<aList.size();i++)
			{
			System.out.println("permission for the employee role in list is............"+aList.get(i));
			EmployeeConfigJSON empConf=new EmployeeConfigJSON();
			empConf.setPermission(aList.get(i));
			employeePermisionlist.add(empConf);
		
			}
		}
		System.out.println("setting permission was done successfully............");
		connection.close();
	}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	return employeePermisionlist;
	
	}
/*
 * function for deleting the department
 */

public static EmployeeConfigJSON DeleteDeprtment(EmployeeConfigJSON config) {
	
	Connection connection=null;
		System.out.println("going to delete department since existing...");
	try {
		System.out.println("going to delete department............");
		connection =DBUtil.getDBConnection();
		System.out.println("going to see whether employee is present under the department to be deleted");
		String querySelect=IQueryConstants.EMP_DEPT_SELECT;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1, config.getDepartment());
		preparedStmt.setString(2, config.getCompanyId());
		
		ResultSet rs=preparedStmt.executeQuery();
		while(rs.next()) {
			employeeId=rs.getString("EmployeeId");
			System.out.println("emp"+employeeId);
			
			
		}
		if(employeeId==null) {
		
		String querySelect1=IQueryConstants.EMP_DELETE_DEPT;
		PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
		preparedStmt1.setString(1, config.getDepartment());
		preparedStmt1.setString(2, config.getCompanyId());
		
		preparedStmt1.executeUpdate();
		    System.out.println("deleting a department was done successfully............");
		 config.setAuthorization("DELETED");
		   	}else {
			System.out.println("department cannot be deleted since it has employee or role");
			config.setAuthorization("EMPLOYEE_EXIST");
			 
		}
		 connection.close();
			
		}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	
	return config;
	}



/*
 * function for deleting the role
 */
public static EmployeeConfigJSON DeleteRole(EmployeeConfigJSON config) {
	Connection connection=null;
		System.out.println("going to deleete role since existing...");
	try {
		System.out.println("going to delete role............");
		connection =DBUtil.getDBConnection();
		System.out.println("going to see whether employee is present under the department to be deleted");
		String querySelect=IQueryConstants.EMP_ROLE_SELECT;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
		preparedStmt.setString(1, config.getRole());
		preparedStmt.setString(2, config.getCompanyId());
		
		ResultSet rs=preparedStmt.executeQuery();
		while(rs.next()) {
			employeeId=rs.getString("EmployeeId");
			System.out.println("emp"+employeeId);
		
		}
		if(employeeId==null) {
		
		String querySelect1=IQueryConstants.EMP_DELETE_ROLE;
		PreparedStatement preparedStmt1 = connection.prepareStatement(querySelect1);
		preparedStmt1.setString(1,config.getRole());
		preparedStmt1.setString(2, config.getCompanyId());
		
		preparedStmt1.executeUpdate(); 
		System.out.println("deleting a role was done successfully ............");
		config.setAuthorization("DELETED");
		 
		connection.close();
	}else {
		System.out.println("role cannot be deleted since it has employee");
		config.setAuthorization("EMPLOYEE_EXIST");
		
	}
		}
	catch (SQLException e)
    {
    e.printStackTrace();
    }
     	
   finally {
	DBUtil.closeConnection(connection);
}
	
	return config;

}

/*
 * Storing OTP in database
 */

public static void storeOtp(String to,int otp)
{
	Connection connection=null;
	try {
		connection=DBUtil.getDBConnection();
		String querySelect=IQueryConstants.STORE_OTP;
		PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
		preparedStmt.setInt(1, otp);
		preparedStmt.setString(2, to);
		preparedStmt.setString(3, to);
		
		preparedStmt.executeUpdate();
	}
	catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBUtil.closeConnection(connection);
			}	
	}


/*
 * OTP verification 
 */
public static int otpVerify(EmployeeLoginJSON json){
    Connection connection=null;
	int flag=1;
	try {
		connection =DBUtil.getDBConnection();
		String querySelect=IQueryConstants.OTP_VERIFY;
		PreparedStatement preparedStmt = connection.prepareStatement(querySelect);
	    preparedStmt.setString(1, json.getEmailId());
	    ResultSet result=preparedStmt.executeQuery();
        while(result.next()) {
        	int Otp=result.getInt("OTP");
        	if((Otp==json.getOtp())) {
        		flag=0;  //set the flag value 0 if the entered otp is correct
        	    break;
        	}

        }
	}catch (Exception e) {
		e.printStackTrace();
	} finally {
		DBUtil.closeConnection(connection);
	}
        
     return flag;   
}





/*
 * Updating the new password in database
 */
	
public static int updatePassword(EmployeeLoginJSON  json)
{   
	int flag=1;
	Connection connection=null;
	try {
		connection=DBUtil.getDBConnection();
		String querySelect=IQueryConstants.UPADTE_NEW_PASSWORD;
		PreparedStatement preparedStmt=connection.prepareStatement(querySelect);
		preparedStmt.setString(1, json.getPassword());
		preparedStmt.setString(2, json.getEmailId());
		preparedStmt.executeUpdate();
		flag=0;
	}
	catch (Exception e) {
			e.printStackTrace();
	} finally {
			DBUtil.closeConnection(connection);
	}	
	return flag;
}


}


