package com.alex.space;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class TopologyMain {

	public static void main(String[] args) throws InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setSpout("signals-spout",new SignalsSpout());

		builder.setBolt("word-normalizer", new WordNormalizer())
				.shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter(), 4)
				.fieldsGrouping("word-normalizer", new Fields("word"))
				.allGrouping("signals-spout", "signals");

		Config conf = new Config();
		conf.put("wordsFile", "data/log.txt");
		conf.setDebug(false);
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Getting-Started-Topology", conf, builder.createTopology());
		Thread.sleep(10000);
		cluster.shutdown();
	}
}
