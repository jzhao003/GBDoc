package gbdoc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import gbdoc.doc.GbFileParagraph;

public class GbDocFileUtil {

	public static String getFileExtendName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	public static void replaceTemplatesWord(File file, String section, String[] inputValues) {
		XWPFDocument xdoc;
		try {
			xdoc = new XWPFDocument(OPCPackage.open(file));
			List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

			List<XWPFParagraph> list = findParagraphsFromList(paragraphList, section);
			int inputValueIndex = 0;
			for (XWPFParagraph paragraph : list) {
				boolean redFlag = false;
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					// 去掉 section number
					if (text.equals("#" + section + "#")) {
						run.setText("", 0);
						run.setColor("000000");
						continue;
					}
					if (redFlag) {
						// 如果前面一个run是红色字符
						if (isRedFont(run.getColor())) {
							run.setColor("000000");
							run.setText("", 0);
							redFlag = true;
						} else {
							// 如果前面一个run是红色，而当前run不是，则 先replace template，再把当前run的字符加在后面
							run.setText(inputValues[inputValueIndex] + text, 0);
							inputValueIndex++;
							redFlag = false;
						}
					} else {
						if (isRedFont(run.getColor())) {
							// 如果当前字符是红色，flag设置成 true
							redFlag = true;
							// 如果当前run也是红色字符，则改为白色，并删除
							run.setColor("000000");
							run.setText("", 0);
						} else {
							redFlag = false;
						}
					}
				}
			}
			File newFile = new File(file.getName() + "1");
			xdoc.write(new FileOutputStream(newFile));
			xdoc.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<XWPFParagraph> findParagraphsFromList(List<XWPFParagraph> allParagraphs, String section) {
		List<XWPFParagraph> targetParas = null;
		for (XWPFParagraph para : allParagraphs) {
			if (para.getText().startsWith("#" + section + "#")) {
				// 找到开头的段落
				targetParas = new ArrayList<XWPFParagraph>();
				targetParas.add(para);
				continue;
			}
			if ((targetParas != null && targetParas.size() != 0)) {
				// 找到结束段落
				if (!para.getText().startsWith("#")) {
					targetParas.add(para);
				} else {
					break;
				}
			}
		}
		return targetParas;
	}

	public static GbFileParagraph findParagraphs(File file, String section) {

		GbFileParagraph gbFileParagraph = new GbFileParagraph();
		List<XWPFParagraph> targetParas = null;
		try {
			// open word file
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file));
			List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

			for (XWPFParagraph para : paragraphList) {
				if (para.getText().startsWith("#" + section + "#")) {
					// 找到开头的段落
					System.out.println("====== " + file);
					gbFileParagraph.setFile(file);
					targetParas = new ArrayList<XWPFParagraph>();
					targetParas.add(para);
					continue;
				}
				if ((targetParas != null && targetParas.size() != 0)) {
					// 找到结束段落
					if (!para.getText().startsWith("#")) {
						targetParas.add(para);
					} else {
						break;
					}
				}
			}
			gbFileParagraph.setParagraphList(targetParas);
			xdoc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gbFileParagraph;
	}

	public static GbFileParagraph findParagraphsInDirectory(File dir, String section) {
		GbFileParagraph gbFileParagraph = null;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (gbFileParagraph == null || gbFileParagraph.getFile() == null) {
				if (file.isDirectory()) {
					gbFileParagraph = findParagraphsInDirectory(file, section);
				} else {
					if (getFileExtendName(file.getName()).equals("docx") && file.length() != 0) {
						System.out.println(file.getAbsolutePath());
						gbFileParagraph = findParagraphs(file, section);

					}
				}
			}
		}
		return gbFileParagraph;
	}

	public static boolean isRedFont(String color) {
		return (color != null) && (color.equals("FF0000"));
	}

	public static void main(String args[]) {
		File file = new File("/Users/jzhao/work/temp/2第二章 组织和管理.docx");
		String[] input = { "Hello", "World", "c", "d", "e" };
		replaceTemplatesWord(file, "7.1.1", input);
	}
}
