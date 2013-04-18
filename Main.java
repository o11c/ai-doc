import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {


	public String preprocessFile(File f){

		String inputText = "";

		BufferedReader reader = null;

		try{

			reader = new BufferedReader(new FileReader(f));
			String text = null;

			while((text = reader.readLine()) != null){
				inputText.concat(text);
			}
		}catch(FileNotFoundException e){
			System.err.println(e);
		}catch (IOException e) {
			System.err.println(e);
		}finally{
			try{
				if(reader != null){
					reader.close();
				}
			}catch(IOException e){
				System.err.println(e);
			}
		}

		StringBuffer s = new StringBuffer();

		for(int i = 0; i < inputText.length(); i++){
			char c = inputText.charAt(i);
			if(Character.isLetter(c)){
				s.append(c);
			}
		}

		String result = s.toString();

		result.toLowerCase();

		result = result.trim().replaceAll(" +", " ");

		return result;
	}

	public String[] preprocessing(String path){

		File dir = new File(path);
		File[] files = dir.listFiles();
		ArrayList<String> results = new ArrayList<String>();

		for(int i = 0; i < files.length; i++){
			if(files[i].isFile()){
				results.add(preprocessFile(files[i]));
			}
		}

		return (String[]) results.toArray();
	}


	public String IntelliGrep(String s){

		Pattern dt = Pattern.compile("deed of trust");
		Pattern dr = Pattern.compile("deed of reconveyance");
		Pattern l = Pattern.compile("lien");
		int lcount = 0;
		int dtcount = 0;
		int drcount = 0;

		Matcher dtmatch = dt.matcher(s);
		Matcher drmatch = dr.matcher(s);
		Matcher lmatch = l.matcher(s);

		while(dtmatch.find()){
			dtcount++;
		}
		while(drmatch.find()){
			drcount++;
		}
		while(lmatch.find()){
			lcount++;
		}

		Hashtable<String, Integer> d = new Hashtable<String, Integer>();		
		d.put("deed of trust", dtcount);
		d.put("deed of reconveyance", drcount);
		d.put("lien", lcount);
		
		ArrayList<String> maxkeys = new ArrayList<String>();
		int max = 0;
		for(Map.Entry<String, Integer> entry : d.entrySet()){
			if(entry.getValue() > max){
				max = entry.getValue();
				maxkeys.clear();
				maxkeys.add(entry.getKey());
			}
			else if(entry.getValue() == max){
				maxkeys.add(entry.getKey());
			}
		}

		Random r = new Random();
		
		int index = r.nextInt(maxkeys.size());
		
		return maxkeys.get(index);
		
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 4){
			System.err.println("Incorrect Number of Arguments!");
			System.exit(1);
		}
		
		for(int i = 0; i < args.length; i++){
			System.out.println(args[i]);
		}
		

	}

}
