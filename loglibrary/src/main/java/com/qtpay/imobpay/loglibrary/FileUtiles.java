package com.qtpay.imobpay.loglibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * com.qtpay.imobpay.tools.FileUtiles Create at 2016-8-1 下午4:51:17
 * 
 * @author Hanpengfei
 * @说明：提供主要方法如下：
 * 1.版本比较函数：h5首页模块和底部条模块比较实现
 * 2.检测文件是否存在
 * 3.指定路径下所有文件和目录全部删除掉
 * 4.检查www包下对应目录名是否存在
 * 5.从文件中读取数据
 * 6.检测SD卡是否可用
 *
 */
public class FileUtiles {
	public static final String imageCachePath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/module/imageCache/";
	public static String imageCachePath_data = "/module/imageCache/";

	//文件夹拷贝成功
	public static int Folder_CP_Success = 0;
	//文件夹拷贝失败-源目录不存在
	public static int Folder_CP_Source_Folder_Fail= 1;
	//文件夹拷贝失败-目标目录已存在
	public static int Folder_CP_Traget_Folder_Fail = 2;
	//文件夹拷贝失败-源文件不存在
	public static int Folder_CP_Source_File_Fail = 3;
	//文件夹拷贝失败-目标文件已存在
	public static int Folder_CP_Traget_File_Fail = 4;
	//文件拷贝成功
	public static int File_CP_Success = 5;
	//文件拷贝失败
	public static int File_CP_Fail = 6;




