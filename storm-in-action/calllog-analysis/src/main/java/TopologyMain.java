import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @author Alex
 * Created by Alex on 2018/3/26.
 */
public class TopologyMain {

	public static void main(String[] args) throws InterruptedException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {
		Config config = new Config();
		config.setDebug(true);

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("call-log-reader-spout", new FakeCallLogReaderSpout());

		// shuffleGrouping和fieldsGrouping方法有助于为spout和bolts设置流分组。
		builder.setBolt("call-log-creator-bolt", new CallLogCreatorBolt())
				.shuffleGrouping("call-log-reader-spout");

		builder.setBolt("call-log-counter-bolt", new CallLogCounterBolt())
				.fieldsGrouping("call-log-creator-bolt", new Fields("call"));

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());

		Thread.sleep(30000);


		cluster.shutdown();

	}
}
