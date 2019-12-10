import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TransportMapper extends Mapper<Object, Text, Text, Text> 
{
	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException 
	{
		String[] records = value.toString().split(",");
		if (!"Year".equals(records[0])) 
		{
			if (!"NA".equals(records[20])) 
			{
				context.write(new Text(records[16]), new Text(records[20]));
			}
			if (!"NA".equals(records[19])) 
			{
				context.write(new Text(records[17]), new Text(records[19]));
			}

		}
	}
}
