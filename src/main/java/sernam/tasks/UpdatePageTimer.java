package sernam.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import sernam.DriverController;
import sernam.Movimiento;

public class UpdatePageTimer extends TimerTask{
	DriverController c;
	SimpleDoubleProperty progress;
	public UpdatePageTimer(DriverController c) {
		this.c = c;
		progress = new SimpleDoubleProperty();
		progress.set(0);
		c.first.spinner.progressProperty().bind(progress);
	}
	@Override
	public void run() {
		int pasoActual = 0;
		int intentos = 0;
		try {	
			
			for(int i = 0; i<=100;i++) {
				progress.set(i);
				c.tWait(5);
			}
				
				if(pasoActual == 0) {
					c.driver.switchTo().frame(c.driver.findElement(By.id("CuerpoIframe")));
					Thread.sleep(250);
					pasoActual = 1;
					System.out.println("Paso 0");
				}
				if(pasoActual == 1) {
					WebElement e = c.driver.findElement(By.id("cbOpciones-button"));
					e.click();
					Thread.sleep(250);
					pasoActual = 2;
					System.out.println("Paso 1");
				}
				if(pasoActual == 2) {
					List<WebElement> el= c.driver.findElements(By.xpath("//li[@role='presentation']"));
					el.get(2).click();
					Thread.sleep(250);
					pasoActual = 3;
					System.out.println("Paso 2");
				}

				List<WebElement> saldo = c.driver.findElements(By.xpath("//tr//td[contains(text(),'Saldo disponible:')]/following-sibling::td"));
				List<WebElement> movimiento = c.driver.findElements(By.xpath("//table[@id='movimiento']/tbody/tr"));
				
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

					List<Movimiento> nuevos = c.getNuevoMov(movs, Double.parseDouble(saldo.get(0).getText().replaceAll(",", "").replaceAll(" ", "")));
					System.out.println(nuevos.size());
					if(!nuevos.isEmpty()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								for(Movimiento elem: nuevos) {
									c.cola.addFirst(elem);
								}
							}
						});	
					}
				}	

				if(pasoActual == 3) {	
					c.driver.switchTo().defaultContent();
					pasoActual = 4;
					System.out.println("Paso 3");
				}
				if(pasoActual == 4) {
					c.driver.switchTo().frame(c.driver.findElement(By.id("menu_frame")));
					pasoActual = 5;
					System.out.println("Paso 4");
				}

				if(pasoActual == 5) {
					List<WebElement> opciones = c.driver.findElements(By.xpath("//div[@class='accordionButton dax']"));
					WebElement regresar = opciones.get(0);
					regresar.click();
					pasoActual = 6;
					System.out.println("Paso 5");
				}

				if(pasoActual == 6) {
					c.driver.switchTo().defaultContent();
					pasoActual = 0;
					System.out.println("Paso 6");
				}
				
				
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Error -> " + intentos);
				intentos++;
				c.tWait(3000);
			}
	
	}

}
