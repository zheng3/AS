package edu.uci.isr.bna4.logics.hints;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.bna4.logics.hints.coders.ArrayPropertyCoder;
import edu.uci.isr.bna4.logics.hints.coders.EnumPropertyCoder;
import edu.uci.isr.bna4.logics.hints.coders.NativePropertyCoder;
import edu.uci.isr.bna4.logics.hints.coders.SWTPropertyCoder;

public class MasterPropertyCoder
	implements IPropertyCoder{

	static MasterPropertyCoder singleton = new MasterPropertyCoder();

	List<IPropertyCoder> propertyCoders = new ArrayList<IPropertyCoder>();

	public MasterPropertyCoder(){
		propertyCoders.add(new NativePropertyCoder());
		propertyCoders.add(new EnumPropertyCoder());
		propertyCoders.add(new ArrayPropertyCoder());
		propertyCoders.add(new SWTPropertyCoder());
	}

	public boolean encode(IPropertyCoder masterCoder, IEncodedValue encodedValue, Object value){
		if(encodedValue != null){
			if(value == null){
				encodedValue.setType("null");
				encodedValue.setData("");
				return true;
			}
			if(masterCoder == null)
				masterCoder = this;
			for(IPropertyCoder pc: propertyCoders){
				if(pc.encode(masterCoder, encodedValue, value)){
					return true;
				}
			}
		}
		return false;
	}

	public Object decode(IPropertyCoder masterCoder, IEncodedValue encodedValue) throws PropertyDecodeException{
		if(encodedValue == null || "null".equals(encodedValue.getType()) || null == encodedValue.getType() || null == encodedValue.getData())
			return null;
		if(masterCoder == null)
			masterCoder = this;
		for(IPropertyCoder pc: propertyCoders){
			Object value = pc.decode(masterCoder, encodedValue);
			if(value != null){
				return value;
			}
		}
		return null;
	}

	public static MasterPropertyCoder getSingleton(){
		return singleton;
	}
}
