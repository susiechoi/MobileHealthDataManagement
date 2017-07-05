import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class ParseGlucoseData {

	// use LinkedHashMap to maintain original ordering of measure values
	// map 'm' will have dates (in String format) as key with corresponding ArrayList values which
		// will contain Strings that are the measurement values taken on that day
	public static LinkedHashMap<String, ArrayList<String>> m;

	// store data into a file to prepare for writing-out into new, "cleaned" txt file
	public static void toMap(String fileName){
		// initialize Scanner to read uncleaned data file
		Scanner in = null;
		try{
			in = new Scanner(new File(fileName));
		}
		// necessary catch exception
		catch (IOException e){
			e.printStackTrace();
		}

		// we will fill local version of map; global map will contain these values by end of method
		LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();

		// while we still have elements in the uncleaned data file left to read
		while(in.hasNext()){
			// split the line (assuming the line is an entry)
			String[] splitDay = in.nextLine().split("}}");
			// for each string in the line
			for (int i=0; i<splitDay.length; i++){
				// in the glucose data this program was based off of, data was given in
				// measureName => measureValue format, and the last measureName was "captured_at,"
					// and measureValue was the YY-MM-DD for when the measurement was taken
				// if the captured_at date we are looking at does not currently exist in the map as a key,
					// we put the date in the map as a key, and set its value to an empty ArrayList
					// which will contain each of the relevant measurements taken on that day
				String key = splitDay[i].substring(splitDay[i].lastIndexOf("=>")+2);
				if (!map.containsKey(key)){
					map.put(key, new ArrayList<String>());
				}
				// split the entry/line into the comma-separated measurements
				String[] oneDaySplit = splitDay[i].split(",");
				// create a map for each date, with the measureName as the key
				// and measureValue as the value
				// at the end of reading, we will add each of these measureValues to the
				// ArrayList value corresponding to the date of this line/entry
				LinkedHashMap<String, String> valMap = new LinkedHashMap<String, String>();
				// all measureNames will initially have values of '-' as an indicator of missing data
				valMap.put("measuredAt1", "-");
				valMap.put("mealType1", "-");
				valMap.put("beforeMeal1", "-");
				valMap.put("notes1", "-");
				valMap.put("bloodGlucose1", "-");
				valMap.put("measuredAt2", "-");
				valMap.put("mealType2", "-");
				valMap.put("beforeMeal2", "-");
				valMap.put("notes2", "-");
				valMap.put("bloodGlucose2", "-");
				// in our data, the same variable names appear twice; the first time represents the first
				// glucose reading of the day, while the second time represents the second glucose reading
				// the variable startAgain will be set equal to the index of the marker ("||") that indicates
				// the separation between the first and second glucose readings
				int startAgain = 0;
				// loop through each measurement in the entry/line
				for (int j=0; j<oneDaySplit.length; j++){
					// break out of the loop so that we can start looking at second reading measurements
					// in the next for loop
					if (oneDaySplit[j].substring(0,2).equals("||")){
						startAgain = j;
						break;
					}
					String metric = oneDaySplit[j];
					if (metric.substring(0, 10).equals("measuredAt")){
						valMap.put("measuredAt1", metric.substring(12));
					}
					if (metric.substring(0, 8).equals("mealType")){
						valMap.put("mealType1", metric.substring(10));
					}
					if (metric.substring(0, 10).equals("beforeMeal")){
						valMap.put("beforeMeal1", metric.substring(12));
					}
					if (metric.substring(0,5).equals("notes")){
						valMap.put("notes1", metric.substring(7));
					}
					if (metric.substring(0, 12).equals("bloodGlucose")){
						valMap.put("bloodGlucose1", metric.substring(14));
					}
				}
				// for looping through the second reading measurements
				if (startAgain != 0){
					for (int k=startAgain; k<oneDaySplit.length; k++){
						String metric = oneDaySplit[k];
						if (metric.substring(2, 12).equals("measuredAt")){
							valMap.put("measuredAt2", metric.substring(14));
						}
						if (metric.substring(0, 8).equals("mealType")){
							valMap.put("mealType2", metric.substring(10));
						}
						if (metric.substring(0, 10).equals("beforeMeal")){
							valMap.put("beforeMeal2", metric.substring(12));
						}
						if (metric.substring(0,5).equals("notes")){
							valMap.put("notes2", metric.substring(7));
						}
						if (metric.substring(0, 12).equals("bloodGlucose")){
							valMap.put("bloodGlucose2", metric.substring(14));
						}
					}
				}
				// for each key in the measureName-to-measureValue map for this date,
				// add each value to the ArrayList value in the larger map
				// (as explained in line 51)
				for (String valMapKey : valMap.keySet()){
					map.get(key).add(valMap.get(valMapKey));
				}
			}
		}
		// fill the global map with teh values from the local map we just created
		m = map;
	}

	public static void writeTo(String fileName){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			// when importing the text file to Excel, we will use '~' as a delimiter
			// so we will write the title of all measurement values, separated by '~'s, as the first line in the text file
			bw.write("captured_at~measuredAt1~mealType1~beforeMeal1~notes1~bloodGlucose1~measuredAt2~mealType2~beforeMeal2~notes2~bloodGlucose2~");
			// new line to prepare to write out data entries
			bw.write("\n");
			// for each date key in the global map
			for (String key : m.keySet()){
				// write out the date key (corresponding to "captured_at" we wrote as first column header in line 136)
				bw.write(key+"~");
				// write out each of the corresponding measurement values
				// the LinkedHashMap we used will ensure that we obey the ordering of headers in line 136
				for (String val : m.get(key)){
					bw.write(val+"~");
				}
				// new line to prepare for next data entry
				bw.write("\n");
			}
		// necessary catch exception
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			// properly close the writers used to write out the cleaned txt file
			try{
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException{
		// the text file with the uncleaned data that we want to create a map from
		toMap("data/glucoseData.txt");
		// the text file we want to write the cleaned data to
		writeTo("data/cleanedGlucoseData.txt");
	}

}
