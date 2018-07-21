package com.alex.space;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class WordCounter implements IRichBolt {

	Integer id;
	String name;
	Map<String, Integer> counters;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.counters = new HashMap<>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}

	@Override
	public void execute(Tuple input) {
		String str = input.getString(0);

		try {
			if (input.getSourceStreamId().equals("signals")) {
				str = input.getStringByField("action");
				if ("refreshCache".equals(str)) {
					counters.clear();
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO NOTHING
		}

		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
		//对元组作为应答
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		System.out.println("-- 单词数 【" + name + "-" + id + "】 --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.out.println("-- " + entry.getKey() + ": " + entry.getValue());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
