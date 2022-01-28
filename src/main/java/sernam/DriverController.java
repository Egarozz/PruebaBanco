package sernam;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.controlsfx.control.Notifications;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import sernam.tasks.ConnectPageTask;
import sernam.tasks.LoadPageTask;
import sernam.tasks.PublicTask;
import sernam.tasks.UpdateMessageTimer;

public class DriverController {
	public WebDriver driver;
	public MainController c;
	public FirstController first;
	List<Button> botones;
	HashMap<String, WebElement> belement;
	boolean loop = true;
	public PTask<Void> update;
	public AnchorPane connectPane;
	
	private double saldo = -1;
	private Movimiento ultimoMov = null;
	public LinkedList<Movimiento> cola;

	private Timer updateMessage;
	
	public DriverController(WebDriver driver, MainController controller, FirstController first, AnchorPane root) {
		this.driver = driver;
		this.c= controller;
		this.first = first;
		this.connectPane = root;
		this.botones = new ArrayList<>();
		this.belement = new HashMap<>();
		this.cola = new LinkedList<>();
		c.captcha.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		updateMessage = new Timer();
		
		
		c.bconectar.setOnAction(e->boton(e));
		first.boton.setOnAction(e->boton(e));
		
		

			
		first.spinner.setVisible(false);
		
		

	}
	

