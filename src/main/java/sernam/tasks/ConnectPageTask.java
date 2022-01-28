package sernam.tasks;

import java.util.Timer;

import javafx.concurrent.Task;
import sernam.DriverController;

public class ConnectPageTask extends PublicTask{
	DriverController d;
	Timer timer;
	public ConnectPageTask(DriverController d) {
		this.d = d;
		timer = new Timer();
	}
	
	@Override
	protected Void call() throws Exception {
		updateMessage("");
		if(!d.conectar(this)) {
			this.cancel();
		}
		return null;
	}
	@Override
	protected void succeeded() {
//		timer.schedule(new UpdatePageTimer(d), 0, 1000);
		d.createUpdateTask();
		new Thread(d.update).start();
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
