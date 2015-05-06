package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.view.View;

public abstract class PresenterImpl<T extends View> implements Presenter{
	protected T	display	= null;
	
	public PresenterImpl(T display){
		this.display = display;
		bind();
	}
	
	protected abstract void bind();
}