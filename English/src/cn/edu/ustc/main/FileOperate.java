package cn.edu.ustc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

public class FileOperate {

	public boolean getSDcard(){
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
				return true;
		return false;
	}
	
	public boolean isXmlFile(String filepath){
		//File
		return false;
	}

	public boolean copyFile(File sourcefile,File targetfile) throws IOException {
		int bytesum = 0;   
        int byteread = 0; 
        FileOutputStream fs =null;
        InputStream inStream = null;
        //File oldfile = new File(oldPath);   
        if (sourcefile.exists()) { //文件不存在时   
        	inStream  = new FileInputStream(sourcefile); //读入原文件   
            fs = new FileOutputStream(targetfile);   
            byte[] buffer = new byte[1024];   
            while ( (byteread = inStream.read(buffer)) != -1) {   
                bytesum += byteread; //字节数 文件大小   
                System.out.println(bytesum);   
                fs.write(buffer, 0, byteread);   
            }   
            inStream.close();   
        }
		return false;   
		
	}
	
	public boolean deleteFile(String filepath){
		File file = new File(filepath);
		if (file == null || !file.exists() || file.isDirectory())  
            return false;  
		return file.delete();
	}
	
	
	/**
	 * 文件重命名
	 */
	public boolean renameFile(String filepath,String newpath){
		File file = new File (filepath);
		if (file == null || !file.exists() || file.isDirectory())  
            return false;  
		return file.renameTo(new File(newpath));
		
	}
	
	public boolean newFile(String path){
		try {
			File file = new File(path);
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.write("<book>");
			out.newLine();
			out.write("</book>");
			out.close();
			out = null;
			file = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
