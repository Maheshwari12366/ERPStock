package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctionns.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	String inputpath="./FileInput/QEDGEHYBIRD.xlsx";
	String outputpath=".//FileOutput/Hybridresults.xlsx";
	ExtentReports reports;
	ExtentTest logger;
	public static WebDriver driver;
	String TCSheet="MasterTestCases";
	public void startTest() throws Throwable
	{
		String module_status="";
		//create instance object for ExcelFile util class
		ExcelFileUtil xl=new ExcelFileUtil(inputpath);
		//iterate all rows in TCSheet
		for(int i=1;i<=xl.rowCount(TCSheet);i++)
		{
			if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
				//read corresponding sheet into TCModule variable
				String TCModule=xl.getCellData(TCSheet, i, 1);
				reports=new ExtentReports("./target/Reports/"+TCModule+"------"+FunctionLibrary.generateDate()+".html");
				logger=reports.startTest(TCModule);
				//iterate all rows in TCModule
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					String Description=xl.getCellData(TCModule, j, 0);
					String object_type=xl.getCellData(TCModule, j,1 );
					String Ltype=xl.getCellData(TCModule, j, 2);
					String LValue=xl.getCellData(TCModule, j, 3);
					String TData=xl.getCellData(TCModule, j, 4);
					try {
						if(object_type.equalsIgnoreCase("startBrowser"))
						{
						driver=FunctionLibrary.startBrowser();
						logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("waitforElement"))
						{
							FunctionLibrary.waitforElement(Ltype,LValue,TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(Ltype, LValue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(Ltype, LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("dropDownAction"))
								{
									FunctionLibrary.dropDownAction(Ltype, LValue, TData);
									logger.log(LogStatus.INFO, Description);
								}
						if(object_type.equalsIgnoreCase("captureStock"))
						{
							FunctionLibrary.captureStock(Ltype, LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("captureSup"))
						{
							FunctionLibrary.captureSup(Ltype, LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("captureCustomer"))
						{
							FunctionLibrary.captureCustomer(Ltype, LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(object_type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//write as pass into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						module_status="True";
						
					}catch(Exception e)
					{
						File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/screenshot"+Description+"--------"+FunctionLibrary.generateDate()+".png"));
						System.out.println(e.getMessage());
						//write as fail into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						module_status="false";
					}
					if(!(module_status).equalsIgnoreCase("false"))
					{
						//write as pass into TCSheet in status cell
						xl.setCellData(TCSheet, i, 3, "pass", outputpath);
					}
					else
					{
						//write as fail into TCSheet in status cell
						xl.setCellData(TCSheet, i, 3, "fail", outputpath);	
					}
					reports.endTest(logger);
					reports.flush();
				}
				
			}
			else
			{
				//write blocked into status cell in TCSheet
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}

}