	/**
	 * @return 无
	 * @throws
	 * @说明：主要是处理CompareModules.Req和CompareBottom.Req，需要将本地文件内容传送到服务器
	 * @Parameters targetPath：目标路径
	 */
	public static String doCompareWithLocalFile(String targetPath) {
		String temp = "";
		if (checkPathExist(targetPath)) {
			try {
				File urlFile = new File(targetPath);
				temp = readDataFromFile(new FileInputStream(urlFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/** check file/floder is exist  检测文件是否存在**/
	public static boolean checkPathExist( String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 指定路径下所有文件和目录全部删除掉
	 * @param context
	 * @param path
	 * @return
	 */
	public static boolean deletePath(Context context, String path) {
		File file = new File(path);

		if (file.isFile()) {
			file.delete();
			return true;
		}

		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return true;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
			return true;
		}
		return false;
	}

	/**
	 * 删除文件
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * 赋值数据到SD卡
	 * @param destPath
	 * @param destFileName
	 * @param sourcePathFileName
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static boolean copyBigDataToSD(String destPath, String destFileName, String sourcePathFileName, Context context) throws IOException
	{
		makeFilePath(destPath, destFileName);
		File mOutput = new File(destPath + destFileName);
		if(!mOutput.exists()) {
			mOutput.getParentFile().mkdirs();
			mOutput.createNewFile();
		}
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(destPath + destFileName);
		myInput = context.getResources().getAssets().open(sourcePathFileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while(length > 0)
		{
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
		return true;
	}

	/**
	 * 将字符串写入到文本文件中：主要是用来写入下载模块的版本信息
	 * @param strcontent
	 * @param filePath
	 * @param fileName
	 * @param replace：true:删除原文件，重新创建；false：附加在文件末尾
	 */
	public static void writeTxtToFile(String strcontent, String filePath, String fileName, boolean replace) {
		//生成文件夹之后，再生成文件，不然会出错
		makeFilePath(filePath, fileName);

		String strFilePath = filePath + fileName;
		// 每次写入时，都换行写
		String strContent = strcontent + "\r\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			else if (replace) {
				file.getAbsoluteFile().delete();
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			if (!replace) {
				raf.seek(file.length());
			}
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}
	}

	/**
	 * 生成文件
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static File makeFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 生成文件夹
	 * @param filePath
	 */
	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			Log.i("error:", e+"");
		}
	}

	/**
	 * 检查www包下对应目录名是否存在
	 * @param pt
	 * @param context
	 * @return
	 */
	public static boolean isTargetExistInAssets(String pt, Context context) {
		AssetManager am = context.getAssets();
		try {
			String[] names = am.list("www");
			for(int i=0;i<names.length;i++){
				if(names[i].equals(pt.trim())){
					return true;
				}else{
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *
	 * @说明：从文件中读取数据
	 * @Parameters InputStream myInput：输入流
	 * @return     返回读取到的数据
	 * @throws IOException
	 * @throws
	 */
	public static String readDataFromFile(InputStream myInput) throws IOException
	{
		String temp = "";
		InputStreamReader isr = new InputStreamReader(myInput, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String mimeTypeLine ;
		while ((mimeTypeLine = br.readLine()) != null) {
			temp += mimeTypeLine;
		}
		br.close();

		return temp;
	}

	/**
	 * 判断是否存在sd卡
	 *
	 * @return
	 */
	public static boolean isAvaiableSdcard() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 覆盖写入版本信息
	 **/
	public static void writeVersion(String fileL, String content) {
		try {
			File file = new File(fileL);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(content);
			bw.newLine();

			bw.flush();
			bw.close();
			osw.close();
			fos.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * @return 无
	 * @throws
	 * @说明：更新模块，直接写到webPlugin目录下version.txt文件中
	 * @Parameters JSONArray modules
	 */
	public static void updateModules(Context context, JSONArray modules, String targetPath) {
		if (modules != null && modules.length() > 0) { // 保留原来的关闭
			if (checkPathExist(targetPath)) {
				// 保存新内容（覆盖）
				FileUtiles.writeVersion(targetPath, modules.toString());
			}
		}
	}

	/**
	 * assets目录比较特殊，不能通过checkPathExist判断目录是否存在,因此增加这个方法来处理。
	 * @param context：当前界面句柄
	 * @param fromFolder：要复制目录
	 * @param toFolder：要移动的目录
	 * @return 返回失败原因
	 */
	public static int copyFilesFassets(Context context, String fromFolder, String toFolder){

		try{

			String fileNames[] = context.getAssets().list(fromFolder);//获取assets目录下的所有文件及目录名

			if(fileNames.length > 0){//如果是目录

				File file=new File(toFolder);

				file.mkdirs();//如果文件夹不存在，则递归

				for(String fileName:fileNames){

					copyFilesFassets(context,fromFolder+"/"+fileName,toFolder+"/"+fileName);

				}

			}else{//如果是文件


				InputStream is = context.getAssets().open(fromFolder);

				FileOutputStream fos = new FileOutputStream(new File(toFolder));

				byte[]buffer=new byte[1024];

				int byteCount=0;

				while((byteCount=is.read(buffer))!= -1){//循环从输入流读取buffer字节

					fos.write(buffer,0,byteCount);//将读取的输入流写入到输出流

				}

				fos.flush();//刷新缓冲区

				is.close();

				fos.close();

			}

		}catch(Exception e){

			//TODO Auto-generated catch block
			e.printStackTrace();

			if ( e instanceof java.io.FileNotFoundException){
				return FileUtiles.Folder_CP_Source_File_Fail;
			} else {
				return FileUtiles.File_CP_Fail;
			}


		}
		return FileUtiles.File_CP_Success;
	}

	/**
	 *   文件夹拷贝方法
	 * @param fromFolder   源文件夹
	 * @param toFolder  目标文件夹
	 * @return
	 */
	public static int copyFolder(String fromFolder, String toFolder)
	{
		//要复制的文件目录
		File[] currentFiles;
		File root;

		//如果不存在则 return出去
		if (!checkPathExist(fromFolder)) {
			return Folder_CP_Source_Folder_Fail;
		}else{
			root = new File(fromFolder);
		}

		//如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		//目标目录
		File targetDir = new File(toFolder);
		//创建目录
		if(!targetDir.exists())
		{
			targetDir.mkdirs();
		}else{
			return Folder_CP_Traget_Folder_Fail;
		}
		//遍历要复制该目录下的全部文件
		for(int i= 0;i<currentFiles.length;i++)
		{    //如果当前项为子目录 进行递归
			if(currentFiles[i].isDirectory())
			{
				copyFolder(currentFiles[i].getPath() + "/", toFolder + currentFiles[i].getName() + "/");

			}else//如果当前项为文件则进行文件拷贝
			{
				copyFile(currentFiles[i].getPath(), toFolder + currentFiles[i].getName());
			}
		}
		return Folder_CP_Success;
	}


	//文件拷贝
	//要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int copyFile(String fromFile, String toFile)
	{

		if(!checkPathExist(fromFile)){
           return Folder_CP_Source_File_Fail;
		}

		if (checkPathExist(toFile)){
			return Folder_CP_Traget_File_Fail;
		}

		try
		{
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return File_CP_Success;

		} catch (Exception ex)
		{
			return File_CP_Fail;
		}
	}



}
