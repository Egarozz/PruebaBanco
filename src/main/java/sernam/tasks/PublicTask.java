package sernam.tasks;

import javafx.concurrent.Task;

public abstract class PublicTask extends Task<Void>{

	@Override
	public void updateMessage(String s) {
		super.updateMessage(s);
	}
	@Override
	public void updateProgress(double a, double b) {
		super.updateProgress(a,b);
	}
}
