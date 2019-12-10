import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TransportReducer extends Reducer<Text, Text, Text, Text> {
	private Map<String, Double> map = new TreeMap<String, Double>();
	private Map<String, Double> execptionMap = new TreeMap<String, Double>();
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
	{
		Iterator<Text> brand = values.iterator();
		int tuple = 0;
		Integer initial = 0;
		while (brand.hasNext()) 
		{
			String temp = brand.next().toString();
			int brandinitial = Integer.parseInt(temp);
			initial = initial + brandinitial;
			tuple++;
		}
		double execution = initial * 1.0 / tuple;
		if(execution == 0.0)
		{
			execptionMap.put(key.toString(), execution);
		}
		else
		{
			map.put(key.toString(), Double.valueOf(execution));
		}

	}

	@Override
	protected void cleanup(Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException 
	{
		if (!map.isEmpty()) 
		{
			List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() { public int compare(Entry<String, Double> o1, Entry<String, Double> o2) { return o2.getValue().compareTo(o1.getValue()); } } );
			
			context.write(new Text("lowest"), new Text(""));
			int size = list.size();
			int i = size - 1;
			while(i > size - 4)
			{
				Entry<String, Double> entry = list.get(i);
				context.write(new Text(entry.getKey()), new Text(entry.getValue() + ""));
				i--;
			}
			context.write(new Text("highest"), new Text(""));
			int j = 0;
			while(j < 3)
			{
				Entry<String, Double> entry = list.get(j);
				context.write(new Text(entry.getKey()), new Text(entry.getValue() + ""));
				j++;
			}
			context.write(new Text("zero data"), new Text(""));

			if(execptionMap.isEmpty()){
				context.write(new Text("NONE"), new Text(""));
			}
			else
			{
				for (Entry<String, Double> entry:execptionMap.entrySet()) 
				{
					context.write(new Text(entry.getKey()), new Text(entry.getValue() + ""));
				}
			}
		}
		else
		{
			context.write(new Text("No Transport Time Found."), new Text(""));
		}
	}
}