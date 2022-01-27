package sernam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {
	
	@FXML
	Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, blimpiar, bconectar;
	
	@FXML
	ImageView imagen;
	
	@FXML
	TextField contra, tarjeta, captcha;
	
	WebDriver driver;
	List<Button> botones;
	HashMap<String, WebElement> belement;
	boolean p = false;
	DriverController dcontroller ;
	public MainController() {
		belement = new HashMap<>();
		botones = new ArrayList<>();
		
	}
	
	public void boton() {
		System.out.println("lol");
	}
	@FXML
	public void initialize() {
		b1.setOnAction(e->boton());
//		FirefoxOptions options = new FirefoxOptions();
//		//ChromeOptions options = new ChromeOptions();
//		List<String> arguments = new ArrayList<>();
//		String user_agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36";
//		
//		arguments.add("--headless");
//		arguments.add("--window-size=1920,1080");
//		arguments.add("--allow-running-insecure-content");
//		arguments.add("--ignore-certificate-errors");
//		arguments.add("--disable-extensions");
//		arguments.add("--proxy-server='direct://'");
//		arguments.add("--proxy-bypass-list=*");
//		arguments.add("--start-maximized");
//		arguments.add("--disable-dev-shm-usage");
//		arguments.add("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36");
//		
//		
//		options.addArguments(arguments);
//		options.setCapability("general.useragent.override",user_agent);
//		driver = new FirefoxDriver(options);
//		//driver = new ChromeDriver(options);
//		dcontroller = new DriverController(driver);
		//dcontroller.initialize();
		
	}
	
	
}
