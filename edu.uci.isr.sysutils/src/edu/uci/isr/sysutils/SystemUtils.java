package edu.uci.isr.sysutils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class SystemUtils{

	public static final String fileSeparator = System.getProperty("file.separator");

	public static final int OS_UNKNOWN = -1;

	public static final int OS_WINDOWS = 150;

	public static final int OS_UNIX = 200;

	public static String getCanonicalPath(File f){
		try{
			return f.getCanonicalPath();
		}
		catch(Exception e){
			return f.getAbsolutePath();
		}
	}

	public static File findFileOnSystemPath(String fileName){
		String systemPath = System.getProperty("java.library.path");
		StringTokenizer st = new StringTokenizer(systemPath, System.getProperty("path.separator"));

		while(st.hasMoreTokens()){
			String pathDirString = st.nextToken();
			File pathDir = new File(pathDirString);
			if(pathDir.exists()){
				if(pathDir.isDirectory()){
					File possibleMatch = new File(pathDir, fileName);
					if(possibleMatch.exists()){
						if(!possibleMatch.isDirectory()){
							return possibleMatch;
						}
					}
				}
			}
		}
		return null;
	}

	public static File getFileIfExists(String dir, String filename){
		try{
			File f = new File(dir, filename);
			//System.out.println("Looking for: " + f);
			if(f.exists()){
				//System.out.println("Found: " + f);
				return f;
			}
			return null;
		}
		catch(Exception e){
			return null;
		}
	}

	public static String[] guessJVMs(){
		int os = guessOperatingSystem();
		String javaExecutableName = "java";
		if(os == SystemUtils.OS_WINDOWS){
			javaExecutableName = "java.exe";
		}
		else if(os == SystemUtils.OS_UNIX){
			javaExecutableName = "java";
		}

		HashSet<String> pathSet = new HashSet<String>();
		File javaExecutable = null;

		String javaHome = System.getProperty("java.home");
		if(javaHome != null){
			javaExecutable = getFileIfExists(javaHome, javaExecutableName);
			if(javaExecutable != null){
				pathSet.add(getCanonicalPath(javaExecutable));
			}
		}

		javaHome = javaHome + fileSeparator + "bin";
		if(javaHome != null){
			javaExecutable = getFileIfExists(javaHome, javaExecutableName);
			if(javaExecutable != null){
				pathSet.add(getCanonicalPath(javaExecutable));
			}
		}

		if(os == SystemUtils.OS_UNIX){
			String[] pathsToSearch = new String[]{"/usr/java/bin", "/usr/lib/java/bin", "/usr/local/java/bin", "/opt/java/bin", "/usr/lib/java/bin"};
			for(String element: pathsToSearch){
				javaExecutable = getFileIfExists(element, javaExecutableName);
				if(javaExecutable != null){
					pathSet.add(getCanonicalPath(javaExecutable));
				}
			}

			try{
				String procOutput = SystemUtils.runAndCaptureProcess("which java");
				if(procOutput != null){
					javaExecutable = getFileIfExists(procOutput, javaExecutableName);
					if(javaExecutable != null){
						pathSet.add(getCanonicalPath(javaExecutable));
					}
				}
			}
			catch(IOException ioe){
			}
		}
		else if(os == SystemUtils.OS_WINDOWS){
			String[] pathsToSearch = new String[]{"C:\\WINDOWS", "C:\\WINDOWS\\SYSTEM", "C:\\WINDOWS\\SYSTEM32", "C:\\WINNT", "C:\\WINNT\\SYSTEM", "C:\\WINNT\\SYSTEM32"};
			for(String element: pathsToSearch){
				javaExecutable = getFileIfExists(element, javaExecutableName);
				if(javaExecutable != null){
					pathSet.add(getCanonicalPath(javaExecutable));
				}
			}
		}

		String[] libraryPaths = getLibraryPathEntries();
		if(libraryPaths != null){
			for(String element: libraryPaths){
				javaExecutable = getFileIfExists(element, javaExecutableName);
				if(javaExecutable != null){
					pathSet.add(getCanonicalPath(javaExecutable));
				}
			}
		}

		return pathSet.toArray(new String[0]);
	}

	public static String[] guessLibLocations(String libraryName){
		HashSet<String> pathSet = new HashSet<String>();
		File library = null;

		String javaHome = System.getProperty("java.home");
		if(javaHome != null){
			library = getFileIfExists(javaHome, libraryName);
			if(library != null){
				pathSet.add(getCanonicalPath(library));
			}
		}

		javaHome = javaHome + fileSeparator + "lib";
		if(javaHome != null){
			library = getFileIfExists(javaHome, libraryName);
			if(library != null){
				pathSet.add(getCanonicalPath(library));
			}
		}

		String[] libraryPaths = getClassPathEntries();
		if(libraryPaths != null){
			for(String element: libraryPaths){
				if(element.endsWith(libraryName)){
					File f = new File(element);
					if(f.exists()){
						pathSet.add(getCanonicalPath(f));
					}
				}
			}
		}

		return pathSet.toArray(new String[0]);
	}

	public static int guessOperatingSystem(){
		String osname = System.getProperty("os.name");
		if(osname != null){
			osname = osname.toLowerCase();
			if(osname.indexOf("windows") != -1){
				return OS_WINDOWS;
			}
			if(osname.indexOf("unix") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("solaris") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("aix") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("hpux") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("hp-ux") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("linux") != -1){
				return OS_UNIX;
			}
			if(osname.indexOf("os x") != -1){
				return OS_UNIX;
			}
		}
		String fileSeparator = System.getProperty("file.separator");
		if(fileSeparator != null){
			if(fileSeparator.equals("\\")){
				return OS_WINDOWS;
			}
			if(fileSeparator.equals("/")){
				return OS_UNIX;
			}
		}
		String pathSeparator = System.getProperty("path.separator");
		if(pathSeparator != null){
			if(pathSeparator.equals(";")){
				return OS_WINDOWS;
			}
			if(pathSeparator.equals(":")){
				return OS_UNIX;
			}
		}
		return OS_UNKNOWN;
	}

	public static int runAndLogProcess(Process p, OutputStream logFileOutputStream) throws IOException{
		int exitValue = -1; // returned to caller when p is finished

		InputStream in = p.getInputStream();
		InputStream err = p.getErrorStream();

		ByteArrayOutputStream inBuf = new ByteArrayOutputStream();
		ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

		//StringBuffer inBuf = new StringBuffer();
		//StringBuffer errBuf = new StringBuffer();

		boolean finished = false; // Set to true when p is finished

		while(!finished){
			try{
				while(in.available() > 0){
					blt_noblock(in, inBuf);
				}

				while(err.available() > 0){
					blt_noblock(err, errBuf);
				}

				// Ask the process for its exitValue. If the process
				// is not finished, an IllegalThreadStateException
				// is thrown. If it is finished, we fall through and
				// the variable finished is set to true.

				exitValue = p.exitValue();
				finished = true;

			}
			catch(IllegalThreadStateException e){
				// Process is not finished yet;
				// Sleep a little to save on CPU cycles
				try{
					Thread.sleep(250);
				}
				catch(InterruptedException ie){
				}
			}
		}

		//Grab any leftovers.
		while(in.available() > 0){
			blt_noblock(in, inBuf);
		}

		while(err.available() > 0){
			blt_noblock(err, errBuf);
		}

		in.close();
		err.close();

		//Log the streams:
		println(logFileOutputStream, "Standard output:");
		//inBuf.writeTo(logFileOutputStream);
		println(logFileOutputStream, fixNewlines(inBuf.toString()));

		println(logFileOutputStream, "Standard error:");
		//errBuf.writeTo(logFileOutputStream);
		println(logFileOutputStream, fixNewlines(errBuf.toString()));

		// return completion status to caller
		return exitValue;
	}

	protected static byte[] buf = new byte[2048];

	public static synchronized void blt_noblock(InputStream is, OutputStream os) throws IOException{
		while(is.available() > 0){
			int len = is.read(buf);
			if(len == -1){
				break;
			}
			os.write(buf, 0, len);
		}
	}

	public static synchronized void blt(InputStream is, OutputStream os) throws IOException{
		while(true){
			int len = is.read(buf);
			if(len == -1){
				break;
			}
			os.write(buf, 0, len);
		}
		is.close();
	}

	public static synchronized byte[] blt(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		blt(is, baos);
		return baos.toByteArray();
	}

	public static String runAndCaptureProcess(String cmd) throws IOException{
		Process process = Runtime.getRuntime().exec(cmd);
		return runAndCaptureProcess(process);
	}

	public static String runAndCaptureProcess(Process p) throws IOException{
		InputStream in = null;
		InputStream err = null;
		try{
			in = p.getInputStream();
			err = p.getErrorStream();

			ByteArrayOutputStream inBuf = new ByteArrayOutputStream();
			ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

			//StringBuffer inBuf = new StringBuffer();
			//StringBuffer errBuf = new StringBuffer();

			boolean finished = false; // Set to true when p is finished

			while(!finished){
				try{
					while(in.available() > 0){
						blt_noblock(in, inBuf);
					}

					while(err.available() > 0){
						blt_noblock(err, errBuf);
					}

					// Ask the process for its exitValue. If the process
					// is not finished, an IllegalThreadStateException
					// is thrown. If it is finished, we fall through and
					// the variable finished is set to true.

					finished = true;

				}
				catch(IllegalThreadStateException e){
					// Process is not finished yet;
					// Sleep a little to save on CPU cycles
					try{
						Thread.sleep(250);
					}
					catch(InterruptedException ie){
					}
				}
			}

			//Grab any leftovers.
			while(in.available() > 0){
				blt_noblock(in, inBuf);
			}

			while(err.available() > 0){
				blt_noblock(err, errBuf);
			}

			if(errBuf.toString().trim().length() > 0){
				return inBuf.toString() + System.getProperty("line.separator") + errBuf.toString();
			}
			else{
				return inBuf.toString();
			}
		}
		finally{
			if(in != null){
				try{
					in.close();
				}
				catch(IOException ioe2){
				}
			}
			if(err != null){
				try{
					err.close();
				}
				catch(IOException ioe3){
				}
			}
		}
	}

	private static void println(OutputStream os) throws IOException{
		os.write(System.getProperty("line.separator").getBytes());
	}

	private static void println(OutputStream os, String s) throws IOException{
		os.write(s.getBytes());
		println(os);
	}

	public static String toString(Object value){
		StringBuffer buf = new StringBuffer();
		toString(value, buf);
		return buf.toString();
	}

	public static void toString(Object value, StringBuffer buf){
		toString(value, buf, new HashSet<Object>());
	}

	private static void toString(Object value, StringBuffer buf, Set<Object> dejaVu){
		if(value == null){
			buf.append(value);
		}

		dejaVu.add(value);
		Class<?> clazz = value.getClass();
		if(clazz.isArray()){
			if(clazz == byte[].class){
				buf.append(Arrays.toString((byte[])value));
			}
			else if(clazz == short[].class){
				buf.append(Arrays.toString((short[])value));
			}
			else if(clazz == int[].class){
				buf.append(Arrays.toString((int[])value));
			}
			else if(clazz == long[].class){
				buf.append(Arrays.toString((long[])value));
			}
			else if(clazz == char[].class){
				buf.append(Arrays.toString((char[])value));
			}
			else if(clazz == float[].class){
				buf.append(Arrays.toString((float[])value));
			}
			else if(clazz == double[].class){
				buf.append(Arrays.toString((double[])value));
			}
			else if(clazz == boolean[].class){
				buf.append(Arrays.toString((boolean[])value));
			}
			else{
				buf.append("[");
				for(int i = 0; i < Array.getLength(value); i++){
					if(i > 0){
						buf.append(", ");
					}
					Object element = Array.get(value, i);
					if(dejaVu.contains(element)){
						buf.append("...");
					}
					else{
						toString(Array.get(value, i), buf, dejaVu);
					}
				}
				buf.append("]");
			}
		}
		else{
			buf.append(value);
		}
		dejaVu.remove(value);
	}

	public static boolean containsJavaFiles(File srcDir) throws IOException{
		if(!srcDir.exists()){
			throw new IllegalArgumentException(srcDir.getAbsolutePath() + " does not exist.");
		}
		if(!srcDir.isDirectory()){
			throw new IllegalArgumentException(srcDir.getAbsolutePath() + " is not a directory.");
		}
		File[] children = srcDir.listFiles();
		for(File element: children){
			if(element.getName().toLowerCase().endsWith(".java")){
				return true;
			}
		}
		return false;
	}

	public static void createDirectory(File srcDir) throws IOException{
		if(!srcDir.mkdirs()){
			throw new IOException("Can't create directory: " + srcDir.getAbsolutePath());
		}
	}

	public static void removeDirectory(File srcDir) throws IOException{
		if(!srcDir.exists()){
			return;
		}
		if(!srcDir.isDirectory()){
			throw new IllegalArgumentException(srcDir.getAbsolutePath() + " is not a directory.");
		}

		File baseDir = srcDir.getParentFile();
		String[] src = getFilesRecursive(srcDir.getAbsolutePath());
		for(int i = src.length - 1; i >= 0; i--){
			File s = new File(baseDir, src[i]);
			s.delete();
		}
	}

	public static void copyContents(File baseDirFile, File targetDir) throws IOException{
		copyContents(baseDirFile, targetDir, null);
	}

	public static void copyContents(File baseDirFile, File targetDir, FilenameFilter ff) throws IOException{
		if(!baseDirFile.exists()){
			throw new IllegalArgumentException("Invalid recursion base: " + baseDirFile.getAbsolutePath());
		}

		//Vector v = new Vector();

		File[] children = baseDirFile.listFiles();
		for(File element: children){
			if(element.isDirectory()){
				copyDirectory(element, targetDir, ff);
			}
			else{
				if(ff != null){
					if(ff.accept(baseDirFile, element.getName())){
						copyFile(element, new File(targetDir, element.getName()));
					}
				}
				else{
					copyFile(element, new File(targetDir, element.getName()));
				}
			}
		}
	}

	public static void copyDirectory(File srcDir, File targetDir) throws IOException{
		copyDirectory(srcDir, targetDir, null);
	}

	public static void copyDirectory(File srcDir, File targetDir, FilenameFilter ff) throws IOException{
		if(!targetDir.exists()){
			throw new IllegalArgumentException(targetDir.getAbsolutePath() + " does not exist.");
		}
		if(!targetDir.isDirectory()){
			throw new IllegalArgumentException(targetDir.getAbsolutePath() + " is not a directory.");
		}

		File baseDir = srcDir.getParentFile();
		String[] src = getFilesRecursive(srcDir.getAbsolutePath());
		for(String element: src){
			File s = new File(baseDir, element);
			File d = new File(targetDir, element);
			if(s.isDirectory()){
				if(d.exists() && d.isDirectory()){
				}
				else if(!d.mkdir()){
					throw new IOException("Couldn't create directory: " + d.getAbsolutePath());
				}
			}
			else{
				if(ff != null){
					if(ff.accept(s.getParentFile(), s.getName())){
						copyFile(s, d);
					}
				}
				else{
					copyFile(s, d);
				}
			}
		}
	}

	public static String fixNewlines(String s){
		try{
			StringReader sr = new StringReader(s);
			BufferedReader br = new BufferedReader(sr);
			StringWriter sw = new StringWriter();

			String lineSep = System.getProperty("line.separator");
			while(true){
				String line = br.readLine();
				if(line == null){
					return sw.toString();
				}
				sw.write(line);
				sw.write(lineSep);
			}
		}
		catch(IOException doesntHappen){
			return null;
		}
	}

	public static void copyFile(File inFile, File outFile) throws IOException{
		FileOutputStream fos = new FileOutputStream(outFile);
		FileInputStream fis = new FileInputStream(inFile);
		blt(fis, fos);
		fos.close();
	}

	public static String[] getContentsRecursive(String baseDir){
		File baseDirFile = new File(baseDir);
		if(!baseDirFile.exists()){
			throw new IllegalArgumentException("Invalid recursion base: " + baseDir);
		}

		Vector<String> v = new Vector<String>();

		File[] children = baseDirFile.listFiles();
		for(File element: children){
			if(element.isDirectory()){
				String[] arr = getFilesRecursive(element.getAbsolutePath());
				for(String element2: arr){
					v.addElement(element2);
				}
			}
		}
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}

	public static String[] getFilesRecursive(String baseDir){
		File baseDirFile = new File(baseDir);
		if(!baseDirFile.exists()){
			throw new IllegalArgumentException("Invalid recursion base: " + baseDir);
		}
		Vector<File> directories = new Vector<File>();
		Vector<File> files = new Vector<File>();
		getFilesRecursive(baseDirFile, directories, files);
		String[] ret = new String[directories.size() + files.size()];
		int i = 0;
		for(File element: directories){
			ret[i++] = getRelativePath(baseDirFile, element);
		}
		for(File element: files){
			ret[i++] = getRelativePath(baseDirFile, element);
		}
		return ret;
	}

	public static String getRelativePath(File baseDir, File f){
		String p;
		if(f.isDirectory()){
			p = f.getName() + fileSeparator;
		}
		else{
			p = f.getName();
		}
		if(baseDir.equals(f)){
			return p;
		}

		while(true){
			f = f.getParentFile();
			if(f == null){
				throw new IllegalArgumentException("File was not contained in base!");
			}
			else if(f.equals(baseDir)){
				p = f.getName() + fileSeparator + p;
				return p;
			}
			else{
				p = f.getName() + fileSeparator + p;
			}
		}
	}

	public static void getFilesRecursive(File base, Vector<File> directories, Vector<File> files){
		if(base.isDirectory()){
			directories.addElement(base);
			File[] children = base.listFiles();
			for(File element: children){
				getFilesRecursive(element, directories, files);
			}
		}
		else{
			files.addElement(base);
		}
	}

	public static int waitForProcess(Process p){
		while(true){
			try{
				int rv = p.exitValue();
				return rv;
			}
			catch(IllegalThreadStateException itse){
			}
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
			}
		}
	}

	static class ExcludingFilenameFilter
	    implements java.io.FilenameFilter{

		String[] extensionsToExclude;

		public ExcludingFilenameFilter(String[] extensionsToExclude){
			this.extensionsToExclude = extensionsToExclude;
			for(int i = 0; i < extensionsToExclude.length; i++){
				extensionsToExclude[i] = extensionsToExclude[i].toLowerCase();
			}
		}

		public boolean accept(File dir, String name){
			for(String element: extensionsToExclude){
				if(name.toLowerCase().endsWith(element)){
					return false;
				}
			}
			return true;
		}
	}

	public static String[] getLibraryPathEntries(){
		String libraryPath = System.getProperty("java.library.path");
		if(libraryPath == null){
			return null;
		}
		String pathSeparator = System.getProperty("path.separator");
		if(pathSeparator == null){
			return null;
		}
		StringTokenizer tok = new StringTokenizer(libraryPath, pathSeparator);
		ArrayList<String> pathEntries = new ArrayList<String>();
		while(tok.hasMoreTokens()){
			pathEntries.add(tok.nextToken().trim());
		}
		return pathEntries.toArray(new String[0]);
	}

	public static String[] getClassPathEntries(){
		return getClassPathEntries(false);
	}

	public static String[] getClassPathEntries(boolean includeCurrentDirectory){
		String libraryPath = System.getProperty("java.class.path");
		if(libraryPath == null){
			return null;
		}
		String pathSeparator = System.getProperty("path.separator");
		if(pathSeparator == null){
			return null;
		}
		StringTokenizer tok = new StringTokenizer(libraryPath, pathSeparator);
		ArrayList<String> pathEntries = new ArrayList<String>();
		if(includeCurrentDirectory){
			pathEntries.add(".");
		}
		while(tok.hasMoreTokens()){
			pathEntries.add(tok.nextToken().trim());
		}
		return pathEntries.toArray(new String[0]);
	}

	public static final DateFormat DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);

	public static String getDateAndTime(){
		return DATE_TIME_FORMAT.format(new java.util.Date());
	}

	public static Date parseDate(String s){
		java.util.Date d = null;

		d = parseDate(s, DateFormat.FULL);
		if(d != null){
			return d;
		}

		d = parseDate(s, DateFormat.LONG);
		if(d != null){
			return d;
		}

		d = parseDate(s, DateFormat.MEDIUM);
		if(d != null){
			return d;
		}

		d = parseDate(s, DateFormat.SHORT);
		if(d != null){
			return d;
		}

		return null;
	}

	public static Date parseDate(String s, int format){
		try{
			DateFormat df = DateFormat.getDateInstance(format, Locale.US);

			//I finally figured out what "lenient" means--this means that dates like
			//"Feburary 95, 1999" will be the 95th day after Feb 1, 1999.  It doesn't 
			//allow off-formats at all, as the Java documentation describes!!!
			df.setLenient(false);
			Date newDate = df.parse(s);
			if(newDate != null){
				return new Date(newDate.getTime());
			}
			else{
				return null;
			}
		}
		catch(java.text.ParseException e){
			return null;
		}
	}

	public static InputStream openURL(String urlString) throws MalformedURLException, FileNotFoundException, IOException{
		return openURL(urlString, null);
	}

	public static InputStream openURL(String urlString, Class<?> resourceClass) throws MalformedURLException, FileNotFoundException, IOException{
		if(urlString.startsWith("file:")){
			URL fileURL = new URL(urlString);
			String filePath = fileURL.getFile(); //Amazingly, this works (albeit for file:// URLs only)
			File file = new File(filePath);
			if(!file.exists()){
				throw new FileNotFoundException(file.getPath());
			}
			if(!file.canRead()){
				throw new IOException("Can't read file: " + file.getPath());
			}
			FileInputStream fis = new FileInputStream(file);
			return fis;
		}
		else if(urlString.startsWith("http:")){
			URL httpURL = new URL(urlString);
			return httpURL.openStream();
		}
		else if(urlString.startsWith("res:")){
			String path = urlString.substring(4);
			while(path.startsWith("/")){
				path = path.substring(1);
			}
			if(resourceClass == null){
				resourceClass = SystemUtils.class;
			}
			//return ClassLoader.getSystemResourceAsStream(path);
			return resourceClass.getResourceAsStream("/" + path);
		}
		else{
			throw new MalformedURLException("Invalid URL: " + urlString);
		}
	}

	/*
	 * public static InputStream openURL(String urlString) throws
	 * MalformedURLException, FileNotFoundException, IOException { if
	 * (urlString.startsWith("file:")) { URL fileURL = new URL(urlString);
	 * String filePath = fileURL.getFile(); //Amazingly, this works (albeit for
	 * file:// URLs only) File file = new File(filePath); if (!file.exists()) {
	 * throw new FileNotFoundException(file.getPath()); } if (!file.canRead()) {
	 * throw new IOException("Can't read file: " + file.getPath()); }
	 * FileInputStream fis = new FileInputStream(file); return fis; } else if
	 * (urlString.startsWith("http:")) { URL httpURL = new URL(urlString);
	 * return httpURL.openStream(); } else { throw new
	 * MalformedURLException("Invalid URL: " + urlString); } }
	 */

	public static String capFirst(String s){
		if(s != null && s.length() > 0){
			char ch = s.charAt(0);
			char uch = Character.toUpperCase(ch);
			if(ch != uch){
				char[] chars = s.toCharArray();
				chars[0] = uch;
				s = new String(chars);
			}
		}
		return s;
	}

	public static String uncapFirst(String s){
		if(s != null && s.length() > 0){
			char ch = s.charAt(0);
			char lch = Character.toLowerCase(ch);
			if(ch != lch){
				char[] chars = s.toCharArray();
				chars[0] = lch;
				s = new String(chars);
			}
		}
		return s;
	}

	public static <T>Set<T> diffSet(Collection<? extends T> a, Collection<? extends T> b){
		Set<T> diff;
		if(a != null){
			diff = new HashSet<T>(a);
			if(b != null){
				for(T o: b){
					if(diff.contains(o)){
						diff.remove(o);
					}
					else{
						diff.add(o);
					}
				}
			}
		}
		else if(b != null){
			diff = new HashSet<T>(b);
		}
		else{
			diff = Collections.emptySet();
		}
		return diff;
	}

	public static boolean deepEquals(Object o1, Object o2){
		if(o1 == o2){
			return true;
		}
		if(o1 == null || o2 == null){
			return false;
		}

		Class<?> c1 = o1.getClass();
		Class<?> c2 = o2.getClass();

		if(!c1.equals(c2)){
			return false;
		}

		if(c1.isArray()){
			if(c1 == byte[].class){
				return Arrays.equals((byte[])o1, (byte[])o2);
			}
			else if(c1 == short[].class){
				return Arrays.equals((short[])o1, (short[])o2);
			}
			else if(c1 == int[].class){
				return Arrays.equals((int[])o1, (int[])o2);
			}
			else if(c1 == long[].class){
				return Arrays.equals((long[])o1, (long[])o2);
			}
			else if(c1 == char[].class){
				return Arrays.equals((char[])o1, (char[])o2);
			}
			else if(c1 == float[].class){
				return Arrays.equals((float[])o1, (float[])o2);
			}
			else if(c1 == double[].class){
				return Arrays.equals((double[])o1, (double[])o2);
			}
			else if(c1 == boolean[].class){
				return Arrays.equals((boolean[])o1, (boolean[])o2);
			}
			else{
				int l1 = Array.getLength(o1);
				int l2 = Array.getLength(o2);

				if(l1 != l2){
					return false;
				}
				for(int i = 0; i < l1; i++){
					if(!deepEquals(Array.get(o1, i), Array.get(o2, i))){
						return false;
					}
				}
				return true;
			}
		}
		return o1.equals(o2);
	}
}
