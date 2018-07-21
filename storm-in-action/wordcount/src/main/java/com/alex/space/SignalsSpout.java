package com.alex.space;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 全部数据流组
 *
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class SignalsSpout extends BaseRichSpout {

	private SpoutOutputCollector collector;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		collector.emit("signals", new Values("refreshCache"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO NOTHING
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("signals", new Fields("action"));
	}
}
