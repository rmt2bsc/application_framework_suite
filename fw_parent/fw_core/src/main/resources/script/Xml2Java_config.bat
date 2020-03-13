rem RMT2 FW Core Configuration binding...

del /Q ..\..\java\com\api\config\jaxb\*.*
xjc -d ../../java/ -p com.api.config.jaxb ../xml/RMT2AppServerConfig.xsd