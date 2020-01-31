package com.learn.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WordCount {

    public static class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        public WCMapper() {
            System.out.println("Mapper Loaded");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while(tokenizer.hasMoreTokens())
                context.write(new Text(tokenizer.nextToken()), new IntWritable(1));
        }
    }

    public static class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        public WCReducer() {
            System.out.println("Reducer Loaded");
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            Iterator valueIterator = values.iterator();
            while (valueIterator.hasNext()) {
                sum += ((IntWritable)valueIterator.next()).get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        System.out.println("Class loaded successfully");
        Configuration conf = new Configuration();
        Job job = new Job(conf, "WordCount");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.out.println("Class finished successfully");

        System.exit(job.waitForCompletion(true) == true ? 0 : -1);
    }
}
