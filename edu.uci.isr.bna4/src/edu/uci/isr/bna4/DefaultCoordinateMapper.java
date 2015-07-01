package edu.uci.isr.bna4;

public class DefaultCoordinateMapper implements IMutableCoordinateMapper{

	public static final int DEFAULT_WORLD_WIDTH = 20000;
	public static final int DEFAULT_WORLD_HEIGHT = 20000;
	
	private int worldWidth = 20000;
	private int worldHeight = 20000;
	
	private int originWorldX = 10000;
	private int originWorldY = 10000;
	
	private double scale = 1.0d;
	
	public DefaultCoordinateMapper(){
		this(DEFAULT_WORLD_WIDTH, DEFAULT_WORLD_HEIGHT);
	}
	
	public DefaultCoordinateMapper(int worldWidth, int worldHeight){
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.originWorldX = worldWidth / 2;
		this.originWorldY = worldWidth / 2;
	}
	
	public final int getWorldMinX(){
		return 0;
	}
	
	public final int getWorldMinY(){
		return 0;
	}
	
	public final int getWorldCenterX(){
		return worldWidth / 2;
	}
	
	public final int getWorldCenterY(){
		return worldHeight / 2;
	}
	
	public final int getWorldMaxX(){
		return worldWidth;
	}
	
	public final int getWorldMaxY(){
		return worldHeight;
	}
	
	public final int worldXtoLocalX(int worldX){
		//return worldX - originWorldX;
		return BNAUtils.round(((double)worldX - (double)originWorldX) * scale);
	}
	
	public final int worldYtoLocalY(int worldY){
		//return worldY - originWorldY;
		return BNAUtils.round(((double)worldY - (double)originWorldY) * scale);
	}
	
	public final int localXtoWorldX(int localX){
		return originWorldX + BNAUtils.round((double)localX / scale);
		//if(xx < getWorldMinX()) return getWorldMinX();
		//if(xx > getWorldMaxX()) return getWorldMaxX();
		//return xx;
	}
	
	public final int localYtoWorldY(int localY){
		return originWorldY + BNAUtils.round((double)localY / scale);
		//if(yy < getWorldMinY()) return getWorldMinY();
		//if(yy > getWorldMaxY()) return getWorldMaxY();
		//return yy;
	}
	
	public final float worldXtoLocalX(float worldX){
		//return worldX - originWorldX;
		return (float)(((double)worldX - (double)originWorldX) * scale);
	}
	
	public final float worldYtoLocalY(float worldY){
		//return worldY - originWorldY;
		return (float)(((double)worldY - (double)originWorldY) * scale);
	}
	
	public final float localXtoWorldX(float localX){
		return (float)originWorldX + (float)((double)localX / scale);
		//if(xx < getWorldMinX()) return getWorldMinX();
		//if(xx > getWorldMaxX()) return getWorldMaxX();
		//return xx;
	}
	
	public final float localYtoWorldY(float localY){
		return (float)originWorldY + (float)((double)localY / scale);
		//if(yy < getWorldMinY()) return getWorldMinY();
		//if(yy > getWorldMaxY()) return getWorldMaxY();
		//return yy;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.isr.bna4.IMutableCoordinateMapper#repositionRelative(int, int)
	 */
	public void repositionRelative(int dx, int dy){
		originWorldX += dx;
		//if(originWorldX < 0) originWorldX = 0;
		originWorldY += dy;
		//if(originWorldY < 0) originWorldY = 0;
		fireCoordinateMapperEvent(new CoordinateMapperEvent(originWorldX, originWorldY, scale));
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.isr.bna4.IMutableCoordinateMapper#repositionAbsolute(int, int)
	 */
	public void repositionAbsolute(int x, int y){
		originWorldX = x;
		//if(originWorldX < 0) originWorldX = 0;
		originWorldY = y;
		//if(originWorldY < 0) originWorldY = 0;
		fireCoordinateMapperEvent(new CoordinateMapperEvent(originWorldX, originWorldY, scale));
	}

	public double getScale(){
		return scale;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.isr.bna4.IMutableCoordinateMapper#rescaleAbsolute(double)
	 */
	public void rescaleAbsolute(double newScale){
		scale = newScale;
		fireCoordinateMapperEvent(new CoordinateMapperEvent(originWorldX, originWorldY, scale));
	}		
	
	/* (non-Javadoc)
	 * @see edu.uci.isr.bna4.IMutableCoordinateMapper#rescaleRelative(double)
	 */
	public void rescaleRelative(double ds){
		scale += ds;
		fireCoordinateMapperEvent(new CoordinateMapperEvent(originWorldX, originWorldY, scale));
	}		
	
	protected java.util.Vector<ICoordinateMapperListener> listeners = new java.util.Vector<ICoordinateMapperListener>();
	
	public void addCoordinateMapperListener(ICoordinateMapperListener l){
		synchronized(listeners){
			listeners.addElement(l);
		}
	}
	
	public void removeCoordinateMapperListener(ICoordinateMapperListener l){
		synchronized(listeners){
			listeners.removeElement(l);
		}
	}
	
	protected void fireCoordinateMapperEvent(CoordinateMapperEvent evt){
		synchronized(listeners){
			for(int i = 0; i < listeners.size(); i++){
				listeners.elementAt(i).coordinateMappingsChanged(evt);
			}
		}
	}
}
