package com.alex.space;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class WordReader implements IRichSpout {

	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	private TopologyContext context;

	public boolean isDistributed() {
		return false;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.context = context;
		this.collector = collector;
		try {
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
		} catch (Exception e) {
			throw new RuntimeException("Error reading file [" + conf.get("wordFile") + "]");
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
	public void nextTuple() {
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//什么也不做
			}
			return;
		}

		String str;
		//创建reader
		BufferedReader reader = new BufferedReader(fileReader);

		try {
			//读所有文本行
			while ((str = reader.readLine()) != null) {
				/**
				 * 按行发布一个新值
				 */
				this.collector.emit(new Values(str), str);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple", e);
		} finally {
			completed = true;
		}

	}

	@Override
	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}

	@Override
	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
