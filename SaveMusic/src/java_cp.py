import glob, os, sys

i = 0
file_str = "javac -cp \"";
for file in glob.glob("*.jar"):
	file_str += file + ";";
			
file_str += ".\" " + sys.argv[1] + ".java";
print(file_str);

i = 0
file_str = "java -cp \"";
for file in glob.glob("*.jar"):
	file_str += file + ";";
	
file_str += ".\" " + sys.argv[1].replace("/", ".");	

print("");
print(file_str);