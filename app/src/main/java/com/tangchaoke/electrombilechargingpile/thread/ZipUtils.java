package com.tangchaoke.electrombilechargingpile.thread;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {

	private static String outPath;

	public static ZipUtils getInstance() {
		return new ZipUtils();
	}

	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 * @param descDir
	 * @author czc
	 */
	public String unZipFiles(String zipPath, String descDir) throws IOException {
		return unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author czc
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public String unZipFiles(File zipFile, String descDir) throws IOException {
		
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + File.separator + zipEntryName)
					.replaceAll("\\*", File.separator);
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0,
					outPath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				// 进入下一次循环
				continue;
			}
			// 输出文件路径信息
//			MLog.d("ZipUtils", "outPath::"+outPath);

			setOutPath(outPath);
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
//			FileUtils.readLogfileToJsonfile(outPath);
		}

		Log.i("ChatController", "******************解压完毕********************");
		// 解压完毕后删除下载好的zip原文件
		zipFile.delete();
		return outPath;
	}

	private void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	
	public String getOutPath(){
		return outPath;
	}
}
