package com.dingmk.comm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.dingmk.comm.constvar.SysConstVar;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhiming on 2016/7/27.
 */
@Slf4j
public final class ZipUtil {

	/**
	 * gzip压缩
	 * 
	 * @param content
	 * 			@return @throws
	 */
	public static byte[] gzip(byte[] content, int off, int len) {
		GZIPOutputStream gos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(baos);
			gos.write(content, off, len);
			gos.finish();
			gos.flush();
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			log.error("gzip meet exception. " + e.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(gos);
		}

		return null;
	}

	/**
	 * gzip压缩 @param content @return @throws
	 */
	public static byte[] gzip(byte[] content) {
		Preconditions.checkNotNull(content);
		return gzip(content, 0, content.length);
	}

	public static byte[] unGzip(byte[] content) {
		Preconditions.checkNotNull(content);
		GZIPInputStream gis = null;

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
			gis = new GZIPInputStream(bais);

			byte[] buffer = new byte[SysConstVar.STREAM_BUFF_SIZE];

			int n;
			while ((n = gis.read(buffer)) != -1) {
				baos.write(buffer, 0, n);
			}

			baos.flush();

			return baos.toByteArray();
		} catch (IOException e) {
			log.error("unGzip meet exception. " + e.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(gis);
		}

		return null;
	}

	/**
	 * 对输入字节数组进行压缩
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] zipBytes(byte[] bytes, int length, String entryName) {
		Preconditions.checkNotNull(bytes);

		ByteArrayOutputStream tempOStream = null;
		BufferedOutputStream tempBOStream = null;
		ZipOutputStream tempZStream = null;

		byte[] result = null;
		try {
			tempOStream = new ByteArrayOutputStream(bytes.length);
			tempBOStream = new BufferedOutputStream(tempOStream);
			tempZStream = new ZipOutputStream(tempBOStream);

			ZipEntry tempEntry = new ZipEntry(entryName);
			tempEntry.setMethod(ZipEntry.DEFLATED);
			tempEntry.setSize((long) length);

			tempZStream.putNextEntry(tempEntry);
			tempZStream.write(bytes, 0, length);
			tempZStream.flush();
			tempBOStream.flush();
			tempOStream.flush();

			result = tempOStream.toByteArray();
		} catch (Exception e) {
			log.error("zipBytes meet exception. ", e);
		} finally {
			IOUtils.closeQuietly(tempZStream);
			IOUtils.closeQuietly(tempOStream);
			IOUtils.closeQuietly(tempBOStream);
		}

		return result;
	}

	/**
	 * 对输入字节数组进行压缩
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] zipBytes(byte[] bytes, String entryName) {
		return zipBytes(bytes, bytes.length, String.valueOf(bytes.length));
	}

	/**
	 * 对输入字节数组进行压缩
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] zipBytes(byte[] bytes) {
		return zipBytes(bytes, String.valueOf(bytes.length));
	}

	/**
	 * 解压字符数组到指定输出流
	 *
	 * @param bytes
	 * @param os
	 */
	public static void unzipBytes(byte[] bytes, OutputStream os) {
		Preconditions.checkNotNull(bytes);
		Preconditions.checkNotNull(os);

		ByteArrayInputStream tempIStream = null;
		BufferedInputStream tempBIStream = null;
		ZipInputStream tempZIStream = null;

		try {
			tempIStream = new ByteArrayInputStream(bytes, 0, bytes.length);
			tempBIStream = new BufferedInputStream(tempIStream);
			tempZIStream = new ZipInputStream(tempBIStream);

			ZipEntry tempEntry = tempZIStream.getNextEntry();
			if (tempEntry != null) {
				long tempDecompressedSize = tempEntry.getCompressedSize();
				if (tempDecompressedSize < 0) {
					tempDecompressedSize = Long.parseLong(tempEntry.getName());
				}

				int size = (int) tempDecompressedSize;
				byte[] tempUncompressedBuf = new byte[size];
				int num = 0;
				while (true) {
					int count = tempZIStream.read(tempUncompressedBuf, 0, size - num);
					num += count;
					os.write(tempUncompressedBuf, 0, count);
					os.flush();
					if (num >= size) {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("unzipBytes meet exception. ", e);
		} finally {
			IOUtils.closeQuietly(tempIStream);
			IOUtils.closeQuietly(tempBIStream);
			IOUtils.closeQuietly(tempZIStream);
		}
	}

	/**
	 * zip压缩文件
	 *
	 * @param orgFile
	 * @param zipFile
	 * @return
	 */
	public static void zipFile(File orgFile, File zipFile) {
		Preconditions.checkNotNull(orgFile);
		Preconditions.checkNotNull(zipFile);

		InputStream orgInput = null;
		ZipOutputStream zipOut = null;
		try {
			orgInput = new FileInputStream(orgFile);
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.putNextEntry(new ZipEntry(orgFile.getName())); // 设置ZipEntry对象
			int temp = 0;
			while ((temp = orgInput.read()) != -1) {
				zipOut.write(temp); // 压缩输出
			}

			log.debug("zipFile successfully. orgFilePath:{}", orgFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("zipFile meet exception. ", e);
		} finally {
			IOUtils.closeQuietly(orgInput);
			IOUtils.closeQuietly(zipOut);
		}
	}

	public static void unzipFile(File orgFile, File unzipFile) {
		Preconditions.checkNotNull(orgFile);
		Preconditions.checkNotNull(unzipFile);

		InputStream eis = null;
		FileOutputStream unzipOut = null;
		try (ZipFile zipFile = new ZipFile(orgFile)) {
			Enumeration<?> emu = zipFile.entries();
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();

				eis = zipFile.getInputStream(entry);
				byte[] buffer = new byte[SysConstVar.STREAM_BUFF_SIZE];
				int bytesRead = 0;

				unzipOut = new FileOutputStream(unzipFile);
				while ((bytesRead = eis.read(buffer)) != -1) {
					unzipOut.write(buffer, 0, bytesRead);
				}
			}

			log.debug("unzipFile successfully. orgFilePath:{}", orgFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("unzipFile meet exception. ", e);
		} finally {
			IOUtils.closeQuietly(eis);
			IOUtils.closeQuietly(unzipOut);
		}
	}

	/**
	 * 创建ZIP文件
	 *
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void createZip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 写入ZIP文件
	 *
	 * @param file
	 *            文件
	 * @param parentPath
	 * @param zos
	 */
	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				if (files != null && files.length > 0) {
					for (File f : files) {
						writeZip(f, parentPath, zos);
					}
				}
			} else {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}
}
