# In order to execute this file either include in OS Path or navigate to the directory 
# where it resides and type at the command line prompt, ./XmlJava.sh <return key>.
# 

rm ../main/java/org/rmt2/jaxb/*
xjc -extension -d ../main/java -p org.rmt2.jaxb -b ../main/resources/xml/schemas/bindings.xml ../main/resources/xml/schemas/