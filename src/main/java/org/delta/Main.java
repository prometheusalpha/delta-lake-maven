package org.delta;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    // Create a SparkConf object to configure the Spark application
    SparkConf conf = new SparkConf().setAppName("Word Count").setMaster("local[*]");

    // Create a JavaSparkContext object to connect to the Spark cluster
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Load the text file into an RDD
    JavaRDD<String> lines = sc.textFile("input.txt");

    // Split each line into words and create a new RDD of words
    JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

    // Map each word to a key-value pair of (word, 1) and reduce by key to count the occurrences of each word
    JavaPairRDD<String, Integer> wordCounts = words
        .mapToPair(word -> new Tuple2<>(word, 1))
        .reduceByKey(Integer::sum);

    // Print the word counts
    wordCounts.foreach(pair -> {
      if (pair._1.toLowerCase().contains("pattern")) {
        System.out.println(pair._1() + ": " + pair._2());
      }
    });

    // Stop the Spark context
    sc.stop();
  }
}