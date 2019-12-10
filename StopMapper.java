
/**
 * @author abhij
 *
 */

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StopMapper extends Mapper<Object, Text, Text, IntWritable> 
{
	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException 
	{
		String[] records = value.toString().split(",");
		if (!"Year".equals(records[0])) 
		{
			if ("1".equals(records[21])&&!"NA".equals(records[22])&&records[22].trim().length() > 0) 
			{
				context.write(new Text(records[22]), new IntWritable(1));
			}

		}
	}
}