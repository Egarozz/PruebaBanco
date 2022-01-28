package sernam.tasks;

import java.time.Duration;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sernam.DriverController;

public class LoadPageTask extends PublicTask{
	DriverController d;
	public LoadPageTask(DriverController d) {
		this.d = d;
	}
	@Override
	protected Void call() throws Exception {
		updateMessage("");
		d.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		updateMessage("Conectando a la pagina");
		d.driver.get("https://zonasegura1.bn.com.pe/BNWeb/Inicio");
		updateMessage("Procesando botones");
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				d.processButtons(d.driver, LoadPageTask.this);
			}
		});	
		Thread.sleep(2000);
		updateMessage("Procesando captcha");
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				d.processCaptcha(d.driver);
			}
		});	
		
		Thread.sleep(2000);
		updateMessage("Completado");	
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				d.first.connect.setVisible(false);
				d.first.root.getChildren().add(d.connectPane);
				d.connectPane.setLayoutY(100);
				d.c.contra.setText("");
				d.c.captcha.setText("");
				d.c.tarjeta.setText("");
			}
		});	
		
		return null;
	}
	@Override
	public void updateMessage(String s) {
		super.updateMessage(s);
	}
	@Override
	public void updateProgress(double a, double b) {
		super.updateProgress(a,b);
	}

}
