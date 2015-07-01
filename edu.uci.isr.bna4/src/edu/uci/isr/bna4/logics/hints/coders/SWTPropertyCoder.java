package edu.uci.isr.bna4.logics.hints.coders;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.logics.hints.IPropertyCoder;
import edu.uci.isr.bna4.logics.hints.PropertyDecodeException;

public class SWTPropertyCoder
	implements IPropertyCoder{

	public boolean encode(IPropertyCoder masterCoder, IEncodedValue encodedValue, Object value){
		if(value instanceof Point){
			Point v = (Point)value;
			encodedValue.setType(Point.class.getName());
			encodedValue.setData(v.x + "," + v.y);
			return true;
		}
		else if(value instanceof Rectangle){
			Rectangle v = (Rectangle)value;
			encodedValue.setType(Rectangle.class.getName());
			encodedValue.setData(v.x + "," + v.y + "," + v.width + "," + v.height);
			return true;
		}
		else if(value instanceof RGB){
			RGB v = (RGB)value;
			encodedValue.setType(RGB.class.getName());
			encodedValue.setData(v.red + "," + v.green + "," + v.blue);
			return true;
		}
		return false;
	}

	public Object decode(IPropertyCoder masterCoder, IEncodedValue encodedValue) throws PropertyDecodeException{
		try{
			String type = encodedValue.getType();
			String data = encodedValue.getData();
			if(Rectangle.class.getName().equals(type)){
				String[] d = data.split(",");
				Rectangle v = new Rectangle(0, 0, 0, 0);
				v.x = Integer.parseInt(d[0]);
				v.y = Integer.parseInt(d[1]);
				v.width = Integer.parseInt(d[2]);
				v.height = Integer.parseInt(d[3]);
				return v;
			}
			else if(Point.class.getName().equals(type)){
				String[] d = data.split(",");
				Point v = new Point(0, 0);
				v.x = Integer.parseInt(d[0]);
				v.y = Integer.parseInt(d[1]);
				return v;
			}
			else if(RGB.class.getName().equals(type)){
				String[] d = data.split(",");
				RGB v = new RGB(0, 0, 0);
				v.red = Integer.parseInt(d[0]);
				v.green = Integer.parseInt(d[1]);
				v.blue = Integer.parseInt(d[2]);
				return v;
			}
		}
		catch(Throwable t){
			throw new PropertyDecodeException(t);
		}
		return null;
	}
}
