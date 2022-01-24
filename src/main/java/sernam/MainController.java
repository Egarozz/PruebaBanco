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
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	
	public MainController() {
		belement = new HashMap<>();
		botones = new ArrayList<>();
	}
	public void findButtons(WebDriver driver) {
		boolean pass = false;
		int intentos = 0;
		while(!pass && intentos < 5) {
			try {
				List<WebElement> elementos = driver.findElements(By.xpath("//div[@class='boton-clave']"));
				botones.addAll(Arrays.asList(b1,b2,b3,b4,b5,b6,b7,b8,b9,b10));
				
				if(elementos.isEmpty()) {
					Thread.sleep(1000);
					
					throw new Exception("No se encontro los botones");
				}
				for(int i = 0; i < 10; i++) {
					botones.get(i).setText(elementos.get(i).getText());
					botones.get(i).setOnAction(e->boton(e));
					belement.put(botones.get(i).getText(), elementos.get(i));
				}
				pass = true;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				intentos++;
				
			}
		}
	}
	public void findCaptcha(WebDriver driver) {
		boolean pass = false;
		int intentos = 0;
		while(!pass && intentos < 5) {
			try {
				WebElement img = driver.findElement(By.xpath("//img[@id='captcha']"));
				captcha.setTextFormatter(new TextFormatter<>((change) -> {
				    change.setText(change.getText().toUpperCase());
				    return change;
				}));
				int width=img.getSize().getWidth();
			    int height=img.getSize().getHeight();
			    File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage bufferedImage=ImageIO.read(screen);
			    BufferedImage dest=bufferedImage.getSubimage(img.getLocation().getX(), img.getLocation().getY(), width, height);
				Image image = SwingFXUtils.toFXImage(dest, null);
				imagen.setImage(image);
				pass = true;
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
				intentos++;
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e1) {
					
				}
			}
		}
	}
	@FXML
	public void initialize() {
		//FirefoxOptions options = new FirefoxOptions();
		bconectar.setOnAction(e->boton(e));
		
		ChromeOptions options = new ChromeOptions();
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
		//driver = new FirefoxDriver(options);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get("https://zonasegura1.bn.com.pe/BNWeb/Inicio");
		findButtons(driver);
		findCaptcha(driver);
		
	}
	public void boton(ActionEvent e) {
		if(!e.getSource().equals(bconectar)) {
			contra.setText(contra.getText() + ((Button)e.getSource()).getText());
		}
		if(e.getSource().equals(bconectar)) {
			WebElement itarjeta = driver.findElement(By.xpath("//input[@id='txtNumeroTarjeta']"));
			WebElement icaptcha = driver.findElement(By.xpath("//input[@id='txtCaptcha']"));
			itarjeta.sendKeys(tarjeta.getCharacters());
			icaptcha.sendKeys(captcha.getCharacters());
			
			for(char s: contra.getText().toCharArray()) {
				belement.get(String.valueOf(s)).click();
				try {
					Thread.sleep(500);
				}catch(Exception ex) {}
			}
			WebElement ilogin = driver.findElement(By.xpath("//input[@id='btnLogin']"));
			try {
				ilogin.click();
				try {
					WebElement error = driver.findElement(By.xpath("//div[@class='cysErrorMsg']"));
					System.out.println(error.getText());
				}catch(Exception e2) {
					try {
						Thread.sleep(5000);
					}catch(Exception ex) {}
					
					actualizar(driver);
				}
				
				
				
			}catch(Exception e1) {
				
				
			}
		}
	}
	public void actualizar(WebDriver driver) {
		try {
			while(p==false) {
				
				driver.switchTo().frame(driver.findElement(By.id("CuerpoIframe")));
				Thread.sleep(1000);
				WebElement e = driver.findElement(By.id("cbOpciones-button"));
				e.click();
				Thread.sleep(1000);
				List<WebElement> el= driver.findElements(By.xpath("//li[@role='presentation']"));
				el.get(2).click();
				Thread.sleep(1000);
				WebElement ultima = driver.findElement(By.xpath("//td[@style='text-align: left; width:40%']"));
				System.out.println(ultima.getText());
				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.id("menu_frame")));
				List<WebElement> opciones = driver.findElements(By.xpath("//div[@class='accordionButton dax']"));
				WebElement regresar = opciones.get(0);
				regresar.click();
				driver.switchTo().defaultContent();
				Thread.sleep(1000);
			}
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
			driver.quit();
		}
	}
}
