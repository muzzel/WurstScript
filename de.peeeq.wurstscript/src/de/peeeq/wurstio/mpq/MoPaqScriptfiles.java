package de.peeeq.wurstio.mpq;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.peeeq.wurstscript.WLogger;

public class MoPaqScriptfiles {

	static File extractFile(File mpqArchive, String fileInMap) {
		File script = new File("./temp/extract.txt");
		script.getParentFile().mkdirs();
		String scriptString = "extract " + "\"" + mpqArchive.getAbsolutePath() + "\"" + " " + fileInMap;  
		try {
			Files.write(scriptString, script, Charsets.UTF_8);
		} catch (IOException e) {
			WLogger.severe(e);
		}
		return script;
	}
	
	static File insertFile(File mpqArchive, File file, String fileInMap) {
		File script = new File("./temp/insert.txt");
		String scriptString = "add " + "\"" + mpqArchive.getAbsolutePath() + "\"" + " " + "\"" + file.getAbsolutePath() + "\"" + " " + fileInMap;  
		try {
			Files.write(scriptString, script, Charsets.UTF_8);
		} catch (IOException e) {
			WLogger.severe(e);
		}
		return script;
	}
	
	static File deleteMapfile(File mpqArchive, String fileInMap) {
		File script = new File("./temp/delete.txt");
		String scriptString = "delete " + "\"" + mpqArchive.getAbsolutePath() + "\"" + " " + fileInMap;  
		try {
			Files.write(scriptString, script, Charsets.UTF_8);
		} catch (IOException e) {
			WLogger.severe(e);
		}
		return script;
	}
	
	static File compactMapfile(File mpqArchive) {
		File script = new File("./temp/compact.txt");
		String scriptString = "compact " + "\"" + mpqArchive.getAbsolutePath() + "\"";  
		try {
			Files.write(scriptString, script, Charsets.UTF_8);
		} catch (IOException e) {
			WLogger.severe(e);
		}
		return script;
	}
}
