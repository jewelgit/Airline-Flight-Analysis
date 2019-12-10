import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StopReducer extends Reducer<Text, IntWritable, Text, Text> 
{
	private Map<String, Integer> map = new TreeMap<String, Integer>();
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
	{
		Iterator<IntWritable> brand = values.iterator();
		for(Integer initial = 0, initialbrand = Integer.parseInt(brand.next().toString()); brand.hasNext(); initial = initial + initialbrand) 	
		map.put(key.toString(), initial);
	}
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException 
	{
		if (!map.isEmpty()) 
		{
			List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) { return o2.getValue().compareTo(o1.getValue());}});
			Entry<String, Integer> entry = list.get(0);
			//A = CarrierDelay, B = WeatherDelay, C = NASDelay, D = SecurityDelay
			boolean flag = false;
			if("A".equals(entry.getKey()))
			{
				flag =true;
				context.write(new Text("Cancellation Code is A: CarrierDelay"), new Text("The count is: "+entry.getValue() + ""));
			}
			else if("B".equals(entry.getKey()))
			{
				flag =true;
				context.write(new Text("Cancellation Code is B: WeatherDelay"), new Text("The count is: "+entry.getValue() + ""));
			}
			else if("C".equals(entry.getKey()))
			{
				flag =true;
				context.write(new Text("Cancellation is C: NASDelay"), new Text("The count is: "+entry.getValue() + ""));			
			}
			else if("D".equals(entry.getKey()))
			{
				flag =true;
				context.write(new Text("Cancellation Code is D: SecurityDelay"), new Text("The count is: "+entry.getValue() + ""));
			}
			
			if(!flag)
			{
				context.write(new Text("No Reason for flight stop."), new Text(""));
			}
			
		}
		else{
			context.write(new Text("No Reason for flight stop."), new Text(""));
		    }
	}
}