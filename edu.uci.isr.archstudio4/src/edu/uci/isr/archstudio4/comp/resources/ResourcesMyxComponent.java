package edu.uci.isr.archstudio4.comp.resources;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;
import edu.uci.isr.widgets.swt.OverlayImageIcon;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;

public class ResourcesMyxComponent
	extends AbstractMyxSimpleBrick
	implements IResources{

	private final Object lock = new Object();

	class ResourcesProxy
		implements InvocationHandler{

		IResources resources = ResourcesMyxComponent.this;

		List<Runnable> toExec = new ArrayList<Runnable>();

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
			synchronized(lock){
				final Method fMethod = method;
				final Object[] fArgs = args;
				if(void.class.equals(method.getReturnType())){
					if(display == null){
						toExec.add(new Runnable(){

							public void run(){
								try{
									fMethod.invoke(resources, fArgs);
								}
								catch(Throwable t){
									t.printStackTrace();
								}
							}
						});
						return null;
					}
					else if(Display.getCurrent() == null){
						SWTWidgetUtils.async(display, new Runnable(){

							public void run(){
								try{
									fMethod.invoke(resources, fArgs);
								}
								catch(Throwable t){
									t.printStackTrace();
								}
							}
						});
						return null;
					}
				}
				if(Display.getCurrent() == null){
					SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
				}
				if(display == null){
					setDisplay(Display.getCurrent());
					for(Runnable runnable: toExec){
						runnable.run();
					}
					toExec.clear();
				}
				return method.invoke(resources, args);
			}
		}
	}

	public static final IMyxName INTERFACE_NAME_IN_RESOURCES = MyxUtils.createName("resources");

	protected Display display = null;
	protected FontRegistry fontRegistry = null;
	protected ImageRegistry imageRegistry = null;
	protected ColorRegistry colorRegistry = null;

	public void setDisplay(Display display){
		checkDevice();
		synchronized(lock){
			if(this.display != null){
				this.display = null;

				imageRegistry.dispose();

				fontRegistry = null;
				imageRegistry = null;
				colorRegistry = null;
			}
			if(display != null){
				this.display = display;

				fontRegistry = new FontRegistry(display);
				imageRegistry = new ImageRegistry(display);
				colorRegistry = new ColorRegistry(display);

				createColor(COLOR_ARCHSTUDIO, RGB_ARCHSTUDIO_MAIN);
				createColor(COLOR_BANNER_BRIGHT, RGB_BANNER_BRIGHT);
				createColor(COLOR_BANNER_DARK, RGB_BANNER_DARK);

				display.disposeExec(new Runnable(){

					public void run(){
						setDisplay(null);
					}
				});
			}
			lock.notifyAll();
		}
	}

	public ResourcesMyxComponent(){
	}

	IResources proxy = null;

	@Override
	public void init(){
		proxy = (IResources)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{IResources.class}, new ResourcesProxy());
	}

	@Override
	public void destroy(){
		proxy = null;
	}

	public Object getServiceObject(IMyxName interfaceName){
		if(interfaceName.equals(INTERFACE_NAME_IN_RESOURCES)){
			return proxy;
		}
		return null;
	}

	protected void checkDevice(){
		if(Display.getCurrent() == null){
			SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}

	public Image getPlatformImage(String symbolicName){
		checkDevice();
		return PlatformUI.getWorkbench().getSharedImages().getImage(symbolicName);
	}

	public ImageDescriptor getPlatformImageDescriptor(String symbolicName){
		checkDevice();
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(symbolicName);
	}

	public Font getPlatformFont(String symbolicName){
		checkDevice();
		return JFaceResources.getFont(symbolicName);
	}

	public Font getBoldPlatformFont(String symbolicName){
		checkDevice();
		FontRegistry fr = JFaceResources.getFontRegistry();
		return fr.getBold(symbolicName);
	}

	public Font getItalicPlatformFont(String symbolicName){
		checkDevice();
		FontRegistry fr = JFaceResources.getFontRegistry();
		return fr.getItalic(symbolicName);
	}

	public Font getFont(String symbolicName){
		checkDevice();
		return fontRegistry.get(symbolicName);
	}

	public void createFont(String symbolicName, FontData[] fontData){
		checkDevice();
		fontRegistry.put(symbolicName, fontData);
	}

	public void createDerivedFont(String newSymbolicName, FontData[] existingFontData, int newHeight, int newStyle){
		checkDevice();
		FontData[] fds = existingFontData;
		FontData[] nfds = new FontData[fds.length];
		for(int i = 0; i < fds.length; i++){
			int h = newHeight;
			if(newHeight == 0){
				h = fds[i].getHeight();
			}
			int s = newStyle;
			if(newStyle == 0){
				s = fds[i].getStyle();
			}
			nfds[i] = new FontData(fds[i].getName(), h, s);
		}
		createFont(newSymbolicName, nfds);
	}

	//	this approach does not work with plugins

	//	public void createImage(String symbolicName, String url){
	//		createImage(symbolicName, url, null);
	//	}

	//	public void createImage(String symbolicName, String url, Class resourceClass){
	//		if(imageRegistry.get(symbolicName) != null) return;
	//		try{
	//			InputStream is = SystemUtils.openURL(url, resourceClass);
	//			ImageData id = new ImageData(is);
	//			ImageDescriptor desc = ImageDescriptor.createFromImageData(id);
	//			Image img = desc.createImage();
	//			imageRegistry.put(symbolicName, img);
	//			is.close();
	//		}
	//		catch(Exception e){
	//			e.printStackTrace();
	//		}
	//	}

	public void createImage(String symbolicName, InputStream is){
		checkDevice();
		if(imageRegistry.get(symbolicName) != null){
			return;
		}
		try{
			ImageData id = new ImageData(is);
			ImageDescriptor desc = ImageDescriptor.createFromImageData(id);
			Image img = desc.createImage();
			createImage(symbolicName, img);
			is.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void createImage(String symbolicName, Image img){
		checkDevice();
		if(imageRegistry.get(symbolicName) != null){
			return;
		}
		try{
			imageRegistry.put(symbolicName, img);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void createOverlayImage(String symbolicName, Image base, Image[] overlays, int[] overlayPositions){
		checkDevice();
		if(imageRegistry.get(symbolicName) != null){
			return;
		}
		OverlayImageIcon oii = new OverlayImageIcon(base, overlays, overlayPositions);
		imageRegistry.put(symbolicName, oii);
	}

	public Image getImage(String symbolicName){
		checkDevice();
		return imageRegistry.get(symbolicName);
	}

	public ImageDescriptor getImageDescriptor(String symbolicName){
		checkDevice();
		return imageRegistry.getDescriptor(symbolicName);
	}

	public void createColor(String symbolicName, RGB colorData){
		checkDevice();
		if(colorRegistry.get(symbolicName) != null){
			return;
		}
		colorRegistry.put(symbolicName, colorData);
	}

	public Color getColor(String symbolicName){
		checkDevice();
		return colorRegistry.get(symbolicName);
	}
}
