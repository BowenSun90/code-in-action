import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;


import java.util.Map;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class CallLogCreatorBolt implements IRichBolt {

	private OutputCollector collector;

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
		this.collector = outputCollector;
	}

	@Override
	public void execute(Tuple tuple) {
		String from = tuple.getString(0);
		String to = tuple.getString(1);
		Integer duration = tuple.getInteger(2);

		// emit
		collector.emit(new Values(from + " - " + to, duration));
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("call", "duration"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
