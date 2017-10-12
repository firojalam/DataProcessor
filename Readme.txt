######## AIDR ################
###### Firoj Alam ####
###### QCRI ####


1. AIDR data processor


2. Shuffolk outlier detection / suspicious tweet detection
usage: java -classpath <DataProcessor.jar>
            qa.org.qcri.DataProcessor.DataProcessor -i input.file -o
            output.arff -f tsv
 -f <file format[arff|csv|tsv]>   option can be chosen for a particular
                                  file format, default is arff
 -i <input-file>                  input file should contain json file list.
 -o <output-file>                 please use name of the output file.

java -classpath bin/DataProcessor/DataProcessor.jar qa.org.qcri.suffolk.JSONPipeline -i  file_list/json_file_list.txt -o shuffolk_marathon_data.arff -f tsv

java -classpath bin/DataProcessor/DataProcessor.jar qa.org.qcri.sampling.DataDistribution -i data/all_events_data_filtered_train.csv -o data/all_events_data_filtered_us_train.csv
