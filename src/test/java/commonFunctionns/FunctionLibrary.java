package commonFunctionns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;
	//method for launching Browser
	public static WebDriver startBrowser() throws Throwable
	{
		conpro=new Properties();
		conpro.load(new FileInputStream("./Files/Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("Chrome"))
		{
			driver=new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver=new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is not Matching",true);
		}
		return driver;
	}
	//method for launch url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
	//method for wait for any webelement
public static void waitforElement(String LocatorName,String LocatorValue,String TestData)
{
	WebDriverWait mywait=new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(TestData)));
	if(LocatorName.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	if(LocatorName.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
	if(LocatorName.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
}
//method for any textBox
public static void typeAction(String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
}
//method for click elements like buttons,links,checkboxes and radio buttons
public static void clickAction(String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
}
//method for validating title
public static void validateTitle(String Expected_Title)
{
	String Actual_Title=driver.getTitle();
	try {
	Assert.assertEquals(Actual_Title, Expected_Title, "Title is not Matching");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
//method for closing browser
public static void closeBrowser()
{
	driver.quit();
}

//method for generate date
public static String generateDate()
{
	Date date=new Date();
	DateFormat df=new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
	return df.format(date);
	
}
//method for selecting items in listboxes
public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
{
if(LocatorType.equalsIgnoreCase("name"))
{
	int value=Integer.parseInt(TestData);
	Select element=new Select(driver.findElement(By.name(LocatorValue)));
	element.selectByIndex(value);
}
if(LocatorType.equalsIgnoreCase("xpath"))
{
	int value=Integer.parseInt(TestData);
	Select element=new Select(driver.findElement(By.xpath(LocatorValue)));
	element.selectByIndex(value);
}
if(LocatorType.equalsIgnoreCase("id"))
{
	int value=Integer.parseInt(TestData);
	Select element=new Select(driver.findElement(By.id(LocatorValue)));
	element.selectByIndex(value);
}
}
//method for capture stock number into notepad
public static void captureStock(String LocatorType,String LocatorValue) throws Throwable
{
	String stocknum;
	if(LocatorType.equalsIgnoreCase("name"))
	{
		stocknum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		stocknum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		stocknum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	//create new notepad into capture data folder
	FileWriter fw=new FileWriter("./CaptureData/stockNumber.txt");
	BufferedWriter bw=new BufferedWriter(fw);
	bw.write("stocknum");
	bw.flush();
	bw.close();
}
//verify stock number from stock table
public static void stockTable() throws Throwable
{
	FileReader fr=new FileReader("./CaptureData/stockNumber.txt");
	BufferedReader br=new BufferedReader(fr);
	//read stock number from notepad
	String Exp_Data=br.readLine();
	
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	//enter stock number into textbox
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	//click search button
	driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
	Thread.sleep(4000);
	String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	try
	{
	Reporter.log(Exp_Data+"    "+Act_Data,true);
	}catch(Throwable t)
	{
		System.out.println(t.getMessage()); 
		
	}
	Assert.assertEquals(Act_Data, Exp_Data, "Stock number is not matching");
}
//method for capturing supplier number into notepad
public static void captureSup(String LocatorName,String LocatorValue) throws Throwable
{
	String SupplierNum="";
	if(LocatorName.equalsIgnoreCase("xpath"))
	{
		SupplierNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorName.equalsIgnoreCase("name"))
	{
		SupplierNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorName.equalsIgnoreCase("id"))
	{
		SupplierNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	//create notepad and write supplier number
	FileWriter fw=new FileWriter("./CaptureData/supplierNumber.txt");
	BufferedWriter bw=new BufferedWriter(fw);
	bw.write(SupplierNum);
	bw.flush();
	bw.close();
}
//method for verifying supplier number in a table
public static void supplierTable() throws Throwable
{
	//read supplier number from notepad
	FileReader fr=new FileReader("./CaptureData/supplierNumber.txt");
	BufferedReader br=new BufferedReader(fr);
	String Exp_Data=br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
			//click search panel ig not dispalyed
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
	Thread.sleep(3000);
	String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(Exp_Data+"  "+Act_Data,true);
	try {
		Assert.assertEquals(Act_Data, Exp_Data,"suppliernumber is not matching");
		
	} catch (AssertionError a) {
		System.out.println(a.getMessage());
	}
}
//method for capturing customer number into notepad
public static void captureCustomer(String LocatorName,String LocatorValue) throws Throwable
{
	String CustomerNum="";
	if(LocatorName.equalsIgnoreCase("xpath"))
	{
		CustomerNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorName.equalsIgnoreCase("name"))
	{
		CustomerNum=driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorName.equalsIgnoreCase("id"))
	{
		CustomerNum=driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	//create notepad and write supplier number
	FileWriter fw=new FileWriter("./CaptureData/customerNumber.txt");
	BufferedWriter bw=new BufferedWriter(fw);
	bw.write(CustomerNum);
	bw.flush();
	bw.close();
}
//method for verifying supplier number in a table
public static void customerTable() throws Throwable
{
	//read supplier number from notepad
	FileReader fr=new FileReader("./CaptureData/customerNumber.txt");
	BufferedReader br=new BufferedReader(fr);
	String Exp_Data=br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
			//click search panel ig not dispalyed
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
	Thread.sleep(3000);
	String Act_Data=driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
	Reporter.log(Exp_Data+"  "+Act_Data,true);
	try {
		Assert.assertEquals(Act_Data, Exp_Data,"customernumber is not matching");
		
	} catch (AssertionError a) {
		System.out.println(a.getMessage());
	}
}
}