package edu.umkc.archstudio4.processor.export;

public class CodeFragment implements Comparable<CodeFragment>
{
	private int start;
	private int stop;

	public CodeFragment(int start, int stop)
	{
		this.start = start;
		this.stop = stop;
	}

	public int getStart()
	{
		return start;
	}

	public int getStop()
	{
		return stop;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof CodeFragment)) return false;
		
		final CodeFragment that = (CodeFragment) obj;
		
		return (that.start == this.start) && (that.stop == this.stop);
	}
	
	@Override
	public int hashCode()
	{
		int code = 17;
		code = 31 * code + start;
		code = 31 * code + stop;
		
		return code;
	}

	@Override
	public int compareTo(CodeFragment that)
	{
		return this.start - that.start;
	}
}