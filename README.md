# dig-mrs-dr
Code for use in the directed research project

to compile : mvn install

to run the program:

mvn exec:java -Dexec.mainClass="edu.isi.mrs.pos.POSTagger" -Dexec.args="--filepath <Absolute_path_to_input_file> --outputpath <absolute_path_to_output_file>"
