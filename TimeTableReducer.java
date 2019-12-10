
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TimeTableReducer extends Reducer<Text, Text, Text, Text> 
{
	private Map<String, Double> map = new TreeMap<String, Double>();
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
	{
		Iterator<Text> brand = values.iterator();
		int tuple = 0;
		double initial = 0.0;
		while (brand.hasNext()) 
		{
			int initialbrand = Integer.parseInt(brand.next().toString());
			initial = initial + initialbrand;
			tuple  = tuple + 1;
		}
		
		double screen = initial/tuple;
		map.put(key.toString(), Double.valueOf(screen));
	}
	@Override
	protected void cleanup(Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException 
	{	
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(map.entrySet());  
		Collections.sort(list,new Comparator<Map.Entry<String, Double>>() { public int compare(Entry<String, Double> o1, Entry<String, Double> o2) { return o2.getValue().compareTo(o1.getValue()); } } ); 
		context.write(new Text("lowest"), new Text(""));
		int size = list.size();
		int i = size - 1;
		while(i > size - 4)
		{
			Entry<String, Double> entry = list.get(i);
			context.write(new Text(entry.getKey()), new Text(entry.getValue()+""));
			i--; 
		}
		context.write(new Text("highest"), new Text(""));
		int j = 0;
		while(j < 3)
		{
			Entry<String, Double> entry = list.get(j);
			context.write(new Text(entry.getKey()), new Text(entry.getValue()+""));
			j++;
		}
		if(size==0){
			context.write(new Text("No flights Found"), new Text(""));
		}
	}
}