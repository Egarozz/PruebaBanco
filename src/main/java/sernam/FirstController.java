package sernam;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FirstController {

	@FXML
	JFXSpinner spinner;
	@FXML
	JFXButton boton;
	@FXML
	Label log;
	
	int progres = 0;
	Task<Void> progress;
	WebDriver driver;
	DriverController dcontroller ;
	
	public FirstController() {
		FirefoxOptions options = new FirefoxOptions();
		List<String> arguments = new ArrayList<>();
		String user_agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36";
		
		arguments.add("--headless");
		arguments.add("--window-size=1920,1080");
		arguments.add("--allow-running-insecure-content");
		arguments.add("--ignore-certificate-errors");
		arguments.add("--disable-extensions");
		arguments.add("--proxy-server='direct://'");
		arguments.add("--proxy-bypass-list=*");
		arguments.add("--start-maximized");
		arguments.add("--disable-dev-shm-usage");
		arguments.add("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36");
		
		
		options.addArguments(arguments);
		options.setCapability("general.useragent.override",user_agent);
		driver = new FirefoxDriver(options);
		//driver = new ChromeDriver(options);
		dcontroller = new DriverController(driver);
		//dcontroller.initialize();
	}
	@FXML 
	public void initialize() {
		spinner.setProgress(0);
		
		progress = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while(progres <= 100) {
					updateProgress(progres,100);
					progres++;
					Thread.sleep(100);
				}
				return null;
			}	
		};			
				
				
		boton.setOnAction(e->conectar(e));
		spinner.progressProperty().bind(progress.progressProperty());
		log.textProperty().bind(progress.progressProperty().asString());
		new Thread(progress).start();
		
		
	}
	
	public void conectar(ActionEvent e) {
		
	}
}
