import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ParsingSleepData {

	public static HashMap<String, ArrayList<String>> m;

	public static void read(String fileName){
		Scanner in = null;
		try{
			in = new Scanner(new File(fileName));
		}
		catch (IOException e){
			e.printStackTrace();
		}

		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		while(in.hasNext()){
			String[] splitLine = in.nextLine().split(",");
			map.put(splitLine[0], new ArrayList<String>());
			for (int i=1; i<splitLine.length; i++){
				String s = splitLine[i];
				if(s.length() > 5 && !s.substring(0, 5).equals("value") && !s.substring(0, 5).equals("logId")){
					if (s.length() > 8 && !s.substring(0, 8).equals("dateTime")){
						if (s.length() > 10 && !s.substring(0, 10).equals("minuteData")){
							map.get(splitLine[0]).add(s.substring(s.lastIndexOf("=>")+2));
						}
					}
				}
			}
		}

		m = map;
	}

	public static void writeTo(String fileName){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write("id~awakeCount~awakeDuration~awakeningsCount~dateOfSleep~duration~efficiency~isMainSleep~minutesAfterWakeup~minutesAsleep~minutesAwake~minutesToFallAsleep~restlessCount~restlessDuration~startTime~timeInBed~totalMinutesAsleep~totalSleepRecords~totalTimeInBed~captured_at");
			bw.write("\n");
			for (String key : m.keySet()){
				bw.write(key+"~");
				for (String val : m.get(key)){
					bw.write(val+"~");
				}
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
		read("data/sleepData.txt");
		writeTo("data/cleanedSleepData.txt");
	}

}
