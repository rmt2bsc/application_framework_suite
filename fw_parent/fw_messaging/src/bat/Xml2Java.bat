rem echo off
rem
rem In order to execute this file either include in OS Path or navigate to the directory 
rem where it resides and type at the command line prompt, xml_bind <return key>.
rem 

del /Q ..\test\java\org\rmt2\jaxbtest\*.*
xjc -extension -d ..\test\java -p org.rmt2.jaxbtest ..\test\resources\xml\schemas\
rem copy C:\ProjectServer\RMT2WebServiceMessages\gen\com\bindings\xml\jaxb\*.* C:\ProjectServer\RMT2WebServiceMessages\resources\xml\schemas\bindings