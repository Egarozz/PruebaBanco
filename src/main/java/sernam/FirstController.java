package sernam;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

public class FirstController {

	@FXML
	JFXSpinner spinner;
	@FXML
	JFXButton boton;
	
	int progres = 0;
	Task<Void> progress ;
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
				
				
				
		spinner.progressProperty().bind(progress.progressProperty());
		new Thread(progress).start();
		
		
	}
}
