package org.delta;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Delta {
    public static void main(String[] args) {
        // Create a SparkConf object to configure the Spark application
        SparkConf conf = new SparkConf().setAppName("Word Count").setMaster("local[*]");

        // Create a JavaSparkContext object to connect to the Spark cluster
        JavaSparkContext sc = new JavaSparkContext(conf);

        // spark session
        SparkSession spark = SparkSession.builder()
                .appName("Java Spark SQL basic example")
                .getOrCreate();

        Dataset<Row> data = spark.range(0, 5).toDF();
        data.write().format("delta").save("/tmp/delta-table");

        // Stop the Spark context
        sc.stop();
    }
}
