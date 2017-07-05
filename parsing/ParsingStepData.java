import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ParsingStepData {

	public static HashMap<String, String> m;

	public static void read(String fileName){
		Scanner in = null;
		try{
			in = new Scanner(new File(fileName));
		}
		catch (IOException e){
			e.printStackTrace();
		}

		HashMap<String, String> map = new HashMap<String, String>();

		while(in.hasNext()){
			String[] splitLine = in.nextLine().split(",");
			map.put(splitLine[0], splitLine[2]);
		}

		m = map;
	}

	public static void writeTo(String fileName){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write("Captured At~Steps");
			bw.write("\n");
			for (String key : m.keySet()){
				bw.write(key+"~");
				bw.write(m.get(key)+"~");
				bw.write("\n");
			}
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try{
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		read("data/stepData.txt");
		writeTo("data/cleanedStepData.txt");
	}

}
