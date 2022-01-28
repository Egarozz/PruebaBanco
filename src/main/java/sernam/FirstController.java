package sernam;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class FirstController {

	@FXML
	public
	JFXSpinner spinner;
	@FXML
	JFXButton boton;
	@FXML
	Label log;
	@FXML
	public
	AnchorPane root;
	@FXML
	public AnchorPane connect;
	
	int progres = 0;
	WebDriver driver;
	DriverController dcontroller ;
	
	public FirstController() {

	}
	@FXML 
	public void initialize() {
		spinner.setProgress(0);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane)fxmlLoader.load(getClass().getResource("Main.fxml").openStream());
			MainController controller = (MainController) fxmlLoader.getController();
			//this.root.getChildren().add(root);
			initializeDriver(controller, this, root);
			
			//connect.setVisible(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		
	}
	public void initializeDriver(MainController controller, FirstController first, AnchorPane root) {
		ChromeOptions options = new ChromeOptions();
		//FirefoxOptions options = new FirefoxOptions();
		List<String> arguments = new ArrayList<>();
		String user_agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36";
		
		arguments.add("--headless");
		arguments.add("--window-size=1920,1080");
		arguments.add("--allow-running-insecure-content");
		//arguments.add("--ignore-certificate-errors");
		arguments.add("--disable-extensions");
		//arguments.add("--proxy-server='direct://'");
		//arguments.add("--proxy-bypass-list=*");
		arguments.add("--start-maximized");
		//arguments.add("--disable-dev-shm-usage");
		arguments.add("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36");
		
		
		options.addArguments(arguments);
		options.setCapability("general.useragent.override",user_agent);
		//driver = new FirefoxDriver(options);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		dcontroller = new DriverController(driver, controller, first, root);
		
	}
	
}
