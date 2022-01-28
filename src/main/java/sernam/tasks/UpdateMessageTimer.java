package sernam.tasks;


import java.awt.Toolkit;
import java.util.TimerTask;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.geometry.Pos;
import sernam.DriverController;
import sernam.Movimiento;

public class UpdateMessageTimer extends TimerTask{
	DriverController c;
	public UpdateMessageTimer(DriverController c) {
		this.c = c;
	}
	@Override
	public void run() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Movimiento mov = c.cola.pollFirst();
				
				if(mov != null) {
					Notifications notificationBuilder = Notifications.create()
							.title(mov.descripcion)
							.text(mov.getDinero())
							.graphic(null)
							.hideAfter(javafx.util.Duration.INDEFINITE)
							.position(Pos.TOP_RIGHT);
					Toolkit.getDefaultToolkit().beep();
					notificationBuilder.show();
				}
				
			}
			
		});
		
	}

}
