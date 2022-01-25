package sernam;

import javafx.concurrent.Task;

public class PTask<T> extends Task<T>{

	@Override
	protected T call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateMessage(String s) {
		super.updateMessage(s);
	}

}
