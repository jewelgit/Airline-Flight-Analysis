import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TimeTableMapper extends Mapper<Object, Text, Text, Text> 
{
	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException 
	{
		String[] records = value.toString().split(",");
		if (!"Year".equals(records[0])) 
		{
			String initial = "0";
			if(!"NA".equals(records[14]))
			{
				if (Integer.parseInt(records[14]) <= 10) 
				{
					initial = "1";
				}
			context.write(new Text(records[8]), new Text(initial));
			}
			
		}
	}
}