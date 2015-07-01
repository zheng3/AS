package edu.uci.isr.bna4;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.uci.isr.widgets.swt.SWTWidgetUtils;

public class ZoomUtils{

	public static final double[] ZOOM_VALUES = new double[]{0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 1.25, 1.50, 1.75, 2.00, 4.00, 8.00, 16.00};

	public static double getNextLowestZoomValue(double currentZoomValue){
		double lowestValue = ZOOM_VALUES[0];
		if(currentZoomValue <= lowestValue){
			return currentZoomValue;
		}
		else{
			for(int i = 0; i < ZOOM_VALUES.length; i++){
				if(currentZoomValue == ZOOM_VALUES[i]){
					int index = i - 1;
					if(index < 0){
						index = 0;
					}
					return ZOOM_VALUES[index];
				}
				if(currentZoomValue < ZOOM_VALUES[i]){
					int index = i - 1;
					if(index < 0){
						index = 0;
					}
					return ZOOM_VALUES[index];
				}
			}
		}
		return ZOOM_VALUES[ZOOM_VALUES.length - 1];
	}

	public static double getNextHighestZoomValue(double currentZoomValue){
		double highestValue = ZOOM_VALUES[ZOOM_VALUES.length - 1];
		if(currentZoomValue >= highestValue){
			return currentZoomValue;
		}
		else{
			for(int i = ZOOM_VALUES.length - 1; i >= 0; i--){
				if(currentZoomValue == ZOOM_VALUES[i]){
					int index = i + 1;
					if(index >= ZOOM_VALUES.length){
						index = ZOOM_VALUES.length - 1;
					}
					return ZOOM_VALUES[index];
				}
				if(currentZoomValue > ZOOM_VALUES[i]){
					int index = i + 1;
					if(index >= ZOOM_VALUES.length){
						index = ZOOM_VALUES.length - 1;
					}
					return ZOOM_VALUES[index];
				}
			}
		}
		return ZOOM_VALUES[0];
	}

	public static String zoomLevelToString(double zoom){
		return BNAUtils.round(zoom * 100.0d) + "%";
	}

	public static double stringToZoomLevel(String s) throws NumberFormatException{
		s = s.trim();
		if(s.endsWith("%")){
			s = s.substring(0, s.length() - 1);
			s = s.trim();
		}
		int pct = Integer.parseInt(s);
		double scale = pct / 100.0d;
		return scale;
	}

	public static Control createZoomWidget(Composite parent, final Composite bna, final ICoordinateMapper cm){
		boolean cmIsMutable = cm instanceof IMutableCoordinateMapper;

		final Combo combo = new Combo(parent, cmIsMutable ? SWT.NONE : SWT.READ_ONLY);
		String[] zoomLevels = new String[ZOOM_VALUES.length];
		int z = 0;
		for(int i = ZOOM_VALUES.length - 1; i >= 0; i--){
			zoomLevels[z++] = zoomLevelToString(ZOOM_VALUES[i]);
		}
		combo.setItems(zoomLevels);
		combo.setText(zoomLevelToString(cm.getScale()));

		final ICoordinateMapperListener cml = new ICoordinateMapperListener(){

			public void coordinateMappingsChanged(final CoordinateMapperEvent evt){
				SWTWidgetUtils.async(combo, new Runnable(){

					public void run(){
						try{
							combo.setText(zoomLevelToString(evt.getNewScale()));
						}
						catch(Throwable t){
							t.printStackTrace();
						}
					}
				});
			}
		};
		cm.addCoordinateMapperListener(cml);

		if(cmIsMutable){
			combo.addSelectionListener(new SelectionListener(){

				public void widgetSelected(SelectionEvent e){
					try{
						double newScale = stringToZoomLevel(combo.getText());
						if(newScale == cm.getScale()){
							return;
						}
						scaleToCenter(bna, (IMutableCoordinateMapper)cm, newScale);
					}
					catch(NumberFormatException nfe){
						combo.setText(zoomLevelToString(cm.getScale()));
					}
				}

				public void widgetDefaultSelected(SelectionEvent e){
					widgetSelected(e);
				}
			});
		}

		combo.addDisposeListener(new DisposeListener(){

			public void widgetDisposed(DisposeEvent e){
				cm.removeCoordinateMapperListener(cml);
			}
		});

		return combo;
	}

	public static void scaleToCenter(Control w, IMutableCoordinateMapper mcm, double newScale){
		Point size = w.getSize();
		int lcx = size.x / 2;
		int lcy = size.y / 2;
		int worldCenterX = mcm.localXtoWorldX(lcx);
		int worldCenterY = mcm.localYtoWorldY(lcy);

		mcm.rescaleAbsolute(newScale);

		int newWorldCenterX = mcm.localXtoWorldX(lcx);
		int newWorldCenterY = mcm.localYtoWorldY(lcy);
		int dwcx = newWorldCenterX - worldCenterX;
		int dwcy = newWorldCenterY - worldCenterY;
		mcm.repositionRelative(-dwcx, -dwcy);
	}
}
