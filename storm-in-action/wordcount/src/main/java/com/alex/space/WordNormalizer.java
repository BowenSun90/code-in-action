package com.alex.space;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class WordNormalizer implements IRichBolt {

	private OutputCollector collector;
	private int numCounterTasks;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.numCounterTasks = context.getComponentTasks("word-counter").size();

	}

	@Override
	public void execute(Tuple input) {
		String sentence = input.getString(0);
		String[] words = sentence.split(" ");
		for (String word : words) {
			word = word.trim();
			if (!word.isEmpty()) {
				word = word.toLowerCase();
				//发布这个单词
				List a = new ArrayList();
				a.add(input);
				collector.emit(a, new Values(word));

				// collector.emitDirect(getWordCountIndex(word),new Values(word));
			}


		}

		collector.ack(input);

	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
