package sernam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;

public class DriverController {
	WebDriver driver;
	MainController c;
	List<Button> botones;
	HashMap<String, WebElement> belement;
	boolean loop = true;
	private PTask<Property<String>> loadPage;
	public DriverController(WebDriver driver, MainController controller) {
		this.driver = driver;
		this.c= controller;
		this.botones = new ArrayList<>();
		this.belement = new HashMap<>();
		
		c.captcha.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		
		c.bconectar.setOnAction(e->boton(e));
		
		loadPage = new PTask<Property<String>>() {
			@Override
			protected Property<String> call() throws Exception {
				
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				updateMessage("Conectando a la pagina");
				driver.get("https://zonasegura1.bn.com.pe/BNWeb/Inicio");
				updateMessage("Procesando botones");
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						processButtons(driver, loadPage);
					}
				});	
				Thread.sleep(1000);
				updateMessage("Procesando captcha");
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						processCaptcha(driver);
					}
				});	
				
				Thread.sleep(1000);
				updateMessage("Completado");	
				
				
				return null;
			}
			
			
		};
		c.log.textProperty().bind(loadPage.messageProperty());
	}
	public void initialize() {
		new Thread(loadPage).start();
	}
	public List<WebElement> findButtons() {
		boolean pass = false;
		int intentos = 0;
		List<WebElement> elementos = null;
		while(!pass && intentos < 5) {
			elementos = driver.findElements(By.xpath("//div[@class='boton-clave']"));
			pass = !elementos.isEmpty();
			if(!pass) System.out.println("No se encontro los botones -" + String.valueOf(intentos+1));
			intentos++;
		}	
		return elementos;
	}
	public WebElement findCaptcha() {
		boolean pass = false;
		int intentos = 0;
		
		List<WebElement> img = null;
		while(!pass && intentos < 5) {
			img = driver.findElements(By.xpath("//img[@id='captcha']"));
			pass = !img.isEmpty();
			if(!pass) System.out.println("No se encontro el captcha -" + String.valueOf(intentos+1));
			intentos++;
		}
		return img.get(0);
	}	
	
	
	private void processCaptcha(WebDriver driver) {
		WebElement img = findCaptcha();
		if(img == null) {return;}
		
		//Obtener las dimensiones del captcha para tomar la captura de pantalla
		int width=img.getSize().getWidth();
		int height=img.getSize().getHeight();
		try {
			File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			BufferedImage bufferedImage=ImageIO.read(screen);
			BufferedImage dest=bufferedImage.getSubimage(img.getLocation().getX(), img.getLocation().getY(), width, height);
			Image image = SwingFXUtils.toFXImage(dest, null);
			c.imagen.setImage(image);
		}
		catch(Exception e) {
			System.out.println("Problema al convertir la imagen");
		}		
	}			
					
				
			
		
	
	
	private void processButtons(WebDriver driver, PTask task) {
		List<WebElement> elementos = findButtons();
		task.updateMessage("Hola");
		tWait(1000);
		/* Colocar botones en un array para reducir el codigo de agregar listeners, ademas
		 * Se coloca en el hashmap para poder tener un facil acceso segun el numero para 
		 * automatizar los clicks a los botones
		 */
		botones.addAll(Arrays.asList(c.b1,c.b2,c.b3,c.b4,c.b5,c.b6,c.b7,c.b8,c.b9,c.b10));
		for(int i = 0; i < 10; i++) {
			botones.get(i).setText(elementos.get(i).getText());
			botones.get(i).setOnAction(e->boton(e));
			belement.put(botones.get(i).getText(), elementos.get(i));
		}
	}
	
	private void boton(ActionEvent e) {
		if(!e.getSource().equals(c.bconectar)) {
			c.contra.setText(c.contra.getText() + ((Button)e.getSource()).getText());
		}
		if(e.getSource().equals(c.bconectar)) {
			initialize();
		}	
			
			
	}
	
	public boolean conectar() {
		/*  -Verificar que el textbox para la tarjeta, para el captcha y el boton de login esten presentes
		 *  -Presionar los botones de la contrasena en un intervalo de 500 ms
		 *  -Verificar que no haya un error
		 *  -Si hay error retornar falso de lo contrario retornar verdadero
		 */
		List<WebElement> itarjeta = driver.findElements(By.xpath("//input[@id='txtNumeroTarjeta']"));
		List<WebElement> icaptcha = driver.findElements(By.xpath("//input[@id='txtCaptcha']"));
		List<WebElement> ilogin = driver.findElements(By.xpath("//input[@id='btnLogin']"));
		
		if(itarjeta.isEmpty() || icaptcha.isEmpty() || ilogin.isEmpty()) return false;
		
		itarjeta.get(0).sendKeys(c.tarjeta.getCharacters());
		icaptcha.get(0).sendKeys(c.captcha.getCharacters());
		for(char s: c.contra.getText().toCharArray()) {
			belement.get(String.valueOf(s)).click();
			c.log.setText("Colocando..." + String.valueOf(s));
			tWait(500);
		}
		
		ilogin.get(0).click();
		WebElement error = findError();
		if(error != null) {
			System.out.println(error.getText());
			return false;
		}
		
		tWait(5000);
		return true;
		
	}
	public WebElement findError() {
		List<WebElement> error = driver.findElements(By.xpath("//div[@class='cysErrorMsg']"));
		if(error.isEmpty()) return null;
		return error.get(0);
	}
				
				
			
				
				
			
		
	
	
	private void actualizar(WebDriver driver) {
		try {
			while(loop) {
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
	private void tWait(long milis) {
		try {
			Thread.sleep(milis);
		}catch(InterruptedException e) {}
	}
	
	

}
