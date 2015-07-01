package edu.uci.isr.archstudio4.comp.xarchcs.views.changesets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.myx.fw.MyxRegistryEvent;
import edu.uci.isr.myx.fw.MyxRegistryListener;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;

public abstract class MyxViewPart
	extends ViewPart
	implements MyxRegistryListener{

	private Class brickClass;

	private Composite parent = null;

	private IMyxBrick brick = null;

	private boolean parentCreated = false;

	private MyxRegistry myxr = MyxRegistry.getSharedInstance();

	public MyxViewPart(Class brickClass){
		this.brickClass = brickClass;
		myxr.addMyxRegistryListener(this);
	}

	@Override
	final public void createPartControl(Composite parent){
		new Label(parent, SWT.NONE).setText("Waiting for MYX brick to initialize...");
		this.parent = parent;
		checkInitialization();
	}

	abstract public void createMyxPartControl(Composite parent);

	@Override
	final public void setFocus(){
		if(parentCreated){
			setMyxFocus();
		}
	}

	abstract public void setMyxFocus();

	public void handleMyxRegistryEvent(MyxRegistryEvent evt){
		if(evt.getEventType() == MyxRegistryEvent.EventType.BrickRegistered){
			if(brickClass.getName().equals(evt.getBrick().getClass().getName())){
				this.brick = evt.getBrick();
				SWTWidgetUtils.async(parent, new Runnable(){

					public void run(){
						checkInitialization();
					}
				});
			}
		}
	}

	private void checkInitialization(){
		if(parent != null && !parent.isDisposed()){
			if(brick == null){
				brick = myxr.getBrick(brickClass);
			}
			if(brick != null && !parentCreated){
				parentCreated = true;
				for(Control control: parent.getChildren()){
					control.dispose();
				}
				createMyxPartControl(parent);
				parent.layout();
			}
		}
	}
}
