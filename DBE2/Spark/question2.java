import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
public class question2 {

		public static void main(String[] args) {
			SparkConf conf = new SparkConf();
			conf.setAppName("BigaData example").setMaster("local[*]");
			JavaSparkContext jsc = new JavaSparkContext(conf);
			
			JavaRDD<String> lines = jsc.textFile(System.getProperty("user.dir")+"/src/db2_project_data.csv");
			
			String header = lines.first();
			lines = lines.filter(row -> !row.equalsIgnoreCase(header));
			
			JavaPairRDD<String, Tuple2<Double,Double>> consumption = lines.mapToPair(s -> {
				String[] foo= s.split(",");
				return new Tuple2<>(foo[0], new Tuple2<Double,Double>(Double.parseDouble(foo[2]),1.0));
			});

			JavaPairRDD<String, Tuple2<Double,Double>> result = consumption.reduceByKey((x, y) -> {
				return new Tuple2<Double,Double>(x._1+y._1, x._2+y._2);
			});
				
			result.foreach(x->System.out.println("CarId:\t"+x._1 +"\t\tAVG: "+ x._2._1/x._2._2 ));
			
			jsc.close();
		}
	}


