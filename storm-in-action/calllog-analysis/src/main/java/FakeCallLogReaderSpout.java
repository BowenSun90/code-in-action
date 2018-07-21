import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class FakeCallLogReaderSpout implements IRichSpout {

	private SpoutOutputCollector collector;
	private TopologyContext context;
	private Random randomGenerator = new Random();
	private Integer idx = 0;

	/**
	 * Spout执行环境
	 */
	@Override
	public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
		this.context = topologyContext;
		this.collector = spoutOutputCollector;
	}

	/**
	 * 通过收集器发出生成的数据
	 */
	@Override
	public void nextTuple() {
		if (this.idx <= 1000) {
			List<String> mobileNumbers = new ArrayList<String>();
			mobileNumbers.add("1234123401");
			mobileNumbers.add("1234123402");
			mobileNumbers.add("1234123403");
			mobileNumbers.add("1234123404");
			mobileNumbers.add("1234123405");
			mobileNumbers.add("1234123406");

			Integer localIdx = 0;
			while (localIdx++ < 100 && this.idx++ < 1000) {
				String fromMobileNumber = mobileNumbers.get(randomGenerator.nextInt(6));
				String toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(6));
				while (fromMobileNumber == toMobileNumber) {
					toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(6));
				}

				Integer duration = randomGenerator.nextInt(60);
				// emit
				this.collector.emit(new Values(fromMobileNumber, toMobileNumber, duration));
			}

		}
	}

	@Override
	public void close() {

	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}


	@Override
	public void ack(Object o) {

	}

	@Override
	public void fail(Object o) {

	}

	/**
	 * 用于指定元组的输出模式
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("from", "to", "duration"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
