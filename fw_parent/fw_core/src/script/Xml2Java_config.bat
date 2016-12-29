rem RMT2 FW Core Configuration binding...

del /Q ..\..\src\java\main\com\api\config\jaxb\*.*
xjc -d ../../src/java/main/ -p com.api.config.jaxb ../resources/xml/RMT2AppServerConfig.xsd