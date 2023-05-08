package org.delta;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;

public class Delta {
  public static void main(String[] args) throws TimeoutException, StreamingQueryException {
    // Create a SparkConf object to configure the Spark application
    SparkConf conf = new SparkConf().setAppName("DeltaApp").setMaster("local[*]");

    // Create a JavaSparkContext object to connect to the Spark cluster
    JavaSparkContext sc = new JavaSparkContext(conf);

    // spark session
    SparkSession spark = SparkSession.builder()
        .appName("DeltaAppExample")
        .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
        .getOrCreate();

    // create a schema for the delta table
    StructType schema = new StructType()
        .add("id", "integer")
        .add("name", "string")
        .add("age", "integer");

    // read data from a csv file
    Dataset<Row> df = spark.readStream().schema(schema).csv("data.csv");

    // df.write().format("delta").save("delta-table");
    df.writeStream().format("console").outputMode("append").start().awaitTermination();

    // read the delta table
    // Dataset<Row> df2 = spark.read().format("delta").load("delta-table");
    //
    // df2.show();

    // Stop the Spark context
    sc.stop();
  }
}
