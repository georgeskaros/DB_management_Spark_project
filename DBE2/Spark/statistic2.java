import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class statistic2 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SparkConf conf = new SparkConf();
		conf.setAppName("BigaData example").setMaster("local[*]");
		JavaSparkContext jsc = new JavaSparkContext(conf);
	
		JavaRDD<String> lines = jsc.textFile(System.getProperty("user.dir")+"/src/db2_project_data.csv");
	
		String header = lines.first();
		lines = lines.filter(row -> !row.equalsIgnoreCase(header));
	
		JavaPairRDD<String, Integer> consumption = lines.mapToPair(s -> {
			String[] foo= s.split(",");
			return new Tuple2<>(foo[3], 1);
		});
	
		JavaPairRDD<String, Integer> result = consumption.reduceByKey((x, y) -> x+y);
		
		System.out.println("Number of unique regions: " + result.count());
	
		jsc.close();
	}

}