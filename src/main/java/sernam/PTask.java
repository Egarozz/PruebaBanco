package sernam;

import javafx.concurrent.Task;

public class PTask<T> extends Task<T>{
	private int progress = 0;
	
	@Override
	protected T call() throws Exception {
		// TODO Auto-generated method stub
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
	public int getP() {return progress;}

}