	public void createUpdateTask() {
		update = new PTask<Void>() {
			@Override
			protected Void call() throws Exception {
				actualizar(driver,this);
				return null;
			}
		};
		first.spinner.progressProperty().bind(update.progressProperty());
	}
	
	
		
	
	public void initialize() {
		LoadPageTask loadPage = new LoadPageTask(this);
		first.log.textProperty().bind(loadPage.messageProperty());
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
	
	
	public void processCaptcha(WebDriver driver) {
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
					
				
			
		
	
	
	public void processButtons(WebDriver driver, PublicTask task) {
		List<WebElement> elementos = findButtons();
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
		
		if(e.getSource().equals(first.boton)) {
			
			initialize();
		}
		if(e.getSource().equals(c.bconectar)) {
			
			first.connect.setVisible(true);
			first.root.getChildren().remove(connectPane);
			first.spinner.setVisible(true);
			
			ConnectPageTask conn = new ConnectPageTask(this);
			first.log.textProperty().bind(conn .messageProperty());
			first.spinner.progressProperty().bind(conn.progressProperty());
			new Thread(conn).start();
			updateMessage.schedule(new UpdateMessageTimer(this), 0,2000);
			
		}
			
			
			
	}
	
	public boolean conectar(PublicTask task) throws Exception{
		/*  -Verificar que el textbox para la tarjeta, para el captcha y el boton de login esten presentes
		 *  -Presionar los botones de la contrasena en un intervalo de 500 ms
		 *  -Verificar que no haya un error
		 *  -Si hay error retornar falso de lo contrario retornar verdadero
		 */
		task.updateMessage("Buscar objetos");
		List<WebElement> itarjeta = driver.findElements(By.xpath("//input[@id='txtNumeroTarjeta']"));
		List<WebElement> icaptcha = driver.findElements(By.xpath("//input[@id='txtCaptcha']"));
		List<WebElement> ilogin = driver.findElements(By.xpath("//input[@id='btnLogin']"));
		if(itarjeta.isEmpty() || icaptcha.isEmpty() || ilogin.isEmpty()) return false;
		
		for(int i = 0; i <= 10; i++) {
			task.updateProgress(i, 100);
			Thread.sleep(10);
		}
		
		
		
		task.updateMessage("Enviar registro");
		itarjeta.get(0).sendKeys(c.tarjeta.getCharacters());
		icaptcha.get(0).sendKeys(c.captcha.getCharacters());
		for(char s: c.contra.getText().toCharArray()) {
			belement.get(String.valueOf(s)).click();
			tWait(500);
		}
		for(int i = 10; i <= 40; i++) {
			task.updateProgress(i, 100);
			Thread.sleep(10);
		}
		
		
		task.updateMessage("Logeando");
		ilogin.get(0).click();
		WebElement error = findError();
		if(error != null) {
			task.updateMessage(error.getText());
			task.updateProgress(0, 100);
			return false;
		}
		for(int i = 40; i <= 80; i++) {
			task.updateProgress(i, 100);
			Thread.sleep(10);
		}
		task.updateMessage("Cargando pagina");
		tWait(5000);
		for(int i = 80; i <= 100; i++) {
			task.updateProgress(i, 100);
			Thread.sleep(10);
		}
		task.updateMessage("Conectado");
		return true;
		
	}
	public WebElement findError() {
		List<WebElement> error = driver.findElements(By.xpath("//div[@class='cysErrorMsg']"));
		if(error.isEmpty()) return null;
		return error.get(0);
	}
				
				
			
				
				
			
		
	
	
	private void actualizar(WebDriver driver, PTask<Void> task) {
		task.updateProgress(0,100);
		int pasoActual = 0;
		int intentos = 0;
		while(loop && intentos < 3) {
			try {
				for(int i = 0; i<=100;i++) {
					task.updateProgress(i,100);
					Thread.sleep(5);
				}
				if(pasoActual == 0) {
					driver.switchTo().frame(driver.findElement(By.id("CuerpoIframe")));
					Thread.sleep(250);
					pasoActual = 1;
					System.out.println("Paso 0");
				}
				if(pasoActual == 1) {
					WebElement e = driver.findElement(By.id("cbOpciones-button"));
					e.click();
					Thread.sleep(250);
					pasoActual = 2;
					System.out.println("Paso 1");
				}
				if(pasoActual == 2) {
					List<WebElement> el= driver.findElements(By.xpath("//li[@role='presentation']"));
					el.get(2).click();
					Thread.sleep(250);
					pasoActual = 3;
					System.out.println("Paso 2");
				}

				List<WebElement> saldo = driver.findElements(By.xpath("//tr//td[contains(text(),'Saldo disponible:')]/following-sibling::td"));
				List<WebElement> movimiento = driver.findElements(By.xpath("//table[@id='movimiento']/tbody/tr"));
				
				if(!movimiento.isEmpty() && !saldo.isEmpty()) {
					List<Movimiento> movs = new ArrayList<>();
					for(WebElement elem: movimiento) {

						String cod = elem.findElement(By.xpath(".//td[@style='text-align: center; width:10%']")).getText();
						String desc = elem.findElement(By.xpath(".//td[@style='text-align: left; width:40%']")).getText();
						String cargo = elem.findElement(By.xpath(".//td[@style='text-align: right; width:15%;color: #c51416;']")).getText();
						String abono = elem.findElement(By.xpath(".//td[@style='text-align: right; width:15%']")).getText();

						Movimiento mov = new Movimiento(cod,desc,cargo,abono);
						movs.add(mov);
					}

					List<Movimiento> nuevos = getNuevoMov(movs, Double.parseDouble(saldo.get(0).getText().replaceAll(",", "").replaceAll(" ", "")));
					System.out.println(nuevos.size());
					if(!nuevos.isEmpty()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								for(Movimiento elem: nuevos) {
									cola.addFirst(elem);
								}
							}
						});	
					}
				}	

				if(pasoActual == 3) {	
					driver.switchTo().defaultContent();
					pasoActual = 4;
					System.out.println("Paso 3");
				}
				if(pasoActual == 4) {
					driver.switchTo().frame(driver.findElement(By.id("menu_frame")));
					pasoActual = 5;
					System.out.println("Paso 4");
				}

				if(pasoActual == 5) {
					List<WebElement> opciones = driver.findElements(By.xpath("//div[@class='accordionButton dax']"));
					WebElement regresar = opciones.get(0);
					regresar.click();
					pasoActual = 6;
					System.out.println("Paso 5");
				}

				if(pasoActual == 6) {
					driver.switchTo().defaultContent();
					pasoActual = 0;
					System.out.println("Paso 6");
				}
				
				
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Error -> " + intentos);
				intentos++;
				tWait(3000);
			}
		}
		
		
		task.updateProgress(0, 100);
		task.updateMessage("Desconectado");
		
	}
	public void tWait(long milis) {
		try {
			Thread.sleep(milis);
		}catch(InterruptedException e) {}
	}
	
	public List<Movimiento> getNuevoMov(List<Movimiento> movimientos, double saldo){
		int posAntiguo = -1;
		List<Movimiento> nuevos = new ArrayList<>();
		
		if(ultimoMov == null) {
			nuevos.add(movimientos.get(0));
			ultimoMov = movimientos.get(0);
			this.saldo = saldo;
			return nuevos;
		}
		
		if(this.saldo != saldo) {
			double nuevoSaldo = this.saldo;
			for(int i = 0; i < movimientos.size(); i++) {
				Movimiento current = movimientos.get(i);
				nuevoSaldo += current.cargo;
				nuevoSaldo -= current.abono;
				if(nuevoSaldo == saldo) {
					posAntiguo = i;
					break;
				}
			}
		}	
		
		for(int i = 0; i < posAntiguo; i++) {
			nuevos.add(movimientos.get(i));
		}
		
//		for(int i = 0; i < movimientos.size(); i++) {
//			Movimiento current = movimientos.get(i);
//			if(!current.codigo.equals(ultimoMov.codigo)) break;
//			if(current.abono != ultimoMov.abono) break;
//			if(current.cargo != ultimoMov.cargo) break;
//			System.out.println("Cambio");
//			posAntiguo = i;
//		}
//		for(int i = 0; i < posAntiguo; i++) {
//			nuevos.add(movimientos.get(i));
//		}
		
		
		
		
		
		if(!nuevos.isEmpty()) ultimoMov = nuevos.get(0);
		this.saldo = saldo;
		return nuevos;
			
		
	}
	

	

}
