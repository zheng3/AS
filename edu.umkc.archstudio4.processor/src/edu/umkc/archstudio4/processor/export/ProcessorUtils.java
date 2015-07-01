package edu.umkc.archstudio4.processor.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

public final class ProcessorUtils
{
	static public class FileFilter {
		static public java.io.FileFilter DEFAULT = new java.io.FileFilter() {
			@Override
			public boolean accept(File f) {
				return !f.isHidden();
			}
		};
	}
	
	static public String nomalizeFeatureName(String featureName)
	{
		return featureName.trim().replace(' ', '_').toUpperCase();
	}
	
	static public String joinStr(String seperator, Object[] fragments)
	{
		final StringBuffer buf = new StringBuffer();
		
		for (Object s : fragments) {
			buf.append(s).append(seperator);
		}
		
		return buf.substring(0, buf.length() - seperator.length());
	}
	
	static public IPath getWorkspaceLocation()
	{
		final IWorkspace iWorkspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot iWorkspaceRoot = iWorkspace.getRoot();
		
		return iWorkspaceRoot.getLocation();
	}

	static public File getProjectSrcDir(String projectName)
	{
		final IPath iLocation = getWorkspaceLocation();
		
        return new File(joinStr(File.separator, new String[] { iLocation.toFile().getPath(), projectName, "src" }));
	}
	
	static public int relocateProjResources(String srcProjPath, String dstProjPath, java.io.FileFilter fileFilter)
	{
		final IPath iLocation = getWorkspaceLocation();
		
		final File srcDir = new File(iLocation.toFile().getPath() + File.separator + srcProjPath);
		final File dstDir = new File(iLocation.toFile().getPath() + File.separator + dstProjPath);
		
		return relocate(srcDir, dstDir, fileFilter);
	}

	static public int relocate(File srcDir, File dstDir, java.io.FileFilter fileFilter)
	{
		int count = 0;
		
		if (fileFilter.accept(dstDir)) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}
			if (srcDir.listFiles() != null) {
				for (File file : srcDir.listFiles()) {
					final String fn = file.getName();
					final File dst = new File(dstDir, fn);
					if (file.isDirectory()) {
						final File src = new File(srcDir, fn);
						count += relocate(src, dst, fileFilter);
					} else if (fileFilter.accept(dst)) {
						InputStream fin = null;
						OutputStream fout = null;
						
						try {
							fin = new FileInputStream(file);
							fout = new FileOutputStream(dst);
							
							final byte[] buffer = new byte[1024];
							int byteCount;
							while((byteCount = fin.read(buffer)) > 0) {
								fout.write(buffer, 0, byteCount);
							}
							
							count += 1;
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (fin != null) {
									fin.close();
								}
								
								if (fout != null) {
									fout.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		return count;
	}
}
